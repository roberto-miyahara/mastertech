package br.com.roberto.sistemaponto.controller;

import br.com.roberto.sistemaponto.dto.BatidaPontoControleHorasDto;
import br.com.roberto.sistemaponto.dto.BatidaPontoDto;
import br.com.roberto.sistemaponto.dto.BatidaPontoInfoDto;
import br.com.roberto.sistemaponto.persistence.model.BatidaPonto;
import br.com.roberto.sistemaponto.service.BatidaPontoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.joda.time.DateTime;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/punchClock")
@Tag(name = "Ponto", description = "A API do Controle de Ponto")
public class BatidaPontoController {

    private final ModelMapper modelMapper;
    private final BatidaPontoService batidaPontoService;

    public BatidaPontoController(BatidaPontoService batidaPontoService, ModelMapper modelMapper) {
        this.batidaPontoService = batidaPontoService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Busca Ponto", description = "Busca todos os registros de ponto realizados.", tags = {"ponto"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = BatidaPontoDto.class))))})
    @GetMapping
    public List<BatidaPontoDto> findAll() {
        List<BatidaPonto> pontos = batidaPontoService.obtemPontos();
        return pontos.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Operation(summary = "Busca Ponto por Usuário", description = "Busca todos os registros de ponto por usuário.", tags = {"ponto"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = BatidaPontoControleHorasDto.class))))})
    @GetMapping("/userId/{id}")
    public BatidaPontoControleHorasDto findAllByUser(@Parameter(description = "ID do usuário. Deve ser informado.", required = true)
                                                     @PathVariable Long id) {
        List<BatidaPonto> pontos = batidaPontoService.obtemPontoPorUsuario(id);
        List<BatidaPontoDto> pontoDtos = pontos.stream().map(this::convertToDto).collect(Collectors.toList());
        return batidaPontoService.obtemPontoPorUsuario(pontoDtos);
    }

    @Operation(summary = "Adiciona Novo Registro", description = "Adiciona novo registro de ponto por usuário", tags = {"ponto"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registro de ponto adicionado com sucesso", content = @Content(schema = @Schema(implementation = BatidaPontoDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuário para o registro não encontrado na base de dados.")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BatidaPontoDto create(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Registro de ponto. Não pode ser nulo ou vazio.",
            content = @Content(schema = @Schema(implementation = BatidaPontoInfoDto.class)), required = true)
                                 @RequestBody BatidaPontoInfoDto batidaPonto) {
        return convertToDto(batidaPontoService.adicionaPonto(convertToSaveEntity(batidaPonto)));
    }

    @Operation(summary = "Deleta Registro de Ponto por ID", description = "Remove um registro de ponto da base de dados", tags = {"ponto"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro com o ID removido da base"),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado na base de dados.")})
    @DeleteMapping("/{id}")
    public String delete(@Parameter(description = "ID do registro de ponto. Deve ser informado.", required = true)
                         @PathVariable Long id) {
        BatidaPonto batidaPonto = batidaPontoService.deletaPonto(id);
        return "Registro de ponto com o ID " + batidaPonto.getId() + " removido da base!";
    }

    private BatidaPontoDto convertToDto(BatidaPonto batidaPonto) {
        BatidaPontoDto batidaPontoDto = modelMapper.map(batidaPonto, BatidaPontoDto.class);
        batidaPontoDto.setConvertedDate(batidaPonto.getDataBatida());
        return batidaPontoDto;
    }

    private BatidaPonto convertToSaveEntity(BatidaPontoInfoDto batidaPontoInfoDto) {
        BatidaPonto batidaPonto = modelMapper.map(batidaPontoInfoDto, BatidaPonto.class);
        batidaPonto.setDataBatida(new DateTime());
        return batidaPonto;
    }
}
