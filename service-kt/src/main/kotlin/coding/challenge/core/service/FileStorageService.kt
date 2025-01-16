package coding.challenge.core.service

import java.io.IOException
import java.nio.file.Path

/**
 * Defines a set of methods for working with a file storage.
 */
interface FileStorageService {

    /**
     * Retrieves file from the file storage that is located in the specified location.
     *
     * @param filePath The full path to the file in the storage
     * @return File as a byte array
     * @throws IOException If there was an error retrieving file
     */
    fun retrieve(filePath: String): ByteArray

    /**
     * Save file to the storage in the specified location
     *
     * @param file     The file to save
     * @param filePath The full path to the file in the storage
     */
    fun save(file: Path, filePath: String)

}
