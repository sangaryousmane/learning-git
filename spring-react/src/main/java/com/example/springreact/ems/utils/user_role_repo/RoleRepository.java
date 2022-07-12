package com.example.springreact.ems.utils.user_role_repo;

import com.example.springreact.ems.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}