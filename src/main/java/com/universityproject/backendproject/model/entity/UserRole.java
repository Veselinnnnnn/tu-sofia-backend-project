package com.universityproject.backendproject.model.entity;

import com.universityproject.backendproject.model.enums.UserRoleEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRole extends BaseEntity {

    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;
}
