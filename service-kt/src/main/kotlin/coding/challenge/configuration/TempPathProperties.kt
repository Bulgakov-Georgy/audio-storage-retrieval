package coding.challenge.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import java.nio.file.Files
import java.nio.file.Path

/**
 * Represents configuration properties for temporary file paths.
 */
@ConfigurationProperties(prefix = "file.temp.path")
class TempPathProperties(audioFiles: String) {

    /**
     * Directory for audio files
     */
    val audioFiles: Path = Path.of(audioFiles)

    init {
        Files.createDirectories(this.audioFiles)
    }

}
