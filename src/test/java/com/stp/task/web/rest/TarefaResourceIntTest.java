package com.stp.task.web.rest;

import com.stp.task.TaskstpApp;

import com.stp.task.domain.Tarefa;
import com.stp.task.repository.TarefaRepository;
import com.stp.task.service.TarefaService;
import com.stp.task.service.dto.TarefaDTO;
import com.stp.task.service.mapper.TarefaMapper;
import com.stp.task.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.stp.task.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.stp.task.domain.enumeration.Status;
/**
 * Test class for the TarefaResource REST controller.
 *
 * @see TarefaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TaskstpApp.class)
public class TarefaResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA_CRIACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_CRIACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_RESPOSTA = "AAAAAAAAAA";
    private static final String UPDATED_RESPOSTA = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.ABERTA;
    private static final Status UPDATED_STATUS = Status.FECHADA;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private TarefaMapper tarefaMapper;

    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTarefaMockMvc;

    private Tarefa tarefa;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TarefaResource tarefaResource = new TarefaResource(tarefaService);
        this.restTarefaMockMvc = MockMvcBuilders.standaloneSetup(tarefaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarefa createEntity(EntityManager em) {
        Tarefa tarefa = new Tarefa()
            .descricao(DEFAULT_DESCRICAO)
            .dataCriacao(DEFAULT_DATA_CRIACAO)
            .resposta(DEFAULT_RESPOSTA)
            .status(DEFAULT_STATUS);
        return tarefa;
    }

    @Before
    public void initTest() {
        tarefa = createEntity(em);
    }

    @Test
    @Transactional
    public void createTarefa() throws Exception {
        int databaseSizeBeforeCreate = tarefaRepository.findAll().size();

        // Create the Tarefa
        TarefaDTO tarefaDTO = tarefaMapper.tarefaToTarefaDTO(tarefa);
        restTarefaMockMvc.perform(post("/api/tarefas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tarefaDTO)))
            .andExpect(status().isCreated());

        // Validate the Tarefa in the database
        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeCreate + 1);
        Tarefa testTarefa = tarefaList.get(tarefaList.size() - 1);
        assertThat(testTarefa.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testTarefa.getDataCriacao()).isEqualTo(DEFAULT_DATA_CRIACAO);
        assertThat(testTarefa.getResposta()).isEqualTo(DEFAULT_RESPOSTA);
        assertThat(testTarefa.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createTarefaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tarefaRepository.findAll().size();

        // Create the Tarefa with an existing ID
        tarefa.setId(1L);
        TarefaDTO tarefaDTO = tarefaMapper.tarefaToTarefaDTO(tarefa);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTarefaMockMvc.perform(post("/api/tarefas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tarefaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tarefaRepository.findAll().size();
        // set the field null
        tarefa.setDescricao(null);

        // Create the Tarefa, which fails.
        TarefaDTO tarefaDTO = tarefaMapper.tarefaToTarefaDTO(tarefa);

        restTarefaMockMvc.perform(post("/api/tarefas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tarefaDTO)))
            .andExpect(status().isBadRequest());

        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataCriacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tarefaRepository.findAll().size();
        // set the field null
        tarefa.setDataCriacao(null);

        // Create the Tarefa, which fails.
        TarefaDTO tarefaDTO = tarefaMapper.tarefaToTarefaDTO(tarefa);

        restTarefaMockMvc.perform(post("/api/tarefas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tarefaDTO)))
            .andExpect(status().isBadRequest());

        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTarefas() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get all the tarefaList
        restTarefaMockMvc.perform(get("/api/tarefas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tarefa.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].dataCriacao").value(hasItem(sameInstant(DEFAULT_DATA_CRIACAO))))
            .andExpect(jsonPath("$.[*].resposta").value(hasItem(DEFAULT_RESPOSTA.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getTarefa() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);

        // Get the tarefa
        restTarefaMockMvc.perform(get("/api/tarefas/{id}", tarefa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tarefa.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.dataCriacao").value(sameInstant(DEFAULT_DATA_CRIACAO)))
            .andExpect(jsonPath("$.resposta").value(DEFAULT_RESPOSTA.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTarefa() throws Exception {
        // Get the tarefa
        restTarefaMockMvc.perform(get("/api/tarefas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTarefa() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);
        int databaseSizeBeforeUpdate = tarefaRepository.findAll().size();

        // Update the tarefa
        Tarefa updatedTarefa = tarefaRepository.findOne(tarefa.getId());
        updatedTarefa
            .descricao(UPDATED_DESCRICAO)
            .dataCriacao(UPDATED_DATA_CRIACAO)
            .resposta(UPDATED_RESPOSTA)
            .status(UPDATED_STATUS);
        TarefaDTO tarefaDTO = tarefaMapper.tarefaToTarefaDTO(updatedTarefa);

        restTarefaMockMvc.perform(put("/api/tarefas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tarefaDTO)))
            .andExpect(status().isOk());

        // Validate the Tarefa in the database
        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeUpdate);
        Tarefa testTarefa = tarefaList.get(tarefaList.size() - 1);
        assertThat(testTarefa.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testTarefa.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
        assertThat(testTarefa.getResposta()).isEqualTo(UPDATED_RESPOSTA);
        assertThat(testTarefa.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingTarefa() throws Exception {
        int databaseSizeBeforeUpdate = tarefaRepository.findAll().size();

        // Create the Tarefa
        TarefaDTO tarefaDTO = tarefaMapper.tarefaToTarefaDTO(tarefa);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTarefaMockMvc.perform(put("/api/tarefas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tarefaDTO)))
            .andExpect(status().isCreated());

        // Validate the Tarefa in the database
        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTarefa() throws Exception {
        // Initialize the database
        tarefaRepository.saveAndFlush(tarefa);
        int databaseSizeBeforeDelete = tarefaRepository.findAll().size();

        // Get the tarefa
        restTarefaMockMvc.perform(delete("/api/tarefas/{id}", tarefa.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tarefa> tarefaList = tarefaRepository.findAll();
        assertThat(tarefaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tarefa.class);
    }
}
