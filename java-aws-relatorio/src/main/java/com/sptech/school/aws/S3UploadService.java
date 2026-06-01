package com.sptech.school.aws;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class S3UploadService {

    public static void uploadPdf(S3Client s3Client, String bucket, String key, byte[] pdfBytes) {

        PutObjectRequest request = PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType("application/pdf")
                        .build();

        s3Client.putObject(request, RequestBody.fromBytes(pdfBytes));

        System.out.println("PDF enviado com sucesso para: s3://"
                        + bucket
                        + "/"
                        + key
        );
    }
}
