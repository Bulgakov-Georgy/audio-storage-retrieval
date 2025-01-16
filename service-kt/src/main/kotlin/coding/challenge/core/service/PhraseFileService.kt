package coding.challenge.core.service

import coding.challenge.core.model.AudioFileFormat
import org.springframework.web.multipart.MultipartFile

/**
 * Defines a set of methods related to management of phrase file.
 */
interface PhraseFileService {

    /**
     * Returns phrase file in the specified format that belongs to the specified user.
     *
     * @param userId   The user identifier
     * @param phraseId The phrase identifier
     * @param format   The format in which file should be returned
     * @return File in the specified format as a byte array
     */
    fun getPhraseFile(userId: Long, phraseId: Long, format: AudioFileFormat): ByteArray

    /**
     * Saves phrase file
     *
     * @param userId   The user identifier
     * @param phraseId The phrase identifier
     * @param file     The file to save
     */
    fun savePhraseFile(userId: Long, phraseId: Long, file: MultipartFile)

}
