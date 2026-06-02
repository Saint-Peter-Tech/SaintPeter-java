package com.sptech.school;

import com.sptech.school.aws.S3Config;
import com.sptech.school.aws.S3Empresa;
import com.sptech.school.aws.S3UploadService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;

import java.time.LocalDate;
import java.util.List;

@Component
public class RelatorioScheduler {

    private final S3Client client = S3Config.criarClient();


    @Scheduled(fixedRate = 60000)
    public void gerarRelatorios() {
        String bucket = "saintpeter";

        List<String> empresas = S3Empresa.listarEmpresas(client, bucket);

        try {
            for (String empresaAtual : empresas) {
                System.out.println("Gerando relatório: " + empresaAtual);

                String caminhoJson = empresaAtual + "modelos/modelos.json";

                List<Modelo> modelos = LeitorJson.lerModelos(client, bucket, caminhoJson);

                byte[] pdf = GerarPdf.gerar(modelos);

                String caminhoPdf = empresaAtual + "relatorios/relatorioModelo.pdf";

                S3UploadService.uploadPdf(client, bucket, caminhoPdf, pdf);
            }
        } catch (Exception e) {
            System.out.println("Erro ao gerar relatório: " + e.getMessage());
        }
    }



}
