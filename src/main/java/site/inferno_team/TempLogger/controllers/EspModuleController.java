package site.inferno_team.TempLogger.controllers;

import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import site.inferno_team.TempLogger.models.module.EspModule;
import site.inferno_team.TempLogger.models.user.Role;
import site.inferno_team.TempLogger.models.user.User;
import site.inferno_team.TempLogger.repositories.ModuleRepository;
import site.inferno_team.TempLogger.repositories.UserRepository;

@Controller
@RequestMapping("/module")
@RequiredArgsConstructor
public class EspModuleController {
    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @PostMapping("/store")
    @ResponseBody
    public String createNewEsp(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String mac = request.get("mac");
        User espUser = User.builder()
                .firstname("esp32-" + name)
                .lastname("no-last-name")
                .email(mac + "@templogger.com")
                .password(encoder.encode("password"))
                .role(Role.MODULE).build();
        userRepository.save(espUser);
        EspModule module = EspModule.builder().name(name).macAddress(mac).user(espUser).build();
        moduleRepository.save(module);

        return "esp32 created successfully.";
    }

    @GetMapping("/create")
    public String createNewEspView() {
        return "module/create";

    }
}
