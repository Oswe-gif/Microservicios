package org.jhoan.springcloud.msvc.cursos.clients;

import org.jhoan.springcloud.msvc.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//@FeignClient(name = "msvc-usuarios", url = "localhost:8081") para de manera local
//@FeignClient(name = "msvc-usuarios", url = "host.docker.internal:8081") para que un microservicio en docker use servicios externos de la maquina host (mi maquina)

@FeignClient(name = "msvc-usuarios", url = "${msvc.usuarios.url}")
public interface UsuarioClientRest {
    @GetMapping("/{id}")
    Usuario detalle(@PathVariable Long id);
    @PostMapping("/")
    Usuario crear(@RequestBody Usuario usuario);
    @GetMapping("/usuarios-por-curso")
    public List<Usuario> obtenerAlumnosPorCurso(@RequestParam Iterable<Long> ids);
}
