package com.mafv.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mafv.academy.models.Docente;
import com.mafv.academy.models.Modulo;

public interface ModuloRepository extends JpaRepository<Modulo, Integer>{
    
    Modulo findByDocente(Docente docente);
}
