package com.mafv.academy.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mafv.academy.models.Alumno;
import com.mafv.academy.repository.AlumnoRepository;
import com.mafv.academy.services.AlumnoService;

@Service
public class AlumnoServiceImpl implements AlumnoService{
    
    @Autowired
    AlumnoRepository repository;

    @Override
    public List<Alumno> findAll() {
        return repository.findAll();
    }

    @Override
    public Alumno findById(int id) {
        Optional<Alumno> findById = repository.findById(id);
        if(findById != null){
            return findById.get();
        }
        return null;
    }

    @Override
    public Alumno save(Alumno alumno) {
        return repository.save(alumno);
    }

    @Override
    public void update(int id, Alumno alumno) {
        this.findById(id);
        alumno.setCodigo(id);
        repository.save(alumno);
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
