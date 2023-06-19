package com.mafv.academy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mafv.academy.models.Estudiante;
import com.mafv.academy.models.Modulo;

public interface EstudianteRepository extends JpaRepository<Estudiante, Integer>{

    @Query("SELECT e FROM Estudiante e WHERE NOT EXISTS " +
           "(SELECT em FROM EstudianteModulo em WHERE em.estudiante = e AND em.modulo = :modulo)")
    List<Estudiante> findEstudiantesNotInModulo(Modulo modulo);
    List<Estudiante> findByApellidosContaining(String apellidos);
}
