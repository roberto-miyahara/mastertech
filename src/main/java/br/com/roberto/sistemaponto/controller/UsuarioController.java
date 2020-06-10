package br.com.roberto.sistemaponto.controller;

import br.com.roberto.sistemaponto.dto.UsuarioDto;
import br.com.roberto.sistemaponto.dto.UsuarioInfoDto;
import br.com.roberto.sistemaponto.persistence.model.Usuario;
import br.com.roberto.sistemaponto.service.UsuarioService;
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
@RequestMapping("/api/users")
@Tag(name = "Usuario", description = "A API do Usuario")
public class UsuarioController {

    private final ModelMapper modelMapper;
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService, ModelMapper modelMapper) {
        this.usuarioService = usuarioService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Busca Usuários", description = "Busca todos os usuários cadastrados.", tags = {"usuario"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UsuarioDto.class))))})
    @GetMapping
    public List<UsuarioDto> findAll() {
        List<Usuario> usuarios = usuarioService.obtemUsuarios();
        return usuarios.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Operation(summary = "Encontra Usuário por ID", description = "Retorna um único usuário", tags = {"usuario"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso", content = @Content(schema = @Schema(implementation = UsuarioDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado na base de dados.")})
    @GetMapping("/{id}")
    public UsuarioDto findOne(@Parameter(description = "ID do usuário. Deve ser informado.", required = true)
                              @PathVariable Long id) {
        return convertToDto(usuarioService.getUsuario(id));
    }

    @Operation(summary = "Adiciona Usuário", description = "Retorna um único usuário", tags = {"usuario"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(schema = @Schema(implementation = UsuarioDto.class)))})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDto create(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Usuario para adicionar. Não pode ser nulo ou vazio.",
            content = @Content(schema = @Schema(implementation = UsuarioInfoDto.class)), required = true)
                             @RequestBody UsuarioInfoDto usuario) {
        return convertToDto(usuarioService.adicionaUsuario(convertToSaveEntity(usuario)));
    }

    @Operation(summary = "Deleta Usuário por ID", description = "Remove um usuário da base de dados", tags = {"usuario"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário com o ID removido da base"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado na base de dados.")})
    @DeleteMapping("/{id}")
    public String delete(@Parameter(description = "ID do usuário. Deve ser informado.", required = true)
                         @PathVariable Long id) {
        Usuario usuario = usuarioService.deletaUsuario(id);
        return "Usuario com o ID " + usuario.getId() + " removido da base!";
    }

    @Operation(summary = "Atualiza usuário", description = "Atualiza o usuário correspondente ao ID informado com as informações repassadas", tags = {"usuario"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso", content = @Content(schema = @Schema(implementation = UsuarioDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado na base de dados.")})
    @PutMapping("/{id}")
    public UsuarioDto updateUsuario(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Usuario para editar. Não pode ser nulo ou vazio.",
            content = @Content(schema = @Schema(implementation = UsuarioInfoDto.class)), required = true)
                                    @RequestBody UsuarioInfoDto usuarioInfoDto,
                                    @Parameter(description = "ID do usuário. Deve ser informado.", required = true)
                                    @PathVariable Long id) {
        return convertToDto(usuarioService.atualizaUsuario(convertToUpdateEntity(usuarioInfoDto, id)));
    }

    private UsuarioDto convertToDto(Usuario usuario) {
        UsuarioDto usuarioDto = modelMapper.map(usuario, UsuarioDto.class);
        usuarioDto.setConvertedDate(usuario.getDataCadastro());
        return usuarioDto;
    }

    private Usuario convertToSaveEntity(UsuarioInfoDto usuarioInfoDto) {
        Usuario usuario = modelMapper.map(usuarioInfoDto, Usuario.class);
        usuario.setDataCadastro(new DateTime());
        return usuario;
    }

    private Usuario convertToUpdateEntity(UsuarioInfoDto usuarioInfoDto, Long id) {
        Usuario usuario = modelMapper.map(usuarioInfoDto, Usuario.class);
        Usuario oldUsuario = usuarioService.getUsuario(id);
        oldUsuario.setCpf(usuario.getCpf());
        oldUsuario.setNome(usuario.getNome());
        oldUsuario.setEmail(usuario.getEmail());
        return oldUsuario;
    }
}
