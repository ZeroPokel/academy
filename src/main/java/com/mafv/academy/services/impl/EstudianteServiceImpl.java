package com.mafv.academy.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mafv.academy.models.Estudiante;
import com.mafv.academy.repository.EstudianteRepository;
import com.mafv.academy.services.EstudianteService;

@Service
@Transactional
public class EstudianteServiceImpl implements EstudianteService{
    
    @Autowired
    EstudianteRepository repository;

    @Override
    public List<Estudiante> findAll() {
        return repository.findAll();
    }

    @Override
    public Estudiante findById(int id) {
        Optional<Estudiante> findById = repository.findById(id);
        if(findById != null){
            return findById.get();
        }
        return null;
    }

    @Override
    public Estudiante save(Estudiante estudiante) {
        return repository.save(estudiante);
    }

    @Override
    public void update(Estudiante estudiante) {
        
        if (estudiante.getFoto() == null || estudiante.getFoto().length <= 0){
            Estudiante estudianteDB = findById(estudiante.getCodigo());
            estudiante.setFoto(estudianteDB.getFoto());
        }

        repository.save(estudiante);
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
