package com.mafv.academy.services;

import java.util.List;

import com.mafv.academy.models.Curso;
import com.mafv.academy.models.Docente;
import com.mafv.academy.models.Modulo;

public interface ModuloService {
    public List<Modulo> findAll();
    public Modulo findById(int idModulo);
    public Modulo save(Modulo modulo);
    public List<Modulo> findByDocente(Docente docente);
    public List<Modulo> findByCurso(Curso curso);
    public void update(Modulo modulo);
    public void deleteById(int idModulo);
    public void deleteAll();
    public void deleteModuloFromCurso(int idModulo, int idCurso);
    public void deleteEstudianteFromModulo(int idModulo, int idEstudiante);
}
