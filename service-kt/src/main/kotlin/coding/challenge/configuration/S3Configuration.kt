package coding.challenge.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

/**
 * Represents configuration for S3.
 */
@Configuration
class S3Configuration {

    @Bean
    fun s3Client(properties: S3Properties): S3Client =
        S3Client.builder()
            .region(Region.US_EAST_1)
            .endpointOverride(URI.create(properties.url))
            .forcePathStyle(true)
            .credentialsProvider {
                AwsBasicCredentials.builder()
                    .accessKeyId(properties.accessKey)
                    .secretAccessKey(properties.secretKey)
                    .build()
            }
            .build()

}
