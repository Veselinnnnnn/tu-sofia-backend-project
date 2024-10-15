package com.universityproject.backendproject.repository;

import com.universityproject.backendproject.model.entity.User;
import com.universityproject.backendproject.model.entity.UserRole;
import com.universityproject.backendproject.model.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findAllByRole(UserRole role);

    User findUserById(long id);

    List<User> findAllByAnimalIsNullAndRole(UserRole userRole);

    List<User> findByRole_RoleIn(List<UserRoleEnum> roles);
}
