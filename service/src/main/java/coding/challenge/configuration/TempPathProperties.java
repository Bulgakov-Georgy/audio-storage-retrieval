package coding.challenge.configuration;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Represents configuration properties for temporary file paths.
 */
@ConfigurationProperties(prefix = "file.temp.path")
@Getter
public class TempPathProperties {

    /**
     * Directory for audio files
     */
    private final Path audioFiles;

    public TempPathProperties(String audioFiles) throws IOException {
        this.audioFiles = Path.of(audioFiles);
        Files.createDirectories(this.audioFiles);
    }

}
