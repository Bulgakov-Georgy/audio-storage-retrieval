package coding.challenge.core.service.impl;

import coding.challenge.DataLoader;
import coding.challenge.configuration.TempPathProperties;
import coding.challenge.core.exception.ErrorCodes;
import coding.challenge.core.exception.ServiceException;
import coding.challenge.core.model.AudioFileFormat;
import coding.challenge.core.model.UserPhrase;
import coding.challenge.core.repository.PhraseRepository;
import coding.challenge.core.repository.UserPhraseRepository;
import coding.challenge.core.repository.UserRepository;
import coding.challenge.core.service.AudioConverter;
import coding.challenge.core.service.FileStorageService;
import org.apache.commons.io.file.PathUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties(TempPathProperties.class)
class DefaultPhraseFileServiceTest {

    private static final long USER_ID = 1L;

    private static final long PHRASE_ID = 1L;

    private static final UserPhrase.UserPhraseId USER_PHRASE_ID = new UserPhrase.UserPhraseId(USER_ID, PHRASE_ID);

    private static final AudioFileFormat AUDIO_FILE_FORMAT = AudioFileFormat.M4A;

    private static final String PHRASE_FILE_PATH = "phrase/file/path";

    private static final Path TEMP_PATH = Path.of("test/tmp/service/audio-files");

    private static final Path M4A_FILE = DataLoader.loadFile("audio-files/1.m4a");

    private static final Path WAV_FILE = DataLoader.loadFile("audio-files/1.wav");

    private static final MultipartFile MULTIPART_FILE;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PhraseRepository phraseRepository;

    @Mock
    private UserPhraseRepository userPhraseRepository;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private AudioConverter audioConverter;

    @Mock
    private TempPathProperties tempPath;

    @InjectMocks
    private DefaultPhraseFileService service;

    static {
        try {
            MULTIPART_FILE = new MockMultipartFile("name", "1.m4a", null, Files.readAllBytes(M4A_FILE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(TEMP_PATH);
    }

    @AfterEach
    void tearDown() throws IOException {
        Mockito.verifyNoMoreInteractions(
                userRepository,
                phraseRepository,
                userPhraseRepository,
                fileStorageService,
                audioConverter,
                tempPath
        );
        PathUtils.deleteDirectory(TEMP_PATH);
    }

    @Test
    public void getPhraseFileWithUserNotFound() {
        when(userRepository.existsById(USER_ID)).thenReturn(false);

        assertThatThrownBy(() -> service.getPhraseFile(USER_ID, PHRASE_ID, AUDIO_FILE_FORMAT))
                .isInstanceOf(ServiceException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCodes.OBJECT_NOT_FOUND);
    }

    @Test
    public void getPhraseFileWithPhraseNotFound() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(phraseRepository.existsById(PHRASE_ID)).thenReturn(false);

        assertThatThrownBy(() -> service.getPhraseFile(USER_ID, PHRASE_ID, AUDIO_FILE_FORMAT))
                .isInstanceOf(ServiceException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCodes.OBJECT_NOT_FOUND);
    }

    @Test
    public void getPhraseFileWithUserPhraseNotFound() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(phraseRepository.existsById(PHRASE_ID)).thenReturn(true);
        when(userPhraseRepository.findById(new UserPhrase.UserPhraseId(USER_ID, PHRASE_ID)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getPhraseFile(USER_ID, PHRASE_ID, AUDIO_FILE_FORMAT))
                .isInstanceOf(ServiceException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCodes.OBJECT_NOT_FOUND);
    }

    @Test
    public void getPhraseFileWithSuccess() throws IOException, EncoderException {
        when(tempPath.getAudioFiles()).thenReturn(TEMP_PATH);
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(phraseRepository.existsById(PHRASE_ID)).thenReturn(true);
        when(userPhraseRepository.findById(USER_PHRASE_ID))
                .thenReturn(Optional.of(new UserPhrase(USER_PHRASE_ID, PHRASE_FILE_PATH)));
        when(fileStorageService.retrieve(PHRASE_FILE_PATH)).thenReturn(Files.readAllBytes(WAV_FILE));
        doAnswer(invocation -> {
            Files.copy(M4A_FILE, invocation.getArgument(1, Path.class), StandardCopyOption.REPLACE_EXISTING);
            return null;
        })
                .when(audioConverter)
                .fromWav(any(), any(), any());

        byte[] actual = service.getPhraseFile(USER_ID, PHRASE_ID, AUDIO_FILE_FORMAT);

        ArgumentCaptor<Path> expectedFileCaptor = ArgumentCaptor.captor();
        verify(audioConverter).fromWav(
                ArgumentMatchers.argThat(
                        file -> file.startsWith(tempPath.getAudioFiles()) && file.toString().endsWith(".wav")
                ),
                expectedFileCaptor.capture(),
                eq(AUDIO_FILE_FORMAT)
        );
        Path expectedFile = expectedFileCaptor.getValue();
        assertTrue(expectedFile.startsWith(tempPath.getAudioFiles()) && expectedFile.toString().endsWith(".m4a"));
        assertArrayEquals(Files.readAllBytes(expectedFile), actual);
    }

    @Test
    public void savePhraseFileWithUserNotFound() {
        when(userRepository.existsById(USER_ID)).thenReturn(false);

        assertThatThrownBy(() -> service.savePhraseFile(USER_ID, PHRASE_ID, MULTIPART_FILE))
                .isInstanceOf(ServiceException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCodes.OBJECT_NOT_FOUND);
    }

    @Test
    public void savePhraseFileWithPhraseNotFound() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(phraseRepository.existsById(PHRASE_ID)).thenReturn(false);

        assertThatThrownBy(() -> service.savePhraseFile(USER_ID, PHRASE_ID, MULTIPART_FILE))
                .isInstanceOf(ServiceException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCodes.OBJECT_NOT_FOUND);
    }

    @Test
    public void savePhraseFileWithUnsupportedAudioType() {
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(phraseRepository.existsById(PHRASE_ID)).thenReturn(true);

        MultipartFile multipartFile = new MockMultipartFile("asd.111", new byte[0]);
        assertThatThrownBy(() -> service.savePhraseFile(USER_ID, PHRASE_ID, multipartFile))
                .isInstanceOf(ServiceException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCodes.UNSUPPORTED_AUDIO_TYPE);
    }

    @Test
    public void savePhraseFileWithSuccess() throws EncoderException {
        when(tempPath.getAudioFiles()).thenReturn(TEMP_PATH);
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(phraseRepository.existsById(PHRASE_ID)).thenReturn(true);
        doAnswer(invocation -> {
            Files.copy(WAV_FILE, invocation.getArgument(1, Path.class), StandardCopyOption.REPLACE_EXISTING);
            return null;
        })
                .when(audioConverter)
                .toWav(any(), any());

        service.savePhraseFile(USER_ID, PHRASE_ID, MULTIPART_FILE);

        ArgumentCaptor<Path> expectedFileCaptor = ArgumentCaptor.captor();
        verify(audioConverter).toWav(
                ArgumentMatchers.argThat(
                        file -> file.startsWith(tempPath.getAudioFiles()) && file.toString().endsWith(".m4a")
                ),
                expectedFileCaptor.capture()
        );
        ArgumentCaptor<String> expectedFilenameCaptor = ArgumentCaptor.captor();
        verify(fileStorageService).save(eq(expectedFileCaptor.getValue()), expectedFilenameCaptor.capture());
        verify(userPhraseRepository).save(
                new UserPhrase(new UserPhrase.UserPhraseId(USER_ID, PHRASE_ID), expectedFilenameCaptor.getValue())
        );
    }

}