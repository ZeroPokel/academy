package com.mafv.academy.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mafv.academy.models.Curso;
import com.mafv.academy.models.Docente;
import com.mafv.academy.models.Estudiante;
import com.mafv.academy.models.EstudianteModulo;
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
    public Modulo findById(int idModulo) {
        Optional<Modulo> findById = repository.findById(idModulo);
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
    public void deleteById(int idModulo) {
        repository.deleteById(idModulo);
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
    public void deleteModuloFromCurso(int idModulo, int idCurso) {
        Optional<Modulo> moduloOptional = repository.findById(idModulo);

        if (moduloOptional.isPresent()) {
            Modulo modulo = moduloOptional.get();
            
            if (modulo.getCurso().getCodigo() == idCurso) {
                modulo.setCurso(null);
            } 

            estudianteModuloRepository.deleteByModuloCodigo(idModulo);

            repository.save(modulo);
        } 
    }

    @Override
    public void deleteEstudianteFromModulo(int idModulo, int idEstudiante) {
        
        estudianteModuloRepository.deleteByModuloCodigoAndEstudianteCodigo(idModulo, idEstudiante);
    }

    @Override
    public Boolean findModuloEstudiante(int idModulo){
        List<EstudianteModulo> estudianteModulo = estudianteModuloRepository.findByModuloCodigo(idModulo);

        return estudianteModulo.size() == 0 ? true : false;
    }

    @Override
    public void deleteAllEstudianteFromModulo(int idModulo) {
        estudianteModuloRepository.deleteByModuloCodigo(idModulo);
    }

    @Override
    public List<EstudianteModulo> findByEstudiante(int idEstudiante) {
        return estudianteModuloRepository.findByEstudianteCodigo(idEstudiante);
    }

    @Override
    public List<EstudianteModulo> findEstudiantesByModulo(int idModulo){
        return estudianteModuloRepository.findByModuloCodigo(idModulo);
    }
}
