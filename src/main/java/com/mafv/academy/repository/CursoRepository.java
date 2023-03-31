package com.mafv.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mafv.academy.models.Curso;

public interface CursoRepository extends JpaRepository<Curso, Integer>{
    
}
