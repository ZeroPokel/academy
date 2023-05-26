package com.mafv.academy.models;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("Estudiante")
public class Estudiante extends Usuario{

    @ManyToOne
    @JoinColumn(name = "curso", referencedColumnName = "codigo", nullable = true)
    private Curso curso;

    @Transient
    private List<EstudianteModulo> estudianteModulos;

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<EstudianteModulo> getEstudianteModulos() {
        return estudianteModulos;
    }

    public void setEstudianteModulos(List<EstudianteModulo> estudianteModulos) {
        this.estudianteModulos = estudianteModulos;
    }
    
}
