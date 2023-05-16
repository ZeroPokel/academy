package com.mafv.academy.models;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("Estudiante")
public class Estudiante extends Usuario{

    @ManyToOne
    @JoinColumn(name = "curso", referencedColumnName = "codigo", nullable = true)
    private Curso curso;
    
    
}
