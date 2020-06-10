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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = SistemaPontoApplication.class)
@AutoConfigureMockMvc
public class BatidaPontoControllerTest {

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(APPLICATION_JSON.getType(), APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    private static final String INITIAL_USER_NAME = "[I] Thiago";
    private static final String INITIAL_USER_EMAIL = "[I] thi.carsil@gmail.com";
    private static final String INITIAL_USER_CPF = "[I] 123.456.789-0";
    private int userId = 0;
    private int idEntrada = 0;
    private int idSaida = 0;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void adicionaValoresIniciais() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/users")
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"nome\":\"" + INITIAL_USER_NAME + "\",\"cpf\":\"" + INITIAL_USER_CPF + "\",\"email\":\"" + INITIAL_USER_EMAIL + "\"}"))
                .andReturn();

        userId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        MvcResult pontoEntradaResult = mockMvc.perform(post("/api/punchClock")
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"idUsuario\":\"" + userId + "\"}"))
                .andReturn();

        idEntrada = JsonPath.read(pontoEntradaResult.getResponse().getContentAsString(), "$.id");

        MvcResult pontoSaidaResult = mockMvc.perform(post("/api/punchClock")
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"idUsuario\":\"" + userId + "\"}"))
                .andReturn();

        idSaida = JsonPath.read(pontoSaidaResult.getResponse().getContentAsString(), "$.id");
    }

    @Test
    public void inserePontoDeUsuarioExistente() throws Exception {
        mockMvc.perform(post("/api/punchClock")
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"idUsuario\":\"" + userId + "\"}"))
                .andDo(print())
                .andExpect(jsonPath("$.idUsuario", is(userId)))
                .andExpect(jsonPath("$.entrada", is(true)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void obtemTodosOsPontos() throws Exception {
        mockMvc.perform(get("/api/punchClock"))
                .andDo(print())
                .andExpect(jsonPath("$.[0].idUsuario", is(userId)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void obtemTodosOsPontosDeUsuarioExistente() throws Exception {
        mockMvc.perform(get("/api/punchClock/userId/" + userId))
                .andDo(print())
                .andExpect(jsonPath("$.horasTrabalhadas", containsString("segundo")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void inserePontoDeUsuarioInexistente() throws Exception {
        mockMvc.perform(post("/api/punchClock")
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"idUsuario\":\"" + 123456 + "\"}"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void deletaPontoInexistente() throws Exception {
        mockMvc.perform(delete("/api/punchClock/" + 123456))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @After
    public void limpar() throws Exception {
        mockMvc.perform(delete("/api/punchClock/" + idEntrada))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(delete("/api/punchClock/" + idSaida))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }
}
