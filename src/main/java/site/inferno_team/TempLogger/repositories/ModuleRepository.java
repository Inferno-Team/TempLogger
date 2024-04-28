package site.inferno_team.TempLogger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import site.inferno_team.TempLogger.models.module.Module;

public interface ModuleRepository extends JpaRepository<Module,Integer>{
    
}
