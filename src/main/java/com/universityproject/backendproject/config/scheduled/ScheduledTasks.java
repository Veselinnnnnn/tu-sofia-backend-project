package com.universityproject.backendproject.config.scheduled;

import com.universityproject.backendproject.service.application.ApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class ScheduledTasks {

    private final ApplicationService applicationService;

    @Scheduled(cron = "0 0 * * * ?")
    public void updateApplicationStatuses() {
        applicationService.updateStatuses();
    }
}

