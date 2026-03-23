package com.revplay.revplay_user_service.repository;

import com.revplay.revplay_user_service.Enum.RoleName;
import com.revplay.revplay_user_service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(RoleName roleName);
}
