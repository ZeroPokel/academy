package com.mafv.academy.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("Estudiante")
public class Estudiante extends Usuario{

    @Transient
    private EstudianteModulo estudianteModulo;

    public EstudianteModulo getEstudianteModulo() {
        return estudianteModulo;
    }

    public void setEstudianteModulo(EstudianteModulo estudianteModulo) {
        this.estudianteModulo = estudianteModulo;
    }

    
    
}
