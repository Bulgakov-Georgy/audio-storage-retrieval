package coding.challenge.core.task;

import coding.challenge.configuration.TempPathProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Defines a set of methods for clearing temporary files.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ClearTempFiles {

    private final TempPathProperties tempPath;

    /**
     * Clears temporary audio files at specified intervals.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void clearTempAudioFiles() throws IOException {
        log.info("Started clearing temp files in {}", tempPath.getAudioFiles());
        try (Stream<Path> pathStream = Files.list(tempPath.getAudioFiles())) {
            pathStream.forEach(path -> {
                try {
                    Files.delete(path);
                } catch (Exception e) {
                    log.error("Error deleting temp file", e);
                }
            });
        }
        log.info("Finished clearing temp files in {}", tempPath.getAudioFiles());
    }

}
