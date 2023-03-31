package com.mafv.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mafv.academy.models.Docente;

public interface DocenteRepository extends JpaRepository<Docente, Integer>{
    
}
