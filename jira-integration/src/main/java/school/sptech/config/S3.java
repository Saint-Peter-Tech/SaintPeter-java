package school.sptech.config;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import java.nio.charset.StandardCharsets;

public class S3 {

    private final S3Client s3Client;
    private final String bucketName;

    public S3(String bucketName) {
        this.bucketName = bucketName;

        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public String lerArquivoJson(String caminho) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(caminho)
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);

            return objectBytes.asString(StandardCharsets.UTF_8);

        } catch (Exception e) {
            System.err.println("Erro ao ler arquivo do S3: " + e.getMessage());
            return null;
        }
    }
}