package com.universityproject.backendproject.repository;


import com.universityproject.backendproject.model.entity.UserRole;
import com.universityproject.backendproject.model.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    UserRole findByRole(UserRoleEnum role);
}
