package br.com.roberto.sistemaponto;

import com.jayway.jsonpath.JsonPath;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = SistemaPontoApplication.class)
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(APPLICATION_JSON.getType(), APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    private static final String USER_NAME = "Roberto";
    private static final String USER_NAME_EDITED = "Roberto Miyahara";
    private static final String USER_EMAIL = "betzmiyahara@gmail.com";
    private static final String USER_CPF = "444.333.888-33";
    private static final String INITIAL_USER_NAME = "[I] Roberto Miyahara";
    private static final String INITIAL_USER_EMAIL = "[I] betzmiyahara@gmail.com";
    private static final String INITIAL_USER_CPF = "[I] 444.333.888-33";
    private int initialInsertId = 0;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void adicionaUsuarioInicial() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/users")
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"nome\":\"" + INITIAL_USER_NAME + "\",\"cpf\":\"" + INITIAL_USER_CPF + "\",\"email\":\"" + INITIAL_USER_EMAIL + "\"}"))
                .andReturn();
        initialInsertId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }

    @Test
    public void cadastraUsuarioEValidaQueORetornoPossuiOsMesmosDadosDaInsercao() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"nome\":\"" + USER_NAME_EDITED + "\",\"cpf\":\"" + USER_CPF + "\",\"email\":\"" + USER_EMAIL + "\"}"))
                .andDo(print())
                .andExpect(jsonPath("$.nome", is(USER_NAME_EDITED)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void editaUsuarioEValidaQueORetornoPossuiOsMesmosDadosDaEdicao() throws Exception {
        mockMvc.perform(put("/api/users/" + initialInsertId)
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"id\": \"" + initialInsertId + "\", \"nome\":\"" + USER_NAME + "\",\"cpf\":\"" + USER_CPF + "\",\"email\":\"" + USER_EMAIL + "\"}"))
                .andDo(print())
                .andExpect(jsonPath("$.nome", is(USER_NAME)))
                .andExpect(jsonPath("$.cpf", is(USER_CPF)))
                .andExpect(jsonPath("$.email", is(USER_EMAIL)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void editaUsuarioInexistente() throws Exception {
        mockMvc.perform(put("/api/users/" + 123456)
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"nome\":\"" + USER_NAME + "\",\"cpf\":\"" + USER_CPF + "\",\"email\":\"" + USER_EMAIL + "\"}"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void obtemTodosOsUsuarios() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andDo(print())
                .andExpect(jsonPath("$.[0].nome", is(INITIAL_USER_NAME)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void validaUsuarioCadastradoInicialmente() throws Exception {
        mockMvc.perform(get("/api/users/" + initialInsertId))
                .andDo(print())
                .andExpect(jsonPath("$.id", is(initialInsertId)))
                .andExpect(jsonPath("$.nome", is(INITIAL_USER_NAME)))
                .andExpect(jsonPath("$.cpf", is(INITIAL_USER_CPF)))
                .andExpect(jsonPath("$.email", is(INITIAL_USER_EMAIL)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void consultaUsuarioInexistente() throws Exception {
        mockMvc.perform(get("/api/users/" + 123456))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void deletaUsuarioInexistente() throws Exception {
        mockMvc.perform(delete("/api/users/" + 123456))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @After
    public void limpar() throws Exception {
        mockMvc.perform(delete("/api/users/" + initialInsertId))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }
}
