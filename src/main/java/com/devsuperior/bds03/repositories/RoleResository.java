package com.devsuperior.bds03.repositories;

import com.devsuperior.bds03.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleResository extends JpaRepository<Role,Long> {
}
