package com.mafv.academy.services;

import java.util.List;

import com.mafv.academy.models.Estudiante;

public interface EstudianteService {
    public List<Estudiante> findAll();
    public Estudiante findById(int idEstudiante);
    public List<Estudiante> findByModulo(int idEstudiante);
    public Estudiante save(Estudiante estudiante);
    public void update(Estudiante estudiante);
    public void deleteById(int idEstudiante);
    public void deleteAll();
}
