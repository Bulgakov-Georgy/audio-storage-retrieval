package coding.challenge.core.service.impl;

import coding.challenge.configuration.S3Properties;
import coding.challenge.core.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Path;

/**
 * S3 implementation of {@link FileStorageService}.
 */
@Service
@RequiredArgsConstructor
public class S3FileStorageService implements FileStorageService {

    private final S3Client s3Client;

    private final S3Properties s3Properties;

    @Override
    public byte[] retrieve(String filePath) throws IOException {
        return s3Client.getObject(
                        GetObjectRequest.builder()
                                .bucket(s3Properties.bucket())
                                .key(filePath)
                                .build()
                )
                .readAllBytes();
    }

    @Override
    public void save(Path file, String filePath) {
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(s3Properties.bucket())
                        .key(filePath)
                        .build(),
                RequestBody.fromFile(file)
        );
    }

}
