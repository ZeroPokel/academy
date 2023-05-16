package com.mafv.academy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mafv.academy.models.Curso;
import com.mafv.academy.models.Estudiante;

public interface EstudianteRepository extends JpaRepository<Estudiante, Integer>{
    
    List<Estudiante> findByCurso(Curso curso);

}
