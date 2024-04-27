package site.inferno_team.TempLogger.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.inferno_team.TempLogger.config.JwtService;
import site.inferno_team.TempLogger.models.auth.AuthenticateRequest;
import site.inferno_team.TempLogger.models.auth.AuthenticationResponse;
import site.inferno_team.TempLogger.models.auth.RegisterRequest;
import site.inferno_team.TempLogger.models.user.Role;
import site.inferno_team.TempLogger.models.user.User;
import site.inferno_team.TempLogger.repositories.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final JwtService jwtService;

    private final AuthenticationManager manager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticateRequest request) {

        manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }


    public AuthenticationResponse authenticateWeb(AuthenticateRequest request) {

        manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    

    public Optional<User> getCurrentUser(HttpServletRequest request) {
        if (!(request.getAttribute("user") instanceof User user)) {
            return Optional.empty();
        }
        return Optional.of(user);
    }
}
