package com.mafv.academy.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Curso {
    
    @Id
    @GeneratedValue
    private int codigo;

    private String nombre;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tutor", referencedColumnName = "codigo")
    private Docente tutor;

    @OneToOne(mappedBy = "curso")
    private Matricula matricula;

    @OneToMany(mappedBy = "curso")
    private List<Estudiante> alumnos;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "curso_modulos", joinColumns = @JoinColumn(name = "Curso_codigo"), inverseJoinColumns = @JoinColumn(name = "Modulo_codigo"))
    private List<Modulo> modulos;

    public Curso() {
    }

    public Curso(int codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Docente getTutor() {
        return tutor;
    }

    public void setTutor(Docente tutor) {
        this.tutor = tutor;
    }

    public List<Estudiante> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<Estudiante> alumnos) {
        this.alumnos = alumnos;
    }

    public List<Modulo> getModulos() {
        return modulos;
    }

    public void setModulos(List<Modulo> modulos) {
        this.modulos = modulos;
    }

    


}
