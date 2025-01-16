package coding.challenge.core.service.impl

import coding.challenge.configuration.S3Properties
import coding.challenge.core.service.FileStorageService
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.nio.file.Path

/**
 * S3 implementation of [FileStorageService].
 */
@Service
class S3FileStorageService(
    private val s3Client: S3Client,
    private val s3Properties: S3Properties,
) : FileStorageService {

    override fun retrieve(filePath: String): ByteArray {
        return s3Client.getObject(
            GetObjectRequest.builder()
                .bucket(s3Properties.bucket)
                .key(filePath)
                .build()
        )
            .readAllBytes()
    }

    override fun save(file: Path, filePath: String) {
        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(s3Properties.bucket)
                .key(filePath)
                .build(),
            RequestBody.fromFile(file)
        )
    }

}
