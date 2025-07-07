package com.EmaDeveloper.ExpenseTracker.roles.repository;

import com.EmaDeveloper.ExpenseTracker.roles.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    boolean existsByName(String name);

    void deleteByName(String name);
}
