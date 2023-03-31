package com.mafv.academy.models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Matricula {
    
    @Id
    @GeneratedValue
    private int codigo;
    private float coste;
    private Date fecha;
    private boolean pagado;

    @ManyToOne
    @JoinColumn(name = "alumno", nullable = false)
    private Alumno alumno;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "curso", referencedColumnName = "codigo")
    private Curso curso;

    public Matricula() {
    }

    public Matricula(int codigo) {
        this.codigo = codigo;
    }

    public Matricula(int codigo, float coste, Date fecha, boolean pagado) {
        this.codigo = codigo;
        this.coste = coste;
        this.fecha = fecha;
        this.pagado = pagado;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public float getCoste() {
        return coste;
    }

    public void setCoste(float coste) {
        this.coste = coste;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
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
        Matricula other = (Matricula) obj;
        if (codigo != other.codigo)
            return false;
        return true;
    }

    
    
}
