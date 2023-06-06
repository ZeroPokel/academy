package com.mafv.academy.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    public Estudiante findById(int idEstudiante) {
        Optional<Estudiante> findById = repository.findById(idEstudiante);
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
                EstudianteModuloKey idEstudianteModulo = new EstudianteModuloKey(estudiante.getCodigo(), estudianteModulo.getModulo().getCodigo());
                estudianteModulo.setCodigo(idEstudianteModulo);
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
    public void deleteById(int idEstudiante) {
        repository.deleteById(idEstudiante);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public List<Estudiante> findByModulo(int idModulo) {
        List<EstudianteModulo> estudianteModulos = estudianteModuloRepository.findByModuloCodigo(idModulo);
        List<Estudiante> estudiantes = new ArrayList<Estudiante>();

        for (EstudianteModulo estMod : estudianteModulos){
            estudiantes.add(estMod.getEstudiante());
        }

        return estudiantes;
    }

}
