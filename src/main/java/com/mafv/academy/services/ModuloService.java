package com.mafv.academy.services;

import java.util.List;

import com.mafv.academy.models.Docente;
import com.mafv.academy.models.Modulo;

public interface ModuloService {
    public List<Modulo> findAll();
    public Modulo findById(int id);
    public Modulo save(Modulo modulo);
    public Modulo findByDocente(Docente docente);
    public void update(Modulo modulo);
    public void deleteById(int id);
    public void deleteAll();
}
