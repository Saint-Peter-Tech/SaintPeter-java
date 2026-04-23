package school.sptech.app;

import school.sptech.Empresa;
import school.sptech.Unidade;
import school.sptech.config.Jira;

import java.util.*;

public class App {
    public static void main(String[] args) throws Exception {
        // Definindo as credenciais do Jira
        String baseUrl = "";
        String email = "";
        String apiToken = "";
        Jira jira = new Jira(baseUrl, email, apiToken);

        // Criando Unidades
        List<Unidade> unidadesPhilips = new ArrayList<>();
        Unidade unidadePhilips1 = new Unidade(1, "São Luiz Itaim");
        Unidade unidadePhilips2 = new Unidade(2, "São Luiz Morumbi");
        unidadesPhilips.addAll(Arrays.asList(unidadePhilips1, unidadePhilips2));
        List<Unidade> unidadesCmos = new ArrayList<>();
        Unidade unidadeCmos1 = new Unidade(1, "Unimed ABC");
        Unidade unidadeCmos2 = new Unidade(2, "Unimed Guarulhos");
        unidadesCmos.addAll(Arrays.asList(unidadeCmos1, unidadeCmos2));
        List<Unidade> unidadesProtec = new ArrayList<>();
        Unidade unidadeProtec1 = new Unidade(1, "Einstein Alphaville");
        Unidade unidadeProtec2 = new Unidade(2, "Einstein Ibirapuera");
        unidadesProtec.addAll(Arrays.asList(unidadeProtec1, unidadeProtec2));

        // Criando empresas
        List<Empresa> empresas = new ArrayList<>();
        Empresa empresa1 = new Empresa(1, "Philips", "PHILIPS", unidadesPhilips);
        Empresa empresa2 = new Empresa(2, "CMOS Drake", "CMOS", unidadesCmos);
        Empresa empresa3 = new Empresa(3, "Protec", "PROTEC", unidadesProtec);
        empresas.addAll(Arrays.asList(empresa1, empresa2, empresa3));

        // Criando lista de alertas possíveis
        Map<Integer, String> alertas = new HashMap<>(Map.of(
                1, ": Uso de rede acima do recomendado",
                2, ": Porcentagem de utilização de monitores acima de 85%",
                3, ": Quantidade elevada de monitores com risco de obsolescência",
                4, ": Monitores com calibração atrasada"
        ));

        // Gerando alerta para uma unidade específica
        Integer unidadeIdAleatorio = (int) (Math.random() * 2);
        Integer empresaIdAleatorio = (int) (Math.random() * 3) + 1;
        Integer alertaIdAleatorio = (int) (Math.random() * 4) + 1;

        for (Empresa empresaAtual : empresas) {
            if (empresaAtual.getId().equals(empresaIdAleatorio)){
                String nomeUnidade = empresaAtual.getUnidades().get(unidadeIdAleatorio).getNome();
                String response = jira.createIssue(
                        empresaAtual.getKey(), // Key do projeto
                        nomeUnidade + alertas.get(alertaIdAleatorio), // Nome da issue
                        "Task" // Tipo da issue
                );
                System.out.println(response);
                return;
            }
        }
    }
}
