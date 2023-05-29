package com.mafv.academy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mafv.academy.models.EstudianteModulo;
import com.mafv.academy.models.EstudianteModuloKey;

public interface EstudianteModuloRepository extends JpaRepository<EstudianteModulo, EstudianteModuloKey>{
    public void deleteByEstudianteCodigo(int codigo);
    public void deleteByModuloCodigo(int codigo);
    public List<EstudianteModulo> findByModuloCodigo(int codigo);
}
