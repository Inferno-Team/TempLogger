package site.inferno_team.TempLogger.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.inferno_team.TempLogger.auth.AuthenticationService;
import site.inferno_team.TempLogger.models.ResponseHelper;
import site.inferno_team.TempLogger.models.temperature.Temperature;
import site.inferno_team.TempLogger.models.user.User;
import site.inferno_team.TempLogger.repositories.TemperatureRepository;

import java.util.ArrayList;

@RequestMapping("/api/v1/temperature")
@RestController
@RequiredArgsConstructor
public class TemperatureController {
    private final AuthenticationService authenticationService;
    private final TemperatureRepository temperatureRepository;

    @PostMapping(path = "/add")
    public ResponseEntity<ResponseHelper<Temperature>> add(
            HttpServletRequest request,
            @RequestParam String temperature,
            @RequestParam String humidity
    ) {
        User user = authenticationService.getCurrentUser(request).orElseThrow();

        Temperature temperatureObject = Temperature.builder().temperature(temperature).humidity(humidity).user(user).build();
        temperatureRepository.save(temperatureObject);
        return ResponseEntity.ok(new ResponseHelper<>("Temperature object created successfully.", 201, temperatureObject));
    }

    @GetMapping(path = "/all")
    @ResponseBody
    public ResponseEntity<ResponseHelper<ArrayList<Temperature>>> getAll(HttpServletRequest request) {
        User user = authenticationService.getCurrentUser(request).orElseThrow();
        ArrayList<Temperature> temperatures = new ArrayList<>(temperatureRepository.findByUser_Id(user.getId()));

        return ResponseEntity.ok(new ResponseHelper<>(
                "All Temperatures",
                200,
                temperatures
        ));
    }
}
