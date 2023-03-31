package com.mafv.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mafv.academy.models.Incidencia;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Integer>{
    
}
