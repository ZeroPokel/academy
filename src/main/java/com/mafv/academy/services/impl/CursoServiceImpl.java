package com.mafv.academy.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mafv.academy.models.Curso;
import com.mafv.academy.repository.CursoRepository;
import com.mafv.academy.services.CursoService;

@Service
public class CursoServiceImpl implements CursoService{
    
    @Autowired
    CursoRepository repository;

    @Override
    public List<Curso> findAll() {
        return repository.findAll();
    }

    @Override
    public Curso findById(int id) {
        Optional<Curso> findById = repository.findById(id);
        if(findById != null){
            return findById.get();
        }
        return null;
    }

    @Override
    public Curso save(Curso curso) {
        return repository.save(curso);
    }

    @Override
    public void update(int id, Curso curso) {
        this.findById(id);
        curso.setCodigo(id);
        repository.save(curso);
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
