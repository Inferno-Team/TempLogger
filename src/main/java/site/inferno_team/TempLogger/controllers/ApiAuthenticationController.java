package site.inferno_team.TempLogger.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import site.inferno_team.TempLogger.models.auth.AuthenticateRequest;
import site.inferno_team.TempLogger.models.auth.AuthenticationResponse;
import site.inferno_team.TempLogger.auth.AuthenticationService;
import site.inferno_team.TempLogger.models.auth.RegisterRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class ApiAuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticateRequest request) {
        return ResponseEntity.ok(service.authenticate(request));

    }

}
