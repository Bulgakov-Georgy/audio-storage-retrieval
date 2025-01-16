package coding.challenge.core.model

import ws.schild.jave.encode.AudioAttributes

/**
 * Defines possible audio file formats with some of their properties.
 */
enum class AudioFileFormat(
    /**
     * Value of the format, basically extension.
     */
    val value: String,
    /**
     * Format name that should be used when the type is an input.
     */
    val inputFormat: String,
    /**
     * Format name that should be used when the type is an output.
     */
    val outputFormat: String,
    /**
     * Attributes controlling the audio encoding process.
     */
    val audioAttributes: AudioAttributes,
) {
    M4A(
        "m4a",
        "m4a",
        "mp3",
        AudioAttributes()
            .setCodec("libmp3lame")
            .setBitRate(128000)
            .setChannels(2)
            .setSamplingRate(44100)
    ),
    WAV(
        "wav",
        "wav",
        "wav",
        AudioAttributes()
            .setCodec("pcm_s16le")
            .setBitRate(128000)
            .setChannels(2)
            .setSamplingRate(44100)
    );

    /**
     * Returns audio file extension (eg. ".wav") for the audio file format.
     */
    val extension: String = ".$value"

    companion object {

        /**
         * Returns audio file format that corresponds to the passed value. If there is no such enum constant [IllegalArgumentException] is thrown.
         *
         * @param value The audio file format string
         * @return AudioFileFormat
         */
        fun fromValue(value: String?): AudioFileFormat {
            for (format in entries) {
                if (format.value.equals(value, ignoreCase = true)) {
                    return format
                }
            }
            throw IllegalArgumentException("Unknown value $value")
        }

    }

}
