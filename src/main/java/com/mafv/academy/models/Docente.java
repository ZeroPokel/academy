package com.mafv.academy.models;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("Docente")
public class Docente extends Usuario{

    @OneToOne(mappedBy = "tutor")
    private Curso curso;

    @OneToMany(mappedBy = "docente")
    private List<Modulo> modulos;

    @Transient
    private boolean tutor;

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<Modulo> getModulos() {
        return modulos;
    }

    public void setModulos(List<Modulo> modulos) {
        this.modulos = modulos;
    }

    public boolean isEsTutor() {
        return tutor;
    }

    public void setEsTutor(boolean tutor) {
        this.tutor = tutor;
    }

    
}
