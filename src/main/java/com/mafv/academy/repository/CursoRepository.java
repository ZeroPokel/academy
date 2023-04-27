package com.mafv.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mafv.academy.models.Curso;
import com.mafv.academy.models.Docente;

public interface CursoRepository extends JpaRepository<Curso, Integer>{
    
    Curso findByTutor(Docente tutor);
}
