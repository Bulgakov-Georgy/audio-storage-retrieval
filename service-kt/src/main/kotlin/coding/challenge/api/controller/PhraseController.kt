package coding.challenge.api.controller

import coding.challenge.api.Parameters
import coding.challenge.api.Paths
import coding.challenge.core.model.AudioFileFormat
import coding.challenge.core.service.PhraseFileService
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

/**
 * Implements a set of REST API methods related to phrases.
 */
@RestController
@RequestMapping(Paths.AUDIO + Paths.USER + Paths.USER_ID + Paths.PHRASE)
class PhraseController(private val phraseFileService: PhraseFileService) {

    @GetMapping(Paths.PHRASE_ID + Paths.AUDIO_FILE_FORMAT)
    fun getPhraseFile(
        @PathVariable(Parameters.USER_ID) userId: Long,
        @PathVariable(Parameters.PHRASE_ID) phraseId: Long,
        @PathVariable(Parameters.AUDIO_FILE_FORMAT) audioFileFormat: AudioFileFormat,
    ): ResponseEntity<Resource> {
        val response = phraseFileService.getPhraseFile(userId, phraseId, audioFileFormat)
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(ByteArrayResource(response))
    }

    @PostMapping(Paths.PHRASE_ID)
    fun savePhraseFile(
        @PathVariable(Parameters.USER_ID) userId: Long,
        @PathVariable(Parameters.PHRASE_ID) phraseId: Long,
        @RequestPart(Parameters.AUDIO_FILE) file: MultipartFile,
    ) = phraseFileService.savePhraseFile(userId, phraseId, file)

}
