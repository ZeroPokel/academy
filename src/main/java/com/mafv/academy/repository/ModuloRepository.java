package com.mafv.academy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mafv.academy.models.Curso;
import com.mafv.academy.models.Docente;
import com.mafv.academy.models.Modulo;

public interface ModuloRepository extends JpaRepository<Modulo, Integer>{
    
    List<Modulo> findByDocente(Docente docente);
    List<Modulo> findByCurso(Curso curso);
}
