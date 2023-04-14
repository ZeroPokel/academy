package com.mafv.academy.services;

import java.util.List;

import com.mafv.academy.models.Incidencia;

public interface IncidenciaService {
    public List<Incidencia> findAll();
    public Incidencia findById(int id);
    public Incidencia save(Incidencia incidencia);
    public void update(Incidencia incidencia);
    public void deleteById(int id);
    public void deleteAll();
}
