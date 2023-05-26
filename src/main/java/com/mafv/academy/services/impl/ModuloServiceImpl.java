package com.mafv.academy.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mafv.academy.models.Curso;
import com.mafv.academy.models.Docente;
import com.mafv.academy.models.Modulo;
import com.mafv.academy.repository.CursoRepository;
import com.mafv.academy.repository.EstudianteModuloRepository;
import com.mafv.academy.repository.ModuloRepository;
import com.mafv.academy.services.ModuloService;

@Service
@Transactional
public class ModuloServiceImpl implements ModuloService{
    
    @Autowired
    ModuloRepository repository;

    @Autowired
    EstudianteModuloRepository estudianteModuloRepository;

    @Autowired
    CursoRepository cursoRepository;

    @Override
    public List<Modulo> findAll() {
        return repository.findAll();
    }

    @Override
    public Modulo findById(int id) {
        Optional<Modulo> findById = repository.findById(id);
        if(findById != null){
            return findById.get();
        }
        return null;
    }

    @Override
    public Modulo save(Modulo modulo) {
        return repository.save(modulo);
    }

    @Override
    public void update(Modulo modulo) {
        repository.save(modulo);
    }

    @Override
    public void deleteById(int id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public List<Modulo> findByDocente(Docente docente) {
        return repository.findByDocente(docente);
    }

    @Override
    public List<Modulo> findByCurso(Curso curso) {
        return repository.findByCurso(curso);
    }

    @Override
    public void deleteModuloFromCurso(int moduloId, int cursoId) {
        Optional<Modulo> moduloOptional = repository.findById(moduloId);

        if (moduloOptional.isPresent()) {
            Modulo modulo = moduloOptional.get();
            
            if (modulo.getCurso().getCodigo() == cursoId) {
                modulo.setCurso(null);
            } 

            estudianteModuloRepository.deleteByModuloCodigo(moduloId);

            repository.save(modulo);
        } 
    }

    @Override
    public void deleteAllModuloFromCurso(int idCurso) {
        Optional<Curso> cursoOptional = cursoRepository.findById(idCurso);
        
        if (cursoOptional.isPresent()) {
            Curso curso = cursoOptional.get();
            List<Modulo> modulosCurso = curso.getModulos();

            for (Modulo modulo : modulosCurso){
                modulo.setCurso(null);
                estudianteModuloRepository.deleteByModuloCodigo(modulo.getCodigo());
                repository.save(modulo);
            }
            
        }

    }
}
