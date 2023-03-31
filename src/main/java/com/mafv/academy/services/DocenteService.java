package com.mafv.academy.services;

import java.util.List;

import com.mafv.academy.models.Docente;

public interface DocenteService {
    public List<Docente> findAll();
    public Docente findById(int id);
    public Docente save(Docente docente);
    public void update(int id, Docente docente);
    public void deleteById(int id);
    public void deleteAll();
}