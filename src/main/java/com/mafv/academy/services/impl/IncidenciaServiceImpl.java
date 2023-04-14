package com.mafv.academy.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mafv.academy.models.Incidencia;
import com.mafv.academy.repository.IncidenciaRepository;
import com.mafv.academy.services.IncidenciaService;

@Service
public class IncidenciaServiceImpl implements IncidenciaService{

    @Autowired
    IncidenciaRepository repository;

    @Override
    public List<Incidencia> findAll() {
        return repository.findAll();
    }

    @Override
    public Incidencia findById(int id) {
        Optional<Incidencia> findById = repository.findById(id);
        if(findById != null){
            return findById.get();
        }
        return null;
    }

    @Override
    public Incidencia save(Incidencia incidencia) {
        return repository.save(incidencia);
    }

    @Override
    public void update(Incidencia incidencia) {
        repository.save(incidencia);
    }

    @Override
    public void deleteById(int id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
    
}
