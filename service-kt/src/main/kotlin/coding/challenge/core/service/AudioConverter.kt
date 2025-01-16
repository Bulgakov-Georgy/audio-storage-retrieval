package coding.challenge.core.service

import coding.challenge.core.model.AudioFileFormat
import ws.schild.jave.EncoderException
import java.nio.file.Path

/**
 * Defines a set of methods for converting audio files.
 */
interface AudioConverter {

    /**
     * Converts file to "wav".
     *
     * @param source File that will be converted
     * @param target Destination of the new file
     * @throws EncoderException If there was an error in the encoding process
     */
    fun toWav(source: Path, target: Path)

    /**
     * Converts file from "wav" to the specified type.
     *
     * @param source File that will be converted
     * @param target Destination of the new file
     * @param format Format of the new file
     * @throws EncoderException If there was an error in the encoding process
     */
    fun fromWav(source: Path, target: Path, format: AudioFileFormat)

}
