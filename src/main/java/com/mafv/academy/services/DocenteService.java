package com.mafv.academy.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mafv.academy.models.Docente;

public interface DocenteService {
    public List<Docente> findAll();
    public Page<Docente> findAll(Pageable page);
    public Docente findById(int idDocente);
    public Docente save(Docente docente);
    public void update(Docente docente);
    public void deleteById(int idDocente);
    public void deleteAll();
    public void deleteDocenteFromAllModulo(int idDocente);
}
