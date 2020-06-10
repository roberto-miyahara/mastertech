package br.com.roberto.sistemaponto.service;

import br.com.roberto.sistemaponto.persistence.model.Usuario;
import br.com.roberto.sistemaponto.persistence.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> obtemUsuarios() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    public Usuario getUsuario(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado na base de dados.");
        }
        return usuario.get();
    }

    public Usuario adicionaUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario deletaUsuario(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado na base de dados.");
        }
        usuarioRepository.deleteById(id);
        return usuario.get();
    }

    public Usuario atualizaUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}
