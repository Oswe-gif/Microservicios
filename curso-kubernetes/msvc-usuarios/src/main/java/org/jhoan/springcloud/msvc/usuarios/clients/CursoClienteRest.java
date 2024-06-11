package org.jhoan.springcloud.msvc.usuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "msvc-cursos", url = "localhost:8002") para de manera local
//@FeignClient(name = "msvc-cursos", url = "host.docker.internal:8002") para que un microservicio en docker use servicios externos de la maquina host (mi maquina)
@FeignClient(name = "msvc-cursos", url = "${msvc.cursos.url}") // y el name y la URL lo mejor es que coincidan con el nombre del contenedor con --name
public interface CursoClienteRest {

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    void eliminarCursoUsuarioPorId(@PathVariable Long id);
}
