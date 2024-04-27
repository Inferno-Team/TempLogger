package site.inferno_team.TempLogger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import site.inferno_team.TempLogger.models.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    public Optional<User> findByEmail(String email);
}
