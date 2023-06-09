package com.mafv.academy.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mafv.academy.models.Curso;
import com.mafv.academy.models.Docente;
import com.mafv.academy.models.Estudiante;
import com.mafv.academy.repository.CursoRepository;
import com.mafv.academy.services.CursoService;

@Service
@Transactional
public class CursoServiceImpl implements CursoService{
    
    @Autowired
    CursoRepository repository;

    @Override
    public List<Curso> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<Curso> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Curso findById(int idCurso) {
        Optional<Curso> findById = repository.findById(idCurso);
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
    public void update(Curso curso) {
        repository.save(curso);
    }

    @Override
    public void deleteById(int idCurso) {
        repository.deleteById(idCurso);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public Curso findByTutor(Docente tutor) {
        return repository.findByTutor(tutor);
    }

    @Override
    public void deleteTutor(int idCurso) {
        Curso curso = repository.findById(idCurso).get();

        curso.setTutor(null);
        repository.save(curso);
    }

    @Override
    public List<Estudiante> findEstudiantesByCurso(Curso curso) {
        return repository.findEstudiantesByCurso(curso);
    }

    @Override
    public List<Curso> findByNombre(String nombre) {
        return repository.findByNombreContaining(nombre);
    }

}
