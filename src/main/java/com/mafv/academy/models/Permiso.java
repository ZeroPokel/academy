package com.mafv.academy.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Permiso {
    
    @Id
    @GeneratedValue
    private int codigo;
    private String nombre;

    @ManyToMany(mappedBy = "permissions")
    private List<Usuario> users;

    public Permiso() {
    }

    public Permiso(int codigo, String nombre) {
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
    
}