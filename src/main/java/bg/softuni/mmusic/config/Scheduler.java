package bg.softuni.mmusic.config;

import bg.softuni.mmusic.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
@Slf4j
@Component
public class Scheduler {
    private final UserService userService;
    private Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);

    public Scheduler(UserService userService) {
        this.userService = userService;
    }

    @Scheduled(cron = "0 0 12 * * *") // every day at 12:00
    public void deleteUnActivatedUsers(){
        userService.deleteUnActivatedUsers(ZonedDateTime.now().minusMonths(1).toLocalDate());
    }
}
