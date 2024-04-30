package site.inferno_team.TempLogger.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import site.inferno_team.TempLogger.models.module.EspModule;
import site.inferno_team.TempLogger.models.temperature.Temperature;
import site.inferno_team.TempLogger.repositories.ModuleRepository;
import site.inferno_team.TempLogger.repositories.TemperatureRepository;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/temp")
public class TempController {
    private TemperatureRepository temperatureRepository;
    private ModuleRepository moduleRepository;

    @GetMapping("/temp/{module_id}")
    public List<Temperature> getTempratureByUserId(@RequestParam("module_id") int id) {
        return temperatureRepository.findByModuleId(id);
    }

    @PostMapping("/store")
    public String storeTemperature(@RequestBody Map<String, String> request) {
        String temp = request.get("temperature");
        String humidity = request.get("humidity");
        String moduleId = request.get("moduleId");
        EspModule module = moduleRepository.findById(Integer.parseInt(moduleId)).orElseThrow();
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
