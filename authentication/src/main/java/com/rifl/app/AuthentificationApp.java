package com.rifl.app;

import com.rifl.app.role.Role;
import com.rifl.app.role.RoleRepository;
import com.rifl.app.role.UserRole;
import com.rifl.app.service.Service;
import com.rifl.app.service.ServiceRepository;
import com.rifl.app.user.entities.User;
import com.rifl.app.user.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
@SpringBootApplication
public class AuthentificationApp {
    public static void main(String[] args) {
        SpringApplication.run(AuthentificationApp.class, args);
    }
    @Bean
    public CommandLineRunner runner(RoleRepository roleRepository,
                                    ServiceRepository serviceRepository,
                                    UserRepository userRepository,
                                     PasswordEncoder passwordEncoder
) {
        return args -> {
            // Check if services exist, if not, add them
            List<String> serviceNames = Arrays.asList("Administratif", "Informatique", "Ingenierie", "Technique", "Commercial");
            for (String serviceName : serviceNames) {
                if (serviceRepository.findByName(serviceName) == null) {
                    Service service = new Service();
                    service.setName(serviceName);
                    serviceRepository.save(service);
                }
            }

            // Check and add roles if needed
            if (roleRepository.findByName("USER").isEmpty()) {
                roleRepository.save(Role.builder().name("USER").build());
            }
            if (roleRepository.findByName("ADMIN").isEmpty()) {
                roleRepository.save(Role.builder().name("ADMIN").build());
            }
            if (roleRepository.findByName("SUPER").isEmpty()) {
                roleRepository.save(Role.builder().name("SUPER").build());
            }

            //Adding super admin
            String adminEmail = "z.khadija@rifl.fr";
            LocalDate contractStartDate = LocalDate.parse("2001-02-01");
            LocalDate contractEndDate = LocalDate.parse("2040-02-01");
            LocalDate dateOfBirth = LocalDate.parse("1972-02-03");
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                var roles = new ArrayList<Role>();
                Service adminService = serviceRepository.findByName("Administratif");
                if (adminService == null) {
                    System.out.println("Admin service not found");
                } else {
                    System.out.println("Admin service found: " + adminService.getName());
                }
                Role adminRole = roleRepository.findByName("ADMIN")
                        .orElseThrow(() -> new IllegalStateException("Role ADMIN not found"));
                roles.add(adminRole);
                User adminUser = User.builder()
                        .firstName("khadija")
                        .lastName("ZIANE")
                        .email(adminEmail)
                        .password(passwordEncoder.encode("123456789"))
                        .identifier("z123456789")
                        .accountLocked(false)
                        .enabled(true)  // Ensure the admin user is enabled
                        .roles(roles)
                        .cin("Z12345")
                        .contractStartDate(contractStartDate)
                        .contractEndDate(contractEndDate)
                        .dateOfBirth(dateOfBirth)
                        .firstPhoneNumber("0681877185")
                        .lastPhoneNumber("0581877185")
                        .matricule("zz21242567")
                        .service(adminService)
                        .build();

                userRepository.save(adminUser);
            }

        };
    }



}

