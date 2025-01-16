package coding.challenge.core.service.impl;

import coding.challenge.core.model.AudioFileFormat;
import coding.challenge.core.service.AudioConverter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.file.PathUtils;
import org.springframework.stereotype.Service;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.EncodingAttributes;

import java.nio.file.Path;

/**
 * Default implementation of {@link AudioConverter}.
 */
@Service
@RequiredArgsConstructor
public class DefaultAudioConverter implements AudioConverter {

    @Override
    public void toWav(Path source, Path target) throws EncoderException {
        AudioFileFormat format = AudioFileFormat.WAV;
        EncodingAttributes attributes = new EncodingAttributes()
                .setInputFormat(AudioFileFormat.fromValue(PathUtils.getExtension(source)).getInputFormat())
                .setOutputFormat(format.getOutputFormat())
                .setAudioAttributes(format.getAudioAttributes());

        new Encoder().encode(new MultimediaObject(source.toFile()), target.toFile(), attributes);
    }

    @Override
    public void fromWav(Path source, Path target, AudioFileFormat format) throws EncoderException {
        EncodingAttributes attributes = new EncodingAttributes()
                .setInputFormat(AudioFileFormat.WAV.getInputFormat())
                .setOutputFormat(format.getOutputFormat())
                .setAudioAttributes(format.getAudioAttributes());

        new Encoder().encode(new MultimediaObject(source.toFile()), target.toFile(), attributes);
    }

}
