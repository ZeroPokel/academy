package com.mafv.academy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mafv.academy.models.Docente;

public interface DocenteRepository extends JpaRepository<Docente, Integer>{
    
    List<Docente> findByApellidosContaining(String apellidos);
}
