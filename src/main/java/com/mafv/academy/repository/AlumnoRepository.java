package com.mafv.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mafv.academy.models.Alumno;

public interface AlumnoRepository extends JpaRepository<Alumno, Integer>{
    
}
