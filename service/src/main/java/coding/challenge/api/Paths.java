package coding.challenge.api;

import lombok.experimental.UtilityClass;

/**
 * Defines a set of constants that specify URI paths.
 */
@UtilityClass
public class Paths {

    public final String AUDIO = "/audio";

    public final String AUDIO_FILE_FORMAT = "/{audioFileFormat}";

    public final String PHRASE = "/phrase";

    public final String PHRASE_ID = "/{phraseId}";

    public final String USER = "/user";

    public final String USER_ID = "/{userId}";

}
