package com.mafv.academy.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mafv.academy.models.Curso;
import com.mafv.academy.models.Docente;

public interface CursoService {
    public List<Curso> findAll();
    public Page<Curso> findAll(Pageable page);
    public Curso findById(int id);
    public Curso findByTutor(Docente tutor);
    public Curso save(Curso curso);
    public void update(Curso curso);
    public void deleteById(int id);
    public void deleteAll();

}
