package org.jhoan.springcloud.msvc.usuarios.repositories;

import org.jhoan.springcloud.msvc.usuarios.models.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository <Usuario, Long>{
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}
