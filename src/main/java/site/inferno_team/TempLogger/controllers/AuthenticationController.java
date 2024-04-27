package site.inferno_team.TempLogger.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import site.inferno_team.TempLogger.auth.AuthenticationService;
import site.inferno_team.TempLogger.models.auth.AuthenticateRequest;
import site.inferno_team.TempLogger.models.auth.AuthenticationResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
 private final AuthenticationService service;
    @GetMapping("/login")
    public String authenticateView(Model model) {
        return "index";
    }

      @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticateRequest request) {
                System.out.println(request.getEmail());
                System.out.println(request.getPassword());
        return ResponseEntity.ok(service.authenticate(request));

    }
}
