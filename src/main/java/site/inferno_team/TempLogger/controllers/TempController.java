package site.inferno_team.TempLogger.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.inferno_team.TempLogger.models.temperature.Temperature;
import site.inferno_team.TempLogger.repositories.TemperatureRepository;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/temp")
public class TempController {
    private TemperatureRepository temperatureRepository;
    @GetMapping("/temp/{module_id}")
    public List<Temperature> getTempratureByUserId(@RequestParam("module_id") int id){
        return temperatureRepository.findByModuleId(id);
    }
    @PostMapping("/store")
    public String storeTemperature(@RequestBody Map<String, String> request){
        String temp=request.get("temperature");
        String humidity=request.get("humidity");
        String moduleId=request.get("moduleId");
        Temperature temperature=Temperature.builder().temperature(temp).humidity(humidity).moduleId(moduleId).build();
        temperatureRepository.save(temperature);
        return "temp has been created";
    }
}
