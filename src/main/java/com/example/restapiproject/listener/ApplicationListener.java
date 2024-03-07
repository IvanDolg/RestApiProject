package com.example.restapiproject.listener;

import com.example.restapiproject.service.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationListener {
    private final UserService userService;

    public ApplicationListener(UserService userService) {
        this.userService = userService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        userService.checkUserDate();
    }
}
