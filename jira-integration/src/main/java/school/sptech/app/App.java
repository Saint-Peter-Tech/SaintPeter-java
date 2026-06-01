package school.sptech.app;

import org.json.JSONObject;
import school.sptech.Hospital;
import school.sptech.Unidade;
import school.sptech.config.Jira;
import school.sptech.config.S3;
import school.sptech.config.Slack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        String bucketS3 = "";

        Jira jira = new Jira(
                "",
                "",
                ""
        );

        S3 s3 = new S3(bucketS3);
        String webhookCanalGeral = "";

        List<Unidade> unidades = new ArrayList<>(Arrays.asList(
                new Unidade(1, "Unidade Vila da Saúde", ""),
                new Unidade(2, "Unidade Jardim Paulista", ""),
                new Unidade(3, "Unidade Sptech", "")
        ));

        Hospital hospital = new Hospital(1, "Hospital Nova Esperança", "PHILIPS", unidades, webhookCanalGeral);
        String dataUltimaVerificacao = "";

        System.out.println("Monitoramento Iniciado...");

        while (true) {
            try {
                System.out.println("Verificando S3 por atualizações...");

                String caminhoMacroS3 = String.format("client/empresa_2/hospital_%d/hospital.json", hospital.getId());
                String hospitalJsonTexto = s3.lerArquivoJson(caminhoMacroS3);

                if (hospitalJsonTexto != null) {
                    JSONObject hospitalJson = new JSONObject(hospitalJsonTexto);
                    String ultimaAtualizacaoJson = hospitalJson.getString("ultimaAtualizacao");

                    if (!ultimaAtualizacaoJson.equals(dataUltimaVerificacao)) {
                        System.out.println("Nova atualização da ETL detectada!");
                        dataUltimaVerificacao = ultimaAtualizacaoJson;

                        enviarRelatorioGeral(hospitalJson, hospital.getUrl());

                        processarAlertasPorResumo(hospitalJson, hospital, jira);
                    } else {
                        System.out.println("Nenhuma mudança no hospital.json desde a última checagem.");
                    }
                }
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                System.out.println("O loop foi interrompido: " + e.getMessage());
                break;
            } catch (Exception e) {
                System.out.println("Erro no monitoramento: " + e.getMessage());
            }
        }
    }

    public static void enviarRelatorioGeral(JSONObject hospitalJson, String webhookGeral) throws Exception {
        String nomeHospital = hospitalJson.getString("nome");
        String dataHora = hospitalJson.getString("ultimaAtualizacao");

        JSONObject alertasSemanais = hospitalJson.getJSONObject("alertasSemanais");
        JSONObject componentesGerais = alertasSemanais.getJSONObject("porComponente");
        JSONObject criticos = hospitalJson.getJSONObject("criticos");
        JSONObject componentesCriticos = criticos.getJSONObject("porComponente");

        StringBuilder relatorio = new StringBuilder();
        relatorio.append(String.format("📊 *Nova captura realizada - %s*\n", nomeHospital));
        relatorio.append(String.format("📅 _Horário: %s_\n\n", dataHora));

        relatorio.append("⚠️ *Alertas Gerais Semanais:*\n");
        relatorio.append(String.format("• CPU: %d | RAM: %d | Disco: %d | Rede: %d\n",
                componentesGerais.getInt("cpu"), componentesGerais.getInt("ram"),
                componentesGerais.getInt("disco"), componentesGerais.getInt("rede")));
        relatorio.append(String.format("*Total: %d*\n\n", alertasSemanais.getInt("totalAlertas")));

        relatorio.append("🚨 *Alertas Críticos Ativos:*\n");
        relatorio.append(String.format("• CPU: %d | RAM: %d | Disco: %d | Rede: %d\n",
                componentesCriticos.getInt("cpuCritico"), componentesCriticos.getInt("ramCritico"),
                componentesCriticos.getInt("discoCritico"), componentesCriticos.getInt("redeCritico")));
        relatorio.append(String.format("*Total Críticos: %d*\n\n", criticos.getInt("totalCriticos")));

        JSONObject payloadSlack = new JSONObject().put("text", relatorio.toString());
        Slack.sendMessage(webhookGeral, payloadSlack);
        System.out.println("Relatório enviado ao Slack Geral.");
    }

    private static void processarAlertasPorResumo(JSONObject hospitalJson, Hospital hospital, Jira jira) {
        JSONObject unidadesJson = hospitalJson.getJSONObject("unidades");

        for (String idKey : unidadesJson.keySet()) {
            JSONObject uJson = unidadesJson.getJSONObject(idKey);
            int criticosDaUnidade = uJson.getInt("totalCriticos");

            if (criticosDaUnidade > 0) {
                int idUnidadeS3 = Integer.parseInt(idKey);
                Unidade unidadeAlvo = null;

                for (Unidade uni : hospital.getUnidades()) {
                    if (uni.getId() == idUnidadeS3) {
                        unidadeAlvo = uni;
                        break;
                    }
                }

                if (unidadeAlvo == null) {
                    System.out.println("⚠Unidade ID " + idKey + " não encontrada.");
                    continue;
                }

                JSONObject detalhesCriticos = uJson.getJSONObject("detalhesCriticos");

                verificarECriticarComponente(unidadeAlvo, "CPU", detalhesCriticos.getInt("cpuCritico"), hospital, jira);
                verificarECriticarComponente(unidadeAlvo, "RAM", detalhesCriticos.getInt("ramCritico"), hospital, jira);
                verificarECriticarComponente(unidadeAlvo, "Disco", detalhesCriticos.getInt("discoCritico"), hospital, jira);
                verificarECriticarComponente(unidadeAlvo, "Rede", detalhesCriticos.getInt("redeCritico"), hospital, jira);
            }
        }
    }

    private static void verificarECriticarComponente(Unidade unidade, String componente, int quantidadeCriticos, Hospital hospital, Jira jira) {
        if (quantidadeCriticos > 0) {
            String msgAlerta = String.format(
                    "🚨 *ALERTA CRÍTICO DE INFRAESTRUTURA*\n" +
                            "• *Local:* %s\n" +
                            "• *Componente Afetado:* %s\n" +
                            "• *Ocorrências Críticas:* %d registradas na última hora.\n" +
                            "• *Troubleshooting inicial:* Verificar dashboard de alertas e entrar em contato com suporte técnico local.",
                    unidade.getNome(), componente, quantidadeCriticos
            );

            try {
                JSONObject pacoteUnidade = new JSONObject().put("text", msgAlerta);
                Slack.sendMessage(unidade.getUrl(), pacoteUnidade);

                String tituloJira = String.format("Incidente Crítico - %s na %s (%d ocorrências)",
                        componente, unidade.getNome(), quantidadeCriticos);

                jira.createIssue(hospital.getKey(), tituloJira, "Task");

                System.out.println("Incidente de " + componente + " enviado para " + unidade.getNome() + " via Slack/Jira.");
            } catch (Exception e) {
                System.out.println("Falha ao despachar alerta: " + e.getMessage());
            }
        }
    }
}