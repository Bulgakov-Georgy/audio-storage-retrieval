package coding.challenge.core.converter

import coding.challenge.core.model.AudioFileFormat
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

/**
 * Converts string to [AudioFileFormat].
 */
@Component
class StringToAudioFileFormatConverter : Converter<String, AudioFileFormat> {

    override fun convert(source: String): AudioFileFormat = AudioFileFormat.fromValue(source)

}
