package com.mafv.academy.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("Estudiante")
public class Estudiante extends Usuario{

    @ManyToOne
    @JoinColumn(name = "curso", referencedColumnName = "codigo", nullable = true)
    private Curso curso;

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    
    
}
