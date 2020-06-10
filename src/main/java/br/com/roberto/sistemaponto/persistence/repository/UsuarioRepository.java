package br.com.roberto.sistemaponto.persistence.repository;

import br.com.roberto.sistemaponto.persistence.model.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
}
