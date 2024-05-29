package org.jhoan.springcloud.msvc.usuarios.controllers;

import jakarta.validation.Valid;
import org.jhoan.springcloud.msvc.usuarios.models.entity.Usuario;
import org.jhoan.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listar(){
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id){
        Optional<Usuario> usuarioOptional = usuarioService.porId(id);
        if (usuarioOptional.isPresent()){
            return ResponseEntity.ok().body(usuarioOptional.get());// el ok es para indicar que salio bien, por ende devuelve su codigo de petición
        }
        return ResponseEntity.notFound().build();//no encontró el recurso, por ende, el notFound() devuelve su codigo de estado.
    }
    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> ids){
        return ResponseEntity.ok(usuarioService.listarPorIds(ids));
    }

    /*@PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario crear(@RequestBody Usuario usuario){
        return usuarioService.guardar(usuario);
    }*/
    //otra opción.
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result){
        if(result.hasErrors()){
            return validar(result);
        }
        if(!usuario.getEmail().isEmpty() && usuarioService.existePorEmail(usuario.getEmail())){
            return ResponseEntity.badRequest().body(Collections.singletonMap("Mensaje","Ya existe el correo electronico"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result,@PathVariable Long id){
        if(result.hasErrors()){
            return validar(result);
        }
        Optional<Usuario> o= usuarioService.porId(id);
        if (o.isPresent()){
            Usuario usuarioDB = o.get();
            if(!usuario.getEmail().isEmpty() && !usuario.getEmail().equalsIgnoreCase(usuarioDB.getEmail()) && usuarioService.existePorEmail(usuario.getEmail())){
                return ResponseEntity.badRequest().body(Collections.singletonMap("Mensaje","Ya existe el correo electronico"));
            }
            usuarioDB.setEmail(usuario.getEmail());
            usuarioDB.setNombre(usuario.getNombre());
            usuarioDB.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuarioDB));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional <Usuario> o = usuarioService.porId(id);
        if (o.isPresent()){
            usuarioService.eliminar(o.get().getId());
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err ->{
            errores.put(err.getField(), "El campo "+err.getField()+ " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }

}
