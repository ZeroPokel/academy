package com.mafv.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mafv.academy.models.Permiso;

public interface PermisoRepository extends JpaRepository<Permiso, String>{
    
}
