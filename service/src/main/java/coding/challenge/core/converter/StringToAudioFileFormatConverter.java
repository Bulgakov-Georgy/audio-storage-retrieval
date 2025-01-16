package coding.challenge.core.converter;

import coding.challenge.core.model.AudioFileFormat;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts string to {@link AudioFileFormat}.
 */
@Component
public class StringToAudioFileFormatConverter implements Converter<String, AudioFileFormat> {

    @Override
    public AudioFileFormat convert(String source) {
        return AudioFileFormat.fromValue(source);
    }

}
