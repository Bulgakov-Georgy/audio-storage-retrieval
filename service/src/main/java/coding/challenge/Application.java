package coding.challenge;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Entry point of the application.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

}
