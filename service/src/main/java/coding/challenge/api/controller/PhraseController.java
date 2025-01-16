package coding.challenge.api.controller;

import coding.challenge.api.Parameters;
import coding.challenge.api.Paths;
import coding.challenge.core.model.AudioFileFormat;
import coding.challenge.core.service.PhraseFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implements a set of REST API methods related to phrases.
 */
@RestController
@RequestMapping(Paths.AUDIO + Paths.USER + Paths.USER_ID + Paths.PHRASE)
@RequiredArgsConstructor
public class PhraseController {

    private final PhraseFileService phraseFileService;

    @GetMapping(Paths.PHRASE_ID + Paths.AUDIO_FILE_FORMAT)
    public ResponseEntity<Resource> getPhraseFile(
            @PathVariable(Parameters.USER_ID) long userId,
            @PathVariable(Parameters.PHRASE_ID) long phraseId,
            @PathVariable(Parameters.AUDIO_FILE_FORMAT) AudioFileFormat audioFileFormat
    ) {
        byte[] response = phraseFileService.getPhraseFile(userId, phraseId, audioFileFormat);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(response));
    }

    @PostMapping(Paths.PHRASE_ID)
    public void savePhraseFile(
            @PathVariable(Parameters.USER_ID) long userId,
            @PathVariable(Parameters.PHRASE_ID) long phraseId,
            @RequestPart(Parameters.AUDIO_FILE) MultipartFile file
    ) {
        phraseFileService.savePhraseFile(userId, phraseId, file);
    }

}
