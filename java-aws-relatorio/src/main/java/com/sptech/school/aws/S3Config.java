package com.sptech.school.aws;

import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class S3Config {

    private static final String ACCESS_KEY = "";
    private static final String SECRET_KEY = "";
    private static final String SESSION_TOKEN = "";

    public static S3Client criarClient(){
        AwsSessionCredentials credentials =
                AwsSessionCredentials.create(
                        ACCESS_KEY,
                        SECRET_KEY,
                        SESSION_TOKEN
                );

        return S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(
                        StaticCredentialsProvider.create(credentials)
                )
                .build();
    }
}
