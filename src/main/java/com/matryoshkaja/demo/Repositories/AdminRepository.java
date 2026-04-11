package com.matryoshkaja.demo.Repositories;

import com.matryoshkaja.demo.Entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Long>{
}
