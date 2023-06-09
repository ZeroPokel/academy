package com.mafv.academy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mafv.academy.models.Curso;
import com.mafv.academy.models.Docente;
import com.mafv.academy.models.Estudiante;
import com.mafv.academy.models.Modulo;

public interface ModuloRepository extends JpaRepository<Modulo, Integer>{
    
    List<Modulo> findByDocente(Docente docente);
    List<Modulo> findByCurso(Curso curso);

    @Query("SELECT em.estudiante FROM EstudianteModulo em WHERE em.modulo = :modulo")
    List<Estudiante> findEstudiantesByModulo(@Param("modulo") Modulo modulo);
    List<Modulo> findByNombreContaining(String nombre);
}
