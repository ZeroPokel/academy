package com.mafv.academy.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Modulo {
    
    @Id
    @GeneratedValue
    private int codigo;

    private String nombre;
    
    @ManyToOne
    @JoinColumn(name = "docente", referencedColumnName = "codigo", nullable = true)
    private Docente docente;

    @ManyToOne
    @JoinColumn(name = "curso", referencedColumnName = "codigo", nullable = true)
    private Curso curso;

    @OneToMany(mappedBy = "modulo")
    private List<EstudianteModulo> nota;

    public Modulo() {
    }

    public Modulo(int codigo) {
        this.codigo = codigo;
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

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<EstudianteModulo> getNota() {
        return nota;
    }

    public void setNota(List<EstudianteModulo> nota) {
        this.nota = nota;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + codigo;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Modulo other = (Modulo) obj;
        if (codigo != other.codigo)
            return false;
        return true;
    }

    

}
