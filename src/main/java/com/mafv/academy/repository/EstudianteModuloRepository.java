package com.mafv.academy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mafv.academy.models.EstudianteModulo;
import com.mafv.academy.models.EstudianteModuloKey;

public interface EstudianteModuloRepository extends JpaRepository<EstudianteModulo, EstudianteModuloKey>{
    public void deleteByEstudianteCodigo(int idEstudiante);
    public void deleteByModuloCodigo(int idModulo);
    public void deleteByModuloCodigoAndEstudianteCodigo(int idModulo, int idEstudiante);
    public List<EstudianteModulo> findByModuloCodigo(int idModulo);
    public List<EstudianteModulo> findByEstudianteCodigo(int idEstudiante);
    public Integer countByModuloCodigo(int idModulo);
}
