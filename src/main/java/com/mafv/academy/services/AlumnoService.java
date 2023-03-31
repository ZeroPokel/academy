package com.mafv.academy.services;

import java.util.List;

import com.mafv.academy.models.Alumno;

public interface AlumnoService {
    public List<Alumno> findAll();
    public Alumno findById(int id);
    public Alumno save(Alumno alumno);
    public void update(int id, Alumno alumno);
    public void deleteById(int id);
    public void deleteAll();
}
