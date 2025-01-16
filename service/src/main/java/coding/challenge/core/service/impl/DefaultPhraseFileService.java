package coding.challenge.core.service.impl;

import coding.challenge.configuration.TempPathProperties;
import coding.challenge.core.exception.ExceptionFactory;
import coding.challenge.core.i18n.Messages;
import coding.challenge.core.model.AudioFileFormat;
import coding.challenge.core.model.UserPhrase;
import coding.challenge.core.repository.PhraseRepository;
import coding.challenge.core.repository.UserPhraseRepository;
import coding.challenge.core.repository.UserRepository;
import coding.challenge.core.service.AudioConverter;
import coding.challenge.core.service.FileStorageService;
import coding.challenge.core.service.PhraseFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Default implementation of {@link PhraseFileService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultPhraseFileService implements PhraseFileService {

    private final TempPathProperties tempPath;

    private final FileStorageService fileStorageService;

    private final AudioConverter audioConverter;

    private final UserRepository userRepository;

    private final PhraseRepository phraseRepository;

    private final UserPhraseRepository userPhraseRepository;

    @Override
    public byte[] getPhraseFile(long userId, long phraseId, AudioFileFormat format) {
        validateUserIdAndPhraseId(userId, phraseId);

        String fileKey = userPhraseRepository.findById(new UserPhrase.UserPhraseId(userId, phraseId))
                .map(UserPhrase::getPhraseFilePath)
                .orElseThrow(() -> ExceptionFactory.createObjectNotFoundException(Messages.FILE_NOT_FOUND));
        try {
            Path fileFromStorage = getWavFileFromStorage(fileKey);
            Path convertedFile = Files.createTempFile(tempPath.getAudioFiles(), null, format.getExtension());
            audioConverter.fromWav(fileFromStorage, convertedFile, format);
            return Files.readAllBytes(convertedFile);
        } catch (Exception e) {
            throw ExceptionFactory.createInternalError(Messages.FAILED_TO_RETRIEVE_FILE, e);
        }
    }

    private Path getWavFileFromStorage(String fileKey) throws IOException {
        byte[] rawFile = fileStorageService.retrieve(fileKey);
        Path file = Files.createTempFile(tempPath.getAudioFiles(), null, AudioFileFormat.WAV.getExtension());
        return Files.write(file, rawFile);
    }

    @Override
    public void savePhraseFile(long userId, long phraseId, MultipartFile file) {
        validateUserIdAndPhraseId(userId, phraseId);

        String extension = resolveMultipartFileExtension(file);
        String filename = userId + "_" + phraseId;
        try {
            Path originalFile = Files.createTempFile(tempPath.getAudioFiles(), filename, extension);
            Path convertedFile = Files.createTempFile(
                    tempPath.getAudioFiles(),
                    filename,
                    AudioFileFormat.WAV.getExtension()
            );
            file.transferTo(originalFile);
            audioConverter.toWav(originalFile, convertedFile);
            filename = filename + AudioFileFormat.WAV.getExtension();
            fileStorageService.save(convertedFile, filename);
            userPhraseRepository.save(new UserPhrase(new UserPhrase.UserPhraseId(userId, phraseId), filename));
        } catch (Exception e) {
            throw ExceptionFactory.createInternalError(Messages.FAILED_TO_SAVE_FILE, e);
        }
    }

    private String resolveMultipartFileExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        try {
            return AudioFileFormat.fromValue(extension).getExtension();
        } catch (IllegalArgumentException e) {
            throw ExceptionFactory.createUnsupportedAudioTypeException(extension);
        }
    }

    private void validateUserIdAndPhraseId(long userId, long phraseId) {
        if (!userRepository.existsById(userId)) {
            throw ExceptionFactory.createObjectNotFoundException(Messages.USER_NOT_FOUND);
        }
        if (!phraseRepository.existsById(phraseId)) {
            throw ExceptionFactory.createObjectNotFoundException(Messages.PHRASE_NOT_FOUND);
        }
    }

}
