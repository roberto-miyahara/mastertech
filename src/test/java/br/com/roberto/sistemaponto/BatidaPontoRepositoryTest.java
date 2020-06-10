package br.com.roberto.sistemaponto;

import br.com.roberto.sistemaponto.persistence.model.BatidaPonto;
import br.com.roberto.sistemaponto.persistence.repository.BatidaPontoRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BatidaPontoRepositoryTest {

    @Autowired
    private BatidaPontoRepository batidaPontoRepositoryTest;

    @Test
    public void bancoVazioRetornaOpcionalVazio() {
        Optional<BatidaPonto> batidaPonto = batidaPontoRepositoryTest.findById(1L);
        assertThat(batidaPonto.isPresent()).isEqualTo(false);
    }

    @Test
    public void bancoComBatidaRetornaOpcionalComValor() {
        BatidaPonto batidaPonto = new BatidaPonto();
        batidaPonto.setEntrada(true);
        batidaPonto.setIdUsuario(1L);

        BatidaPonto batida = batidaPontoRepositoryTest.save(batidaPonto);

        Optional<BatidaPonto> batidaEncontrada = batidaPontoRepositoryTest.findById(batida.getId());
        assertThat(batidaEncontrada.isPresent()).isEqualTo(true);
        if (batidaEncontrada.isPresent()) {
            assertThat(batidaEncontrada.get().isEntrada()).isEqualTo(true);
            assertThat(batidaEncontrada.get().getIdUsuario()).isEqualTo(1L);
        }
    }

    @After
    public void limpar() {
        batidaPontoRepositoryTest.deleteAll();
    }
}
