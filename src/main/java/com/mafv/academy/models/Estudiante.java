package com.mafv.academy.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Estudiante {
    
    @Id
    @GeneratedValue
    private int codigo;

    private String nombre;
    private String apellidos;
    
    @Column(unique=true, length=9)
    private String dni;
    
    private String email;

    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date fechaNacimiento;

    @ManyToOne
    @JoinColumn(name="curso", nullable = false)
    private Curso curso;

    @OneToMany(mappedBy = "estudiante")
    private List<Incidencia> incidencias;

    @OneToMany(mappedBy = "estudiante")
    private List<Matricula> matriculas;

    public Estudiante() {
    }

    public Estudiante(int codigo) {
        this.codigo = codigo;
    }

    public Estudiante(int codigo, String nombre, String apellidos, String dni, String email, Date fechaNacimiento) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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
        Estudiante other = (Estudiante) obj;
        if (codigo != other.codigo)
            return false;
        return true;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    
}
