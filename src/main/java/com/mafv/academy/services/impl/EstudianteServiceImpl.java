package com.mafv.academy.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mafv.academy.models.Curso;
import com.mafv.academy.models.Estudiante;
import com.mafv.academy.models.EstudianteModulo;
import com.mafv.academy.models.EstudianteModuloKey;
import com.mafv.academy.repository.CursoRepository;
import com.mafv.academy.repository.EstudianteModuloRepository;
import com.mafv.academy.repository.EstudianteRepository;
import com.mafv.academy.services.EstudianteService;

@Service
@Transactional
public class EstudianteServiceImpl implements EstudianteService{
    
    @Autowired
    EstudianteRepository repository;

    @Autowired
    EstudianteModuloRepository estudianteModuloRepository;

    @Autowired
    CursoRepository cursoRepository;

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
        repository.save(estudiante);

        List<EstudianteModulo> estudianteModulos = estudiante.getEstudianteModulos();
        if (estudianteModulos != null){
            for (EstudianteModulo estudianteModulo : estudianteModulos){
                EstudianteModuloKey id = new EstudianteModuloKey(estudiante.getCodigo(), estudianteModulo.getModulo().getCodigo());
                estudianteModulo.setCodigo(id);
                estudianteModuloRepository.save(estudianteModulo);
            }
        }
        return estudiante;
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

    @Override
    public List<Estudiante> findByCurso(Curso curso) {
        return repository.findByCurso(curso);
    }

    @Override
    public void deleteEstudianteFromCurso(int estudianteId, int cursoId) {
        Optional<Estudiante> estudianteOptional = repository.findById(estudianteId);

        if (estudianteOptional.isPresent()) {
            Estudiante estudiante = estudianteOptional.get();

            if (estudiante.getCurso().getCodigo() == cursoId) {
                estudiante.setCurso(null);
            } 

            estudianteModuloRepository.deleteByEstudianteCodigo(estudianteId);

            repository.save(estudiante);
        } 
    }

    @Override
    public void deleteAllEstudianteFromCurso(int idCurso) {
        Optional<Curso> cursoOptional = cursoRepository.findById(idCurso);
        
        if (cursoOptional.isPresent()) {
            Curso curso = cursoOptional.get();
            List<Estudiante> estudiantesCurso = curso.getEstudiantes();

            for (Estudiante estudiante : estudiantesCurso){
                estudiante.setCurso(null);
                estudianteModuloRepository.deleteByEstudianteCodigo(estudiante.getCodigo());
                repository.save(estudiante);
            }
            
        }

    }

    @Override
    public List<Estudiante> findByModulo(int id) {
        List<EstudianteModulo> estudianteModulos = estudianteModuloRepository.findByModuloCodigo(id);
        List<Estudiante> estudiantes = new ArrayList<Estudiante>();

        for (EstudianteModulo estMod : estudianteModulos){
            estudiantes.add(estMod.getEstudiante());
        }

        return estudiantes;
    }

}
