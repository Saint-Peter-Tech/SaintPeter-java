package com.sptech.school;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LeitorJson {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<Modelo> lerModelos(S3Client s3Client, String bucket, String key) throws IOException {

        GetObjectRequest request = GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build();

        try (var stream = s3Client.getObject(request)) {

            JsonNode root = mapper.readTree(stream);

            JsonNode modelosNode = root.get("modelos");

            Map<String, Modelo> modelos = mapper.convertValue(modelosNode, new TypeReference<Map<String, Modelo>>() {});

            return modelos.values()
                    .stream()
                    .toList();
        }
    }
}
