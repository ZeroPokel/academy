package com.mafv.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mafv.academy.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
    
    Usuario findByUsername(String username);
}
