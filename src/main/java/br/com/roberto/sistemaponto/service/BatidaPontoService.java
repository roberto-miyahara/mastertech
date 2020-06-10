package br.com.roberto.sistemaponto.service;

import br.com.roberto.sistemaponto.dto.BatidaPontoControleHorasDto;
import br.com.roberto.sistemaponto.dto.BatidaPontoDto;
import br.com.roberto.sistemaponto.persistence.model.BatidaPonto;
import br.com.roberto.sistemaponto.persistence.model.Usuario;
import br.com.roberto.sistemaponto.persistence.repository.BatidaPontoRepository;
import br.com.roberto.sistemaponto.persistence.repository.UsuarioRepository;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
public class BatidaPontoService {

    private final BatidaPontoRepository batidaPontoRepository;
    private final UsuarioRepository usuarioRepository;

    public BatidaPontoService(BatidaPontoRepository batidaPontoRepository, UsuarioRepository usuarioRepository) {
        this.batidaPontoRepository = batidaPontoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<BatidaPonto> obtemPontos() {
        return (List<BatidaPonto>) batidaPontoRepository.findAll();
    }

    public List<BatidaPonto> obtemPontoPorUsuario(Long id) {
        return batidaPontoRepository.findByUser(id);
    }

    public BatidaPontoControleHorasDto obtemPontoPorUsuario(List<BatidaPontoDto> batidasPonto) {
        BatidaPontoControleHorasDto ch = new BatidaPontoControleHorasDto();
        ch.setBatidaPontoList(batidasPonto);

        Seconds seconds = Seconds.seconds(0);
        for (int i = 0; i < batidasPonto.size(); i++) {
            BatidaPontoDto batidaAtual = batidasPonto.get(i);
            if (batidaAtual.isEntrada()) {
                int nextIndex = i + 1;
                boolean hasNextItem = nextIndex < batidasPonto.size();

                DateTime entrada = batidaAtual.getConvertedDate(batidaAtual.getDataBatida());
                DateTime saida = new DateTime();

                if (hasNextItem) {
                    BatidaPontoDto batidaSaida = batidasPonto.get(nextIndex);
                    saida = batidaSaida.getConvertedDate(batidaSaida.getDataBatida());
                }
                seconds = seconds.plus(Seconds.secondsBetween(entrada, saida));
            }
        }
        ch.setHorasTrabalhadas(ch.converterSegundosEmPeriodo(seconds));

        return ch;
    }

    public BatidaPonto adicionaPonto(BatidaPonto batidaPonto) {
        Optional<Usuario> usuario = usuarioRepository.findById(batidaPonto.getIdUsuario());
        if (usuario.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado na base de dados.");
        }
        List<BatidaPonto> byUserAndDate = batidaPontoRepository.findByUser(batidaPonto.getIdUsuario());
        if (byUserAndDate.size() % 2 == 0) {
            batidaPonto.setEntrada(true);
        }
        return batidaPontoRepository.save(batidaPonto);
    }

    public BatidaPonto deletaPonto(Long id) {
        Optional<BatidaPonto> batida = batidaPontoRepository.findById(id);
        if (batida.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Marcação de ponto não encontrada na base de dados.");
        }
        batidaPontoRepository.deleteById(id);
        return batida.get();
    }
}
