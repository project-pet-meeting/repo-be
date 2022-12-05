package sideproject.petmeeting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PetMeetingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetMeetingApplication.class, args);
    }

}
