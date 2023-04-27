package com.mafv.academy.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_usuario")
public class Usuario {
    
    @Id
    @GeneratedValue
    private int codigo;

    private String username;
    private String password;

    private String nombre;
    private String apellidos;

    private Long telefono;
    private String sexo;
    
    @Column(unique=true, length=9)
    private String dni;
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaNacimiento;

    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "user_permission", joinColumns = @JoinColumn(name = "Usuario_codigo"), inverseJoinColumns = @JoinColumn(name = "Permiso_codigo"))
    private List<Permiso> permissions;

    public Usuario() {
    }

    public Usuario(int codigo, String username, String password) {
        this.codigo = codigo;
        this.username = username;
        this.password = password;
    }

    public Usuario(int codigo, String username, String password, String nombre, String apellidos, Long telefono,
            String sexo, String dni, String email, Date fechaNacimiento) {
        this.codigo = codigo;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.sexo = sexo;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
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
        Usuario other = (Usuario) obj;
        if (codigo != other.codigo)
            return false;
        return true;
    }

    public List<Permiso> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permiso> permissions) {
        this.permissions = permissions;
    }
    
}
