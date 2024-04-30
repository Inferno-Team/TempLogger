package site.inferno_team.TempLogger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import site.inferno_team.TempLogger.models.temperature.Temperature;

import java.util.Date;
import java.util.List;

public interface TemperatureRepository extends JpaRepository<Temperature, Integer> {

    List<Temperature> findByModuleId(int moduleId);

    List<Temperature> findAllByOrderByCreatedAtDesc();

    List<Temperature> findAllByCreatedAtBetweenOrderByCreatedAtAsc(Date startOfDay, Date endOfDay);


}