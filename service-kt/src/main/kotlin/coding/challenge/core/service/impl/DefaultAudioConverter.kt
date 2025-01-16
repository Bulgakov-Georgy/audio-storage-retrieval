package coding.challenge.core.service.impl

import coding.challenge.core.model.AudioFileFormat
import coding.challenge.core.service.AudioConverter
import org.apache.commons.io.file.PathUtils
import org.springframework.stereotype.Service
import ws.schild.jave.Encoder
import ws.schild.jave.MultimediaObject
import ws.schild.jave.encode.EncodingAttributes
import java.nio.file.Path

/**
 * Default implementation of [AudioConverter].
 */
@Service
class DefaultAudioConverter : AudioConverter {

    override fun toWav(source: Path, target: Path) {
        val format = AudioFileFormat.WAV
        val attributes = EncodingAttributes()
            .setInputFormat(AudioFileFormat.fromValue(PathUtils.getExtension(source)).inputFormat)
            .setOutputFormat(format.outputFormat)
            .setAudioAttributes(format.audioAttributes)

        Encoder().encode(MultimediaObject(source.toFile()), target.toFile(), attributes)
    }

    override fun fromWav(source: Path, target: Path, format: AudioFileFormat) {
        val attributes = EncodingAttributes()
            .setInputFormat(AudioFileFormat.WAV.inputFormat)
            .setOutputFormat(format.outputFormat)
            .setAudioAttributes(format.audioAttributes)

        Encoder().encode(MultimediaObject(source.toFile()), target.toFile(), attributes)
    }

}
