package coding.challenge.core.service;

import coding.challenge.core.model.AudioFileFormat;
import org.springframework.web.multipart.MultipartFile;

/**
 * Defines a set of methods related to management of phrase file.
 */
public interface PhraseFileService {

    /**
     * Returns phrase file in the specified format that belongs to the specified user.
     *
     * @param userId   The user identifier
     * @param phraseId The phrase identifier
     * @param format   The format in which file should be returned
     * @return File in the specified format as a byte array
     */
    byte[] getPhraseFile(long userId, long phraseId, AudioFileFormat format);

    /**
     * Saves phrase file
     *
     * @param userId   The user identifier
     * @param phraseId The phrase identifier
     * @param file     The file to save
     */
    void savePhraseFile(long userId, long phraseId, MultipartFile file);

}
