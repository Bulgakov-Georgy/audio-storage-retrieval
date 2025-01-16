package coding.challenge.core.service.impl

import coding.challenge.DataLoader
import coding.challenge.configuration.TempPathProperties
import coding.challenge.core.exception.ErrorCodes
import coding.challenge.core.exception.ServiceException
import coding.challenge.core.model.AudioFileFormat
import coding.challenge.core.model.UserPhrase
import coding.challenge.core.repository.PhraseRepository
import coding.challenge.core.repository.UserPhraseRepository
import coding.challenge.core.repository.UserRepository
import coding.challenge.core.service.AudioConverter
import coding.challenge.core.service.FileStorageService
import org.apache.commons.io.file.PathUtils
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.invocation.InvocationOnMock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.Optional

@ExtendWith(MockitoExtension::class)
@EnableConfigurationProperties(TempPathProperties::class)
class DefaultPhraseFileServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var phraseRepository: PhraseRepository

    @Mock
    private lateinit var userPhraseRepository: UserPhraseRepository

    @Mock
    private lateinit var fileStorageService: FileStorageService

    @Mock
    private lateinit var audioConverter: AudioConverter

    @Mock
    private lateinit var tempPath: TempPathProperties

    private lateinit var service: DefaultPhraseFileService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Files.createDirectories(TEMP_PATH)
        service = DefaultPhraseFileService(
            tempPath,
            fileStorageService,
            audioConverter,
            userRepository,
            phraseRepository,
            userPhraseRepository
        )
    }

    @AfterEach
    fun tearDown() {
        verifyNoMoreInteractions(
            userRepository,
            phraseRepository,
            userPhraseRepository,
            fileStorageService,
            audioConverter,
            tempPath
        )
        PathUtils.deleteDirectory(TEMP_PATH)
    }

    @Test
    fun phraseFileWithUserNotFound() {
        whenever(userRepository.existsById(USER_ID)).doReturn(false)

        assertThatThrownBy { service.getPhraseFile(USER_ID, PHRASE_ID, AUDIO_FILE_FORMAT) }
            .isInstanceOf(ServiceException::class.java)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCodes.OBJECT_NOT_FOUND)
    }

    @Test
    fun phraseFileWithPhraseNotFound() {
        whenever(userRepository.existsById(USER_ID)).doReturn(true)
        whenever(phraseRepository.existsById(PHRASE_ID)).doReturn(false)

        assertThatThrownBy { service.getPhraseFile(USER_ID, PHRASE_ID, AUDIO_FILE_FORMAT) }
            .isInstanceOf(ServiceException::class.java)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCodes.OBJECT_NOT_FOUND)
    }

    @Test
    fun phraseFileWithUserPhraseNotFound() {
        whenever(userRepository.existsById(USER_ID)).doReturn(true)
        whenever(phraseRepository.existsById(PHRASE_ID)).doReturn(true)
        whenever(userPhraseRepository.findById(UserPhrase.UserPhraseId(USER_ID, PHRASE_ID))).doReturn(Optional.empty())

        assertThatThrownBy { service.getPhraseFile(USER_ID, PHRASE_ID, AUDIO_FILE_FORMAT) }
            .isInstanceOf(ServiceException::class.java)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCodes.OBJECT_NOT_FOUND)
    }

    @Test
    fun phraseFileWithSuccess() {
        whenever(tempPath.audioFiles).doReturn(TEMP_PATH)
        whenever(userRepository.existsById(USER_ID)).doReturn(true)
        whenever(phraseRepository.existsById(PHRASE_ID)).doReturn(true)
        whenever(userPhraseRepository.findById(USER_PHRASE_ID))
            .doReturn(Optional.of(UserPhrase(USER_PHRASE_ID, PHRASE_FILE_PATH)))
        whenever(fileStorageService.retrieve(PHRASE_FILE_PATH)).doReturn(Files.readAllBytes(WAV_FILE))
        Mockito.doAnswer { invocation: InvocationOnMock ->
            Files.copy(M4A_FILE, invocation.getArgument(1, Path::class.java), StandardCopyOption.REPLACE_EXISTING)
            null
        }
            .whenever(audioConverter)
            .fromWav(any(), any(), any())

        val actual = service.getPhraseFile(USER_ID, PHRASE_ID, AUDIO_FILE_FORMAT)

        val expectedFileCaptor = argumentCaptor<Path>()
        verify(audioConverter).fromWav(
            argThat { startsWith(tempPath.audioFiles) && toString().endsWith(".wav") },
            expectedFileCaptor.capture(),
            eq(AUDIO_FILE_FORMAT)
        )
        val expectedFile = expectedFileCaptor.firstValue
        assertTrue(expectedFile.startsWith(tempPath.audioFiles) && expectedFile.toString().endsWith(".m4a"))
        assertArrayEquals(Files.readAllBytes(expectedFile), actual)
    }

    @Test
    fun savePhraseFileWithUserNotFound() {
        whenever(userRepository.existsById(USER_ID)).doReturn(false)

        assertThatThrownBy { service.savePhraseFile(USER_ID, PHRASE_ID, MULTIPART_FILE) }
            .isInstanceOf(ServiceException::class.java)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCodes.OBJECT_NOT_FOUND)
    }

    @Test
    fun savePhraseFileWithPhraseNotFound() {
        whenever(userRepository.existsById(USER_ID)).doReturn(true)
        whenever(phraseRepository.existsById(PHRASE_ID)).doReturn(false)

        assertThatThrownBy { service.savePhraseFile(USER_ID, PHRASE_ID, MULTIPART_FILE) }
            .isInstanceOf(ServiceException::class.java)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCodes.OBJECT_NOT_FOUND)
    }

    @Test
    fun savePhraseFileWithUnsupportedAudioType() {
        whenever(userRepository.existsById(USER_ID)).doReturn(true)
        whenever(phraseRepository.existsById(PHRASE_ID)).doReturn(true)

        val multipartFile: MultipartFile = MockMultipartFile("asd.111", ByteArray(0))
        assertThatThrownBy { service.savePhraseFile(USER_ID, PHRASE_ID, multipartFile) }
            .isInstanceOf(ServiceException::class.java)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCodes.UNSUPPORTED_AUDIO_TYPE)
    }

    @Test
    fun savePhraseFileWithSuccess() {
        whenever(tempPath.audioFiles).doReturn(TEMP_PATH)
        whenever(userRepository.existsById(USER_ID)).doReturn(true)
        whenever(phraseRepository.existsById(PHRASE_ID)).doReturn(true)
        doAnswer { invocation: InvocationOnMock ->
            Files.copy(WAV_FILE, invocation.getArgument(1, Path::class.java), StandardCopyOption.REPLACE_EXISTING)
            null
        }
            .whenever(audioConverter)
            .toWav(any(), any())

        service.savePhraseFile(USER_ID, PHRASE_ID, MULTIPART_FILE)

        val expectedFileCaptor = argumentCaptor<Path>()
        verify(audioConverter).toWav(
            argThat { startsWith(tempPath.audioFiles) && toString().endsWith(".m4a") },
            expectedFileCaptor.capture()
        )
        val expectedFilenameCaptor = argumentCaptor<String>()
        verify(fileStorageService).save(eq(expectedFileCaptor.firstValue), expectedFilenameCaptor.capture())
        verify(userPhraseRepository).save(
            UserPhrase(UserPhrase.UserPhraseId(USER_ID, PHRASE_ID), expectedFilenameCaptor.firstValue)
        )
    }

    companion object {

        private const val USER_ID = 1L

        private const val PHRASE_ID = 1L

        private const val PHRASE_FILE_PATH = "phrase/file/path"

        private val USER_PHRASE_ID = UserPhrase.UserPhraseId(USER_ID, PHRASE_ID)

        private val AUDIO_FILE_FORMAT = AudioFileFormat.M4A

        private val TEMP_PATH: Path = Path.of("test/tmp/service/audio-files")

        private val M4A_FILE: Path = DataLoader.loadFile("audio-files/1.m4a")

        private val WAV_FILE: Path = DataLoader.loadFile("audio-files/1.wav")

        private val MULTIPART_FILE: MultipartFile = MockMultipartFile(
            "name",
            "1.m4a",
            null,
            Files.readAllBytes(M4A_FILE)
        )

    }

}