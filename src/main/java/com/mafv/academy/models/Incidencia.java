package com.mafv.academy.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Incidencia {
    
    @Id
    @GeneratedValue
    private int codigo;

    private String descripcion;
    private String tipo;
    private Date fecha;
    private String sancion;
    private String lugar;

    @ManyToOne
    @JoinColumn(name = "alumno", nullable = false)
    private Estudiante alumno;

    @ManyToOne
    @JoinColumn(name = "docente", nullable = false)
    private Docente docente;
    
    public Incidencia() {
    }

    public Incidencia(int codigo) {
        this.codigo = codigo;
    }

    public Incidencia(int codigo, String descripcion, String tipo, Date fecha, String sancion, String lugar) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.fecha = fecha;
        this.sancion = sancion;
        this.lugar = lugar;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getSancion() {
        return sancion;
    }

    public void setSancion(String sancion) {
        this.sancion = sancion;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Estudiante getAlumno() {
        return alumno;
    }

    public void setAlumno(Estudiante alumno) {
        this.alumno = alumno;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
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
        Incidencia other = (Incidencia) obj;
        if (codigo != other.codigo)
            return false;
        return true;
    }

    
}
