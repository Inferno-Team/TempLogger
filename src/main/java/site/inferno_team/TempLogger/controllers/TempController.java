package site.inferno_team.TempLogger.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import site.inferno_team.TempLogger.auth.AuthenticationService;
import site.inferno_team.TempLogger.models.module.EspModule;
import site.inferno_team.TempLogger.models.temperature.Temperature;
import site.inferno_team.TempLogger.models.user.User;
import site.inferno_team.TempLogger.repositories.ModuleRepository;
import site.inferno_team.TempLogger.repositories.TemperatureRepository;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/temp")
@RequiredArgsConstructor
public class TempController {
    private final TemperatureRepository temperatureRepository;
    private final ModuleRepository moduleRepository;
    private final AuthenticationService service;

    @GetMapping("/temp/{module_id}")
    public List<Temperature> getTempratureByUserId(@RequestParam("module_id") int id) {
        return temperatureRepository.findByModuleId(id);
    }

    @PostMapping("/store")
    @ResponseBody
    public String storeTemperature(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
        String temp = request.get("temperature");
        String humidity =  request.get("humidity");
        User user = service.getCurrentUser(httpRequest).orElseThrow();
        // String moduleId = request.get("moduleId");
        int moduleId = user.getEspModule().getId();
        EspModule module = moduleRepository.findById(moduleId).orElseThrow();
        Temperature temperature = Temperature.builder().temperature(temp).humidity(humidity).module(module).build();
        temperatureRepository.save(temperature);
        return "temp has been created";
    }

    @PostMapping("/delete")
    public String deleteTemp(int id) {
        temperatureRepository.deleteById(id);
        return "deleted";
    }

    @PostMapping("/update")
    public String updateTemp(@RequestBody Map<String, String> request) {
        Temperature temperature = temperatureRepository.findById(Integer.parseInt(request.get("id"))).orElse(null);
        if (temperature != null) {
            temperature.setTemperature(request.get("temperature"));
            temperatureRepository.save(temperature);
        }
        return "updated";
    }

}
