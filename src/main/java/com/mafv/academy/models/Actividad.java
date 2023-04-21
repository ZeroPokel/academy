package com.mafv.academy.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Actividad {
    
    @Id
    @GeneratedValue
    private int codigo;

    private String titulo;
    private String descripcion;
    private Date fechaInicio;
    private Date fechaFin;
    private float calificacion;


}
