package com.sptech.school.aws;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CommonPrefix;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;

import java.util.List;

public class S3Empresa {

    public static List<String> listarEmpresas(S3Client s3Client, String bucket) {

        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix("client/")
                .delimiter("/")
                .build();

        return s3Client.listObjectsV2(request)
                .commonPrefixes()
                .stream()
                .map(CommonPrefix::prefix)
                .toList();
    }
}
