package com.saintpetertech;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class GerarLog {
    public static void main(String[] args) throws InterruptedException {


        System.out.println(getTimestamp() + " Inicializando Log...");
        Thread.sleep(3000);

        for(int i = 0; i < 10; i++) {
            int random = gerarRandom(3);
            String mensagem = gerarMensagem(random);

            System.out.println(getTimestamp() + mensagem);

            Thread.sleep(3000);
        }

        System.out.println(getTimestamp() + " Encerrando o programa...");
    }

    // Pegar data e hora formatada [yyyy-MM-dd HH:mm:ss]:
    public static String getTimestamp() {
        LocalDateTime now = LocalDateTime.now();

        String padrao = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatarData = DateTimeFormatter.ofPattern(padrao);

        String timestamp = now.format(formatarData);

        String dataFormatada = "[" + timestamp + "]";
        return dataFormatada;
    }

    // Gerar número random de 0 a 2:
    public static int gerarRandom(int length){
        Random random = new Random();

        int randomInt = random.nextInt(length);
        return randomInt;
    }

    // Sortear tipo de log:
    public static String gerarMensagem(int randomTipo) {

        List<String> listaTipo = new ArrayList<>(Arrays.asList("INFO","ERRO","ALERTA"));
        List<String> listaInfo = new ArrayList<>(Arrays.asList(
                "ETL inicializada pelo usuário: victor@email.com",
                "ETL encerrada pelo usuário: victor@email.com",
                "Login efetuado pelo usuário: ana@email.com (ProLife)",
                "Login efetuado pelo usuário: bruno@email.com (MultiMed)",
                "Login efetuado pelo usuário: jaime@email.com (ProLife)",
                "Login efetuado pelo usuário: carlos@suporte.com (ProLife)",
                "Logout efetuado pelo usuário: ana@email.com (ProLife)",
                "Logout efetuado pelo usuário: bruno@email.com (MultiMed)",
                "Logout efetuado pelo usuário: jaime@email.com (ProLife)",
                "Logout efetuado pelo usuário: carlos@suporte.com (ProLife)"
        ));
        List<String> listaErro = new ArrayList<>(Arrays.asList(
                "Não foi possível conectar ao banco de dados",
                "Tentativa de login: e-mail joao@email.com não cadastrado no sistema",
                "Instância EC2 indisponível no momento",
                "Não foi possível encontrar o arquivo dados_hadware.csv"
        ));
        List<String> listaAlerta = new ArrayList<>(Arrays.asList(
                "CPU > 90% no monitor 1B8D4C62A0E9 (H. Cor - ProLife)",
                "RAM > 75% no monitor 1B8D4C62A0E9 (H. Cor - ProLife)",
                "Temperatura > 95Cº no monitor 1B8D4C62A0E9 (H. Cor - ProLife)",
                "Rede DESCONECTADA no monitor 1B8D4C62A0E9 (H. Cor - ProLife)",
                "CPU > 90% no monitor 4F2A9B81C3D5 (H. Cor - ProLife)",
                "RAM > 75% no monitor 4F2A9B81C3D5 (H. Cor - ProLife)",
                "Temperatura > 95Cº no monitor 4F2A9B81C3D5 (H. Cor - ProLife)",
                "Rede DESCONECTADA no monitor 4F2A9B81C3D5 (H. Cor - ProLife)",
                "CPU > 90% no monitor E7A2C90D5F3B (Hospital São Luiz - MultiMed)",
                "RAM > 75% no monitor E7A2C90D5F3B (Hospital São Luiz - MultiMed)",
                "Temperatura > 95Cº no monitor E7A2C90D5F3B (Hospital São Luiz - MultiMed)",
                "Rede DESCONECTADA no monitor E7A2C90D5F3B (Hospital São Luiz - MultiMed)",
                "CPU > 90% no monitor A89109BD7F37 (Hospital São Luiz - MultiMed)",
                "RAM > 75% no monitor A89109BD7F37 (Hospital São Luiz - MultiMed)",
                "Temperatura > 95Cº no monitor A89109BD7F37 (Hospital São Luiz - MultiMed)",
                "Rede DESCONECTADA no monitor A89109BD7F37 (Hospital São Luiz - MultiMed)"
        ));

        String item;

        String tipo = listaTipo.get(randomTipo);

        int randomItem;

        if(randomTipo == 0) {
            randomItem = gerarRandom(listaInfo.size());
            item = listaInfo.get(randomItem);
        } else if (randomTipo == 1) {
            randomItem = gerarRandom(listaErro.size());
            item = listaErro.get(randomItem);
        } else {
            randomItem = gerarRandom(listaAlerta.size());
            item = listaAlerta.get(randomItem);
        }

        String mensagem = " " + tipo + " " + item;
        return mensagem;
    }

} // Close GerarLog
