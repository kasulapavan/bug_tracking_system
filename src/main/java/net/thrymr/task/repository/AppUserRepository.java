package net.thrymr.task.repository;

import net.thrymr.task.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AppUserRepository extends JpaRepository<AppUser,Long > {

    AppUser findByEmail(String email);
}
