package coding.challenge.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

/**
 * Represents configuration for S3.
 */
@Configuration
public class S3Configuration {

    @Bean
    public S3Client s3Client(S3Properties properties) {
        return S3Client.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(URI.create(properties.url()))
                .forcePathStyle(true)
                .credentialsProvider(
                        () -> AwsBasicCredentials.builder()
                                .accessKeyId(properties.accessKey())
                                .secretAccessKey(properties.secretKey())
                                .build()
                )
                .build();
    }

}
