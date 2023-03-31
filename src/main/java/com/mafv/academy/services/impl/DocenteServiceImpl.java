package com.mafv.academy.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mafv.academy.models.Docente;
import com.mafv.academy.repository.DocenteRepository;
import com.mafv.academy.services.DocenteService;

@Service
public class DocenteServiceImpl implements DocenteService{
    
    @Autowired
    DocenteRepository repository;

    @Override
    public List<Docente> findAll() {
        return repository.findAll();
    }

    @Override
    public Docente findById(int id) {
        Optional<Docente> findById = repository.findById(id);
        if(findById != null){
            return findById.get();
        }
        return null;
    }

    @Override
    public Docente save(Docente docente) {
        return repository.save(docente);
    }

    @Override
    public void update(int id, Docente docente) {
        this.findById(id);
        docente.setCodigo(id);
        repository.save(docente);
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