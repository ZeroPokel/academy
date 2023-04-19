package com.mafv.academy.models;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class EstudianteModuloKey implements Serializable{
    
    private int estudiante_codigo;
    private int modulo_codigo;
    
    public EstudianteModuloKey() {
    }

    public EstudianteModuloKey(int estudiante_codigo, int modulo_codigo) {
        this.estudiante_codigo = estudiante_codigo;
        this.modulo_codigo = modulo_codigo;
    }

    public int getEstudiante_codigo() {
        return estudiante_codigo;
    }

    public void setEstudiante_codigo(int estudiante_codigo) {
        this.estudiante_codigo = estudiante_codigo;
    }

    public int getModulo_codigo() {
        return modulo_codigo;
    }

    public void setModulo_codigo(int modulo_codigo) {
        this.modulo_codigo = modulo_codigo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + estudiante_codigo;
        result = prime * result + modulo_codigo;
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
        EstudianteModuloKey other = (EstudianteModuloKey) obj;
        if (estudiante_codigo != other.estudiante_codigo)
            return false;
        if (modulo_codigo != other.modulo_codigo)
            return false;
        return true;
    }

    

    
}
