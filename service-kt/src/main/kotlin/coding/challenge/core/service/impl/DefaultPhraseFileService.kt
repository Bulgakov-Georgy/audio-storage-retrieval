package coding.challenge.core.service.impl

import coding.challenge.configuration.TempPathProperties
import coding.challenge.core.exception.ExceptionFactory
import coding.challenge.core.i18n.Messages
import coding.challenge.core.model.AudioFileFormat
import coding.challenge.core.model.UserPhrase
import coding.challenge.core.repository.PhraseRepository
import coding.challenge.core.repository.UserPhraseRepository
import coding.challenge.core.repository.UserRepository
import coding.challenge.core.service.AudioConverter
import coding.challenge.core.service.FileStorageService
import coding.challenge.core.service.PhraseFileService
import org.apache.commons.io.FilenameUtils
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path

/**
 * Default implementation of [PhraseFileService].
 */
@Service
class DefaultPhraseFileService(
    private val tempPath: TempPathProperties,
    private val fileStorageService: FileStorageService,
    private val audioConverter: AudioConverter,
    private val userRepository: UserRepository,
    private val phraseRepository: PhraseRepository,
    private val userPhraseRepository: UserPhraseRepository,
) : PhraseFileService {

    override fun getPhraseFile(userId: Long, phraseId: Long, format: AudioFileFormat): ByteArray {
        validateUserIdAndPhraseId(userId, phraseId)

        val fileKey = userPhraseRepository.findById(UserPhrase.UserPhraseId(userId, phraseId))
            .map { it.phraseFilePath ?: throw ExceptionFactory.createObjectNotFoundException(Messages.FILE_NOT_FOUND) }
            .orElseThrow { ExceptionFactory.createObjectNotFoundException(Messages.FILE_NOT_FOUND) }!!
        try {
            val fileFromStorage = getWavFileFromStorage(fileKey)
            val convertedFile = Files.createTempFile(tempPath.audioFiles, null, format.extension);
            audioConverter.fromWav(fileFromStorage, convertedFile, format)
            return Files.readAllBytes(convertedFile)
        } catch (e: Exception) {
            throw ExceptionFactory.createInternalError(Messages.FAILED_TO_RETRIEVE_FILE, e)
        }
    }

    private fun getWavFileFromStorage(fileKey: String): Path {
        val rawFile = fileStorageService.retrieve(fileKey)
        val file: Path = Files.createTempFile(tempPath.audioFiles, null, AudioFileFormat.WAV.extension)
        return Files.write(file, rawFile)
    }

    override fun savePhraseFile(userId: Long, phraseId: Long, file: MultipartFile) {
        validateUserIdAndPhraseId(userId, phraseId)

        val extension = resolveMultipartFileExtension(file)
        var filename = "${userId}_$phraseId"
        try {
            val originalFile = Files.createTempFile(tempPath.audioFiles, filename, extension)
            val convertedFile = Files.createTempFile(tempPath.audioFiles, filename, AudioFileFormat.WAV.extension);
            file.transferTo(originalFile)
            audioConverter.toWav(originalFile, convertedFile)
            filename += AudioFileFormat.WAV.extension
            fileStorageService.save(convertedFile, filename)
            userPhraseRepository.save(UserPhrase(UserPhrase.UserPhraseId(userId, phraseId), filename))
        } catch (e: Exception) {
            throw ExceptionFactory.createInternalError(Messages.FAILED_TO_SAVE_FILE, e)
        }
    }

    private fun resolveMultipartFileExtension(file: MultipartFile): String {
        val extension = FilenameUtils.getExtension(file.originalFilename)
        try {
            return AudioFileFormat.fromValue(extension).extension
        } catch (e: java.lang.IllegalArgumentException) {
            throw ExceptionFactory.createUnsupportedAudioTypeException(extension)
        }
    }

    private fun validateUserIdAndPhraseId(userId: Long, phraseId: Long) {
        if (!userRepository.existsById(userId)) {
            throw ExceptionFactory.createObjectNotFoundException(Messages.USER_NOT_FOUND)
        }
        if (!phraseRepository.existsById(phraseId)) {
            throw ExceptionFactory.createObjectNotFoundException(Messages.PHRASE_NOT_FOUND)
        }
    }

}
