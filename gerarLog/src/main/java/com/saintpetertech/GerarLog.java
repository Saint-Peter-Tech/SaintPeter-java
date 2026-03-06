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
            int random = gerarRandom();
            String mensagem = gerarMensagem(random);

            System.out.println(getTimestamp() + mensagem);

            Thread.sleep(5000);
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
    public static int gerarRandom(){
        Random random = new Random();

        int randomInt = random.nextInt(3);
        return randomInt;
    }

    // Sortear tipo de log:
    public static String gerarMensagem(int randomTipo) {

        List<String> listaTipo = new ArrayList<>(Arrays.asList("INFO","ERRO","ALERTA"));
        List<String> listaInfo = new ArrayList<>(Arrays.asList("ETL inicializada pelo usuário: jaime@suporte.com ","Login efetuado pelo usuário: ana@email.com","Login efetuado pelo usuário: bruno@email.com"));
        List<String> listaErro = new ArrayList<>(Arrays.asList("Não foi possível conectar ao banco de dados","E-mail joao@email.com não cadastrado no sistema","Instância EC2 indisponível no momento"));
        List<String> listaAlerta = new ArrayList<>(Arrays.asList("CPU > 90% do monitor 4F2A9B81C3D5 do hospital H. Cor","Temperatura > 95Cº do monitor E7A2C90D5F3B do hospital São Luiz","Rede DESCONECTADA no monitor 1B8D4C62A0E9"));

        String item;

        String tipo = listaTipo.get(randomTipo);

        int randomItem = gerarRandom();

        if(randomTipo == 0) {
            item = listaInfo.get(randomItem);
        } else if (randomTipo == 1) {
            item = listaErro.get(randomItem);
        } else {
            item = listaAlerta.get(randomItem);
        }

        String mensagem = " " + tipo + " " + item;
        return mensagem;
    }

} // Close GerarLog
