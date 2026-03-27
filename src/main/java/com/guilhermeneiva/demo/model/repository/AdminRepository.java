package com.guilhermeneiva.demo.model.repository;

import com.guilhermeneiva.demo.model.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Long> {
}
