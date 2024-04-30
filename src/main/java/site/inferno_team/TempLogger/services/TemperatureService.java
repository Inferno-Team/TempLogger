package site.inferno_team.TempLogger.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.inferno_team.TempLogger.models.module.EspModule;
import site.inferno_team.TempLogger.models.temperature.Temperature;
import site.inferno_team.TempLogger.repositories.TemperatureRepository;
import site.inferno_team.TempLogger.utils.Pair;

@RequiredArgsConstructor
@Service
public class TemperatureService {
    private final TemperatureRepository temperatureRepository;

    private Map<EspModule, List<Pair<String, Long>>> getAllTempratures(String type) {

        LocalDate currentDate = LocalDate.now();

        // Get the start and end timestamps for the current day
        Date startOfDay = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endOfDay = Date.from(currentDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Temperature> temperatures = temperatureRepository
                .findAllByCreatedAtBetweenOrderByCreatedAtAsc(startOfDay, endOfDay);

        Function<Temperature, String> temperatureMapper = Temperature::getTemperature;
        Function<Temperature, String> humidityMapper = Temperature::getHumidity;
        Function<Temperature, Long> createdAtMapper = temp -> temp.getCreatedAt().getTime(); // Assuming createdAt is

        return temperatures.stream()
                .collect(Collectors.groupingBy(Temperature::getModule,
                        Collectors.mapping(temp -> Pair.of(
                                type.equals("temperature") ? temperatureMapper.apply(temp) : humidityMapper.apply(temp),
                                createdAtMapper.apply(temp)),
                                Collectors.toList())));

    }

    public Map<String, List<Pair<String, Long>>> allTempratures() {
        Map<EspModule, List<Pair<String, Long>>> temperatures = this.getAllTempratures("temperature");
        Map<String, List<Pair<String, Long>>> tempMap = new HashMap<>();

        for (Map.Entry<EspModule, List<Pair<String, Long>>> entry : temperatures.entrySet()) {
            EspModule module = entry.getKey();
            String userName = module.getUser().getFirstname();
            List<Pair<String, Long>> temperatureList = entry.getValue();

            tempMap.put(userName, temperatureList);
        }
        return tempMap;
    }

    public Map<String, List<Pair<String, Long>>> allHumidities() {
        Map<EspModule, List<Pair<String, Long>>> humidities = this.getAllTempratures("humidity");
        Map<String, List<Pair<String, Long>>> humMap = new HashMap<>();

        for (Map.Entry<EspModule, List<Pair<String, Long>>> entry : humidities.entrySet()) {
            EspModule module = entry.getKey();
            String userName = module.getUser().getFirstname();
            List<Pair<String, Long>> temperatureList = entry.getValue();

            humMap.put(userName, temperatureList);
        }
        return humMap;
    }

}
