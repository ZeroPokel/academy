package com.mafv.academy.services;

import java.util.List;

import com.mafv.academy.models.Curso;

public interface CursoService {
    public List<Curso> findAll();
    public Curso findById(int id);
    public Curso save(Curso curso);
    public void update(int id, Curso curso);
    public void deleteById(int id);
    public void deleteAll();
}
