package com.mafv.academy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mafv.academy.models.Curso;
import com.mafv.academy.models.Docente;
import com.mafv.academy.models.Estudiante;

public interface CursoRepository extends JpaRepository<Curso, Integer>{
    
    Curso findByTutor(Docente tutor);

    @Query("SELECT em.estudiante FROM EstudianteModulo em JOIN em.modulo m WHERE m.curso = :curso")
    List<Estudiante> findEstudiantesByCurso(@Param("curso") Curso curso);
}
