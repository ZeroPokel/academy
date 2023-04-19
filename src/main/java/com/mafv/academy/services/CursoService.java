package com.mafv.academy.services;

import java.util.List;

import com.mafv.academy.models.Curso;
import com.mafv.academy.models.Docente;

public interface CursoService {
    public List<Curso> findAll();
    public Curso findById(int id);
    public Curso findByTutor(Docente docente);
    public Curso save(Curso curso);
    public void update(Curso curso);
    public void deleteById(int id);
    public void deleteAll();
}
