package com.mafv.academy.services;

import java.util.List;

import com.mafv.academy.models.Curso;
import com.mafv.academy.models.Estudiante;

public interface EstudianteService {
    public List<Estudiante> findAll();
    public Estudiante findById(int id);
    public List<Estudiante> findByModulo(int id);
    public Estudiante save(Estudiante alumno);
    public List<Estudiante> findByCurso(Curso curso);
    public void update(Estudiante alumno);
    public void deleteById(int id);
    public void deleteAll();
    public void deleteEstudianteFromCurso(int estudianteId, int cursoId);
    public void deleteAllEstudianteFromCurso(int cursoId);
}
