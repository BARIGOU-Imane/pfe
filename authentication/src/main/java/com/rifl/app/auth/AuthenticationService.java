package com.rifl.app.auth;

import com.rifl.app.role.Role;
import com.rifl.app.service.ServiceRepository;
import com.rifl.app.user.entities.Token;
import com.rifl.app.email.EmailService;
import com.rifl.app.email.EmailTemplateName;
import com.rifl.app.role.RoleRepository;
import com.rifl.app.security.JwtService;
import com.rifl.app.user.entities.User;
import com.rifl.app.user.repositories.TokenRepository;
import com.rifl.app.user.repositories.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;
    private final ServiceRepository serviceRepository;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest request) throws MessagingException {
        var roles = new ArrayList<Role>();
        for (String roleName : request.getRoles()) {
            var role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new IllegalStateException("Role " + roleName + " not found"));
            roles.add(role);
        }
     String serviceName = request.getService();
     com.rifl.app.service.Service service = (com.rifl.app.service.Service) serviceRepository.findByName(serviceName);
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .identifier(request.getIdentifier())
                .accountLocked(false)
                .enabled(false)
                .dateOfBirth(request.getDateOfBirth())
                .matricule(request.getMatricule())
                .contractStartDate(request.getContractStartDate())
                .contractEndDate(request.getContractEndDate())
                .roles(roles)
                .cin(request.getCin())
                .firstPhoneNumber(request.getFirstPhoneNumber())
                .lastPhoneNumber(request.getLastPhoneNumber())
                .service(service)
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getIdentifier(),
                        request.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());
        claims.put("fullName", user.getFullName());

        var jwtToken = jwtService.generateToken(claims, (User) auth.getPrincipal());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
        }

        var user = userRepository.findById(Long.valueOf(savedToken.getUser().getId()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    private String generateAndSaveActivationToken(User user) {
        // Generate a token
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);

        return generatedToken;
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }
}
