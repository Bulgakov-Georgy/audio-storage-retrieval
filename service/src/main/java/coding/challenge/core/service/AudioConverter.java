package coding.challenge.core.service;

import coding.challenge.core.model.AudioFileFormat;
import ws.schild.jave.EncoderException;

import java.nio.file.Path;

/**
 * Defines a set of methods for converting audio files.
 */
public interface AudioConverter {

    /**
     * Converts file to "wav".
     *
     * @param source File that will be converted
     * @param target Destination of the new file
     * @throws EncoderException If there was an error in the encoding process
     */
    void toWav(Path source, Path target) throws EncoderException;

    /**
     * Converts file from "wav" to the specified type.
     *
     * @param source File that will be converted
     * @param target Destination of the new file
     * @param format Format of the new file
     * @throws EncoderException If there was an error in the encoding process
     */
    void fromWav(Path source, Path target, AudioFileFormat format) throws EncoderException;

}
