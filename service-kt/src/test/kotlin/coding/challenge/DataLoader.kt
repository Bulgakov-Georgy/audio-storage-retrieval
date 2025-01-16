package coding.challenge

import java.net.URISyntaxException
import java.nio.file.Path

object DataLoader {

    fun loadFile(filename: String): Path {
        try {
            return Path.of(this.javaClass.classLoader.getResource(filename)?.toURI())
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
    }

}
