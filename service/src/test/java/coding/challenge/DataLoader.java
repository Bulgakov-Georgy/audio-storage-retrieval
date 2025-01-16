package coding.challenge;

import java.net.URISyntaxException;
import java.nio.file.Path;

public final class DataLoader {

    private DataLoader() {
    }

    public static Path loadFile(String filename) {
        try {
            return Path.of(DataLoader.class.getClassLoader().getResource(filename).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
