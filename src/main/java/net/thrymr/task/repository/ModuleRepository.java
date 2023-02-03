package net.thrymr.task.repository;

import net.thrymr.task.entity.MyModule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<MyModule, Long> {
}
