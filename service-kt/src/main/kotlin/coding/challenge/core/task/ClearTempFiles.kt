package coding.challenge.core.task

import coding.challenge.configuration.TempPathProperties
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path

/**
 * Defines a set of methods for clearing temporary files.
 */
@Component
class ClearTempFiles(private val tempPath: TempPathProperties) {

    /**
     * Clears temporary audio files at specified intervals.
     */
    @Scheduled(cron = "0 0 0 * * *")
    fun clearTempAudioFiles() {
        log.info("Started clearing temp files in {}", tempPath.audioFiles)
        Files.list(tempPath.audioFiles).use { pathStream ->
            pathStream.forEach { path: Path ->
                try {
                    Files.delete(path)
                } catch (e: Exception) {
                    log.error("Error deleting temp file", e)
                }
            }
        }
        log.info("Finished clearing temp files in {}", tempPath.audioFiles)
    }

    companion object {

        private val log = LoggerFactory.getLogger(this::class.java)

    }

}
