package site.inferno_team.TempLogger.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import site.inferno_team.TempLogger.models.module.EspModule;
import site.inferno_team.TempLogger.models.temperature.Temperature;
import site.inferno_team.TempLogger.repositories.ModuleRepository;
import site.inferno_team.TempLogger.repositories.TemperatureRepository;
import site.inferno_team.TempLogger.services.TemperatureService;
import site.inferno_team.TempLogger.utils.Pair;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {
    private final TemperatureService temperatureService;
    private final TemperatureRepository temperatureRepository;
    private final ModuleRepository moduleRepository;

    @GetMapping("")
    public String getMethodName(Model model) {
        Map<String, List<Pair<String, Long>>> tempratures = temperatureService.allTempratures();
        Map<String, List<Pair<String, Long>>> humidities = temperatureService.allHumidities();

        Gson gson = new Gson();

        model.addAttribute("temperaturesJson", gson.toJson(tempratures));
        model.addAttribute("humiditiesJson", gson.toJson(humidities));

        return "dashboard/index";

    }

    @ResponseBody
    public String generateAndInsertFakeData() {
        Random random = new Random();
        EspModule espModule = moduleRepository.findAll().get(0);
        System.out.println(espModule);

        // Define the date range
        long startMillis = toDate("2024-05-01 00:00:00").getTime();
        long endMillis = toDate("2024-05-01 23:59:59").getTime();

        for (int i = 0; i < 250; i++) {
            // Generate random temperature and humidity values
            String temperature = String.format("%.2f", -20 + random.nextDouble() * (40 + 20));
            String humidity = String.format("%.2f", random.nextDouble() * 100);

            // Generate random createdAt and updatedAt dates within the specified range
            long randomMillis = startMillis + (long) (random.nextDouble() * (endMillis - startMillis));
            Date createdAt = new Date(randomMillis);
            Date updatedAt = new Date(randomMillis + TimeUnit.MINUTES.toMillis(1 + random.nextInt(59)));

            // Create and save the Temperature entity
            Temperature temp = Temperature.builder()
                    .createdAt(createdAt)
                    .module(espModule).temperature(temperature).humidity(humidity)
                    .build();

            temperatureRepository.save(temp);
        }
        return "Done";
    }

    // Helper method to convert String date to Date object
    private Date toDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
