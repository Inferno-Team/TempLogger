package site.inferno_team.TempLogger.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import site.inferno_team.TempLogger.models.module.EspModule;
import site.inferno_team.TempLogger.models.module.EspModuleStatus;
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

    @PostMapping(path = "/store")
    @Transactional
    public String createNewEsp(@RequestParam Map<String, String> request, Model model) {
        System.out.println(request.entrySet());
        if (request.get("name").isEmpty() || request.get("mac").isEmpty() || request.get("ip").isEmpty()) {
            model.addAttribute("error", "one of the sent field is empty.");
            return "module/error";
        }
        String name = request.get("name");
        String mac = request.get("mac");
        String ip = request.get("ip");

        User espUser = User.builder()
                .firstname("esp32-" + name)
                .lastname("no-last-name")
                .email(mac + "@templogger.com")
                .password(encoder.encode("password"))
                .role(Role.MODULE).build();
        userRepository.save(espUser);
        EspModule module = EspModule.builder().name(name).macAddress(mac).ipAddress(ip).user(espUser).build();
        moduleRepository.save(module);
        model.addAttribute("msg", "Module created successfully.");
        return "/module/create";
    }

    @GetMapping("/create")
    public String createNewEspView() {
        return "module/create";

    }

    @GetMapping("/all")
    public String showAllModuelsView(Model model) {
        List<EspModule> espModules = moduleRepository.findAll();
        System.out.println(espModules);
        model.addAttribute("modules", espModules);
        return "module/show-all";
    }

    @PostMapping("/change-status/{id}")
    @ResponseBody
    public String changeModuleStatus(@PathVariable(name = "id") int id) {
        EspModule module = moduleRepository.findById(id).orElseThrow();
        module.setStatus(module.getStatus() == EspModuleStatus.ON ? EspModuleStatus.OFF : EspModuleStatus.ON);
        moduleRepository.save(module);
        // TODO: send command to Module ESP32 to change it status.
        return "Module Status Changed.";
    }

    @GetMapping("/update/{id}")
    public String updateEspView(@PathVariable(name = "id") Optional<Integer> id, Model model) {
        System.out.println(id);
        EspModule module = moduleRepository.findById(id.get()).orElseThrow();
        model.addAttribute("module", module);
        return "module/update";
    }

    @PostMapping("/update")
    public String updateEspValues(@RequestParam Map<String, String> request, Model model) {
        int id = Integer.parseInt(request.get("id"));
        EspModule module = moduleRepository.findById(id).orElseThrow();
        module.setName(request.get("name"));
        module.setIpAddress(request.get("ip"));
        module.setMacAddress(request.get("mac"));
        moduleRepository.save(module);
        model.addAttribute("module", module);
        return "module/update";
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public String deleteEspModule(@PathVariable(name = "id") int id) {
        EspModule module = moduleRepository.findById(id).orElseThrow();
        moduleRepository.delete(module);
        return "Module Removed successfully.";
    }

}
