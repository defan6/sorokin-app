package com.ddos.auth;

import com.ddos.auth.model.dto.register.RegisterRequest;
import com.ddos.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class AuthApplication implements CommandLineRunner {

    private final AuthService authService; // ваш AuthServiceImpl

    @Override
    public void run(String... args) {
        int total = 1_000_000;
        int adminRatio = 6; // каждая 6-я запись будет админом

        for (int i = 1; i <= total; i++) {
            RegisterRequest req = new RegisterRequest();
            req.setUsername("user" + i);
            req.setFullName("Full Name " + i);
            req.setPassword("pass" + i); // ⚠️ в реале только хэш

            if (i % adminRatio == 0) {
                authService.registerAdmin(req);  // сохранение + событие в Kafka
            } else {
                authService.registerUser(req);   // сохранение + событие в Kafka
            }

            if (i % 10_000 == 0) {
                System.out.println("✅ Inserted " + i + " users");
            }
        }

        System.out.println("🎉 Done! Inserted " + total + " users");
    }
}
