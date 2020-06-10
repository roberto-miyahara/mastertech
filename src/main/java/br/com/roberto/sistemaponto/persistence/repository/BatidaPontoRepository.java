package br.com.roberto.sistemaponto.persistence.repository;

import br.com.roberto.sistemaponto.persistence.model.BatidaPonto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BatidaPontoRepository extends CrudRepository<BatidaPonto, Long> {

    @Query("SELECT B FROM BatidaPonto B WHERE B.idUsuario = :userId AND B.dataBatida >= CURRENT_DATE")
    List<BatidaPonto> findByUser(@Param("userId") long userId);
}