package com.mafv.academy.models;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("Estudiante")
public class Estudiante extends Usuario{

    @Transient
    private List<EstudianteModulo> estudianteModulos;

    public List<EstudianteModulo> getEstudianteModulos() {
        return estudianteModulos;
    }

    public void setEstudianteModulos(List<EstudianteModulo> estudianteModulos) {
        this.estudianteModulos = estudianteModulos;
    }
    
}
