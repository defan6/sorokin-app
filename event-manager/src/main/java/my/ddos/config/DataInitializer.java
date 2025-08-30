package my.ddos.config;

import lombok.RequiredArgsConstructor;
import my.ddos.enums.UserRole;
import my.ddos.model.entity.Event;
import my.ddos.model.entity.Role;
import my.ddos.model.entity.User;
import my.ddos.model.entity.Venue;
import my.ddos.repository.EventRepository;
import my.ddos.repository.RoleRepository;
import my.ddos.repository.UserRepository;
import my.ddos.repository.VenueRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args){

        if(roleRepository.count() == 0){
            Role userRole = new Role(UserRole.ROLE_USER);
            Role adminRole = new Role(UserRole.ROLE_ADMIN);
            roleRepository.saveAll(List.of(userRole, adminRole));
        }
        if(userRepository.findByUsername("admin").isEmpty()){
            Role adminRole = roleRepository.findByRole(UserRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(UserRole.ROLE_ADMIN)));
            User admin = new User();
            admin.setUsername("admin");
            admin.setFullName("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.getUserRoles().add(adminRole);
            userRepository.save(admin);
        }
    }


    @Bean
    CommandLineRunner dataLoader(EventRepository eventRepository, VenueRepository venueRepository, UserRepository userRepository) {
        return args -> {
            List<Venue> venues = venueRepository.findAll();
            User organizer = userRepository.findById(1L).orElseThrow(); // какой-то орг

            List<Event> events = new ArrayList<>();
            int counter = 1;
            for (int i = 0; i < 1000; i++) {
                Venue venue = venues.get(i % venues.size()); // распределение по 10 venue
                Event event = new Event();
                event.setTitle("Event " + counter);
                event.setDescription("Description for event " + counter);
                event.setEventDate(LocalDateTime.now().plusDays(counter));
                event.setVenue(venue);
                event.setOrganizer(organizer);

                events.add(event);
                counter++;
            }

            eventRepository.saveAll(events);
        };
    }
}
