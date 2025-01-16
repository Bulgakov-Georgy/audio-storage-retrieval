package coding.challenge.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ws.schild.jave.encode.AudioAttributes;

/**
 * Defines possible audio file formats with some of their properties.
 */
@RequiredArgsConstructor
@Getter
public enum AudioFileFormat {

    M4A(
            "m4a",
            "m4a",
            "mp3",
            new AudioAttributes()
                    .setCodec("libmp3lame")
                    .setBitRate(128000)
                    .setChannels(2)
                    .setSamplingRate(44100)
    ),
    WAV(
            "wav",
            "wav",
            "wav",
            new AudioAttributes()
                    .setCodec("pcm_s16le")
                    .setBitRate(128000)
                    .setChannels(2)
                    .setSamplingRate(44100)
    );

    /**
     * Value of the format, basically extension.
     */
    private final String value;

    /**
     * Format name that should be used when the type is an input.
     */
    private final String inputFormat;

    /**
     * Format name that should be used when the type is an output.
     */
    private final String outputFormat;

    /**
     * Attributes controlling the audio encoding process.
     */
    private final AudioAttributes audioAttributes;

    /**
     * Returns audio file extension for the audio file format.
     *
     * @return Extension string, eg. ".wav"
     */
    public String getExtension() {
        return "." + value;
    }

    /**
     * Returns audio file format that corresponds to the passed value. If there is no such enum constant {@link IllegalArgumentException} is thrown.
     *
     * @param value The audio file format string
     * @return AudioFileFormat
     */
    public static AudioFileFormat fromValue(String value) {
        for (AudioFileFormat format : AudioFileFormat.values()) {
            if (format.getValue().equalsIgnoreCase(value)) {
                return format;
            }
        }
        throw new IllegalArgumentException("Unknown value " + value);
    }

}
