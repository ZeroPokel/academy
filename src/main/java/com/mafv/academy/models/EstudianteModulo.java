package com.mafv.academy.models;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class EstudianteModulo {

    @EmbeddedId
    private EstudianteModuloKey codigo = new EstudianteModuloKey();

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @MapsId("estudiante_codigo")
    @JoinColumn(name = "estudiante_codigo")
    private Estudiante estudiante;
    
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @MapsId("modulo_codigo")
    @JoinColumn(name = "modulo_codigo")
    private Modulo modulo;

    private float nota;

    public EstudianteModulo() {
    }

    public EstudianteModulo(EstudianteModuloKey codigo) {
        this.codigo = codigo;
    }

    public EstudianteModuloKey getCodigo() {
        return codigo;
    }

    public void setCodigo(EstudianteModuloKey codigo) {
        this.codigo = codigo;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    
}
