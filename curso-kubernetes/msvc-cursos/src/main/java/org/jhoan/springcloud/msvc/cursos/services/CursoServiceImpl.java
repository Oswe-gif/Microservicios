package org.jhoan.springcloud.msvc.cursos.services;


import org.jhoan.springcloud.msvc.cursos.clients.UsuarioClientRest;
import org.jhoan.springcloud.msvc.cursos.models.Usuario;
import org.jhoan.springcloud.msvc.cursos.models.entity.Curso;
import org.jhoan.springcloud.msvc.cursos.models.entity.CursoUsuario;
import org.jhoan.springcloud.msvc.cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CursoServiceImpl implements CursoService {
    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioClientRest clientRest;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) cursoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long id) {
        return cursoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porIdConUsuarios(Long id) {
        Optional<Curso> o = cursoRepository.findById(id);
        if (o.isPresent()){
            Curso curso = o.get();
            if (!curso.getCursoUsuarios().isEmpty()){
                List<Long> ids = curso.getCursoUsuarios().stream().map(cursoUsuario -> cursoUsuario.getUsuarioId()).toList();
                List<Usuario> usuarios = clientRest.obtenerAlumnosPorCurso(ids);
                curso.setUsuarios(usuarios);
            }
            return Optional.of(curso);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        cursoRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void eliminarCursoUsuarioPorId(Long id) {
        cursoRepository.eliminarCursoUsuario(id);
    }

    @Override
    @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> o = cursoRepository.findById(cursoId);
        if(o.isPresent()){
            Usuario usuarioMsvc = clientRest.detalle(usuario.getId());

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> o = cursoRepository.findById(cursoId);
        if(o.isPresent()){
            Usuario usuarioNuevoMsvc = clientRest.crear(usuario);

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);
            return Optional.of(usuarioNuevoMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> o = cursoRepository.findById(cursoId);
        if(o.isPresent()){
            Usuario usuarioNuevoMsvc = clientRest.detalle(usuario.getId());

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());

            curso.removeCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);
            return Optional.of(usuarioNuevoMsvc);
        }
        return Optional.empty();
    }
}
