package pl.diamondleague.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.diamondleague.app.domain.GameTeamAsserts.*;
import static pl.diamondleague.app.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.diamondleague.app.IntegrationTest;
import pl.diamondleague.app.domain.Game;
import pl.diamondleague.app.domain.GameTeam;
import pl.diamondleague.app.domain.Player;
import pl.diamondleague.app.repository.GameTeamRepository;
import pl.diamondleague.app.service.dto.GameTeamDTO;
import pl.diamondleague.app.service.mapper.GameTeamMapper;

/**
 * Integration tests for the {@link GameTeamResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GameTeamResourceIT {

    private static final Integer DEFAULT_GOALS = 0;
    private static final Integer UPDATED_GOALS = 1;

    private static final String ENTITY_API_URL = "/api/game-teams";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GameTeamRepository gameTeamRepository;

    @Autowired
    private GameTeamMapper gameTeamMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameTeamMockMvc;

    private GameTeam gameTeam;

    private GameTeam insertedGameTeam;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameTeam createEntity(EntityManager em) {
        GameTeam gameTeam = new GameTeam().goals(DEFAULT_GOALS);
        // Add required entity
        Player player;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            player = PlayerResourceIT.createEntity();
            em.persist(player);
            em.flush();
        } else {
            player = TestUtil.findAll(em, Player.class).get(0);
        }
        gameTeam.setCaptain(player);
        // Add required entity
        Game game;
        if (TestUtil.findAll(em, Game.class).isEmpty()) {
            game = GameResourceIT.createEntity(em);
            em.persist(game);
            em.flush();
        } else {
            game = TestUtil.findAll(em, Game.class).get(0);
        }
        gameTeam.setGame(game);
        return gameTeam;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameTeam createUpdatedEntity(EntityManager em) {
        GameTeam updatedGameTeam = new GameTeam().goals(UPDATED_GOALS);
        // Add required entity
        Player player;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            player = PlayerResourceIT.createUpdatedEntity();
            em.persist(player);
            em.flush();
        } else {
            player = TestUtil.findAll(em, Player.class).get(0);
        }
        updatedGameTeam.setCaptain(player);
        // Add required entity
        Game game;
        if (TestUtil.findAll(em, Game.class).isEmpty()) {
            game = GameResourceIT.createUpdatedEntity(em);
            em.persist(game);
            em.flush();
        } else {
            game = TestUtil.findAll(em, Game.class).get(0);
        }
        updatedGameTeam.setGame(game);
        return updatedGameTeam;
    }

    @BeforeEach
    public void initTest() {
        gameTeam = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedGameTeam != null) {
            gameTeamRepository.delete(insertedGameTeam);
            insertedGameTeam = null;
        }
    }

    @Test
    @Transactional
    void createGameTeam() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the GameTeam
        GameTeamDTO gameTeamDTO = gameTeamMapper.toDto(gameTeam);
        var returnedGameTeamDTO = om.readValue(
            restGameTeamMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gameTeamDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GameTeamDTO.class
        );

        // Validate the GameTeam in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedGameTeam = gameTeamMapper.toEntity(returnedGameTeamDTO);
        assertGameTeamUpdatableFieldsEquals(returnedGameTeam, getPersistedGameTeam(returnedGameTeam));

        insertedGameTeam = returnedGameTeam;
    }

    @Test
    @Transactional
    void createGameTeamWithExistingId() throws Exception {
        // Create the GameTeam with an existing ID
        gameTeam.setId(1L);
        GameTeamDTO gameTeamDTO = gameTeamMapper.toDto(gameTeam);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameTeamMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gameTeamDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GameTeam in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGameTeams() throws Exception {
        // Initialize the database
        insertedGameTeam = gameTeamRepository.saveAndFlush(gameTeam);

        // Get all the gameTeamList
        restGameTeamMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameTeam.getId().intValue())))
            .andExpect(jsonPath("$.[*].goals").value(hasItem(DEFAULT_GOALS)));
    }

    @Test
    @Transactional
    void getGameTeam() throws Exception {
        // Initialize the database
        insertedGameTeam = gameTeamRepository.saveAndFlush(gameTeam);

        // Get the gameTeam
        restGameTeamMockMvc
            .perform(get(ENTITY_API_URL_ID, gameTeam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gameTeam.getId().intValue()))
            .andExpect(jsonPath("$.goals").value(DEFAULT_GOALS));
    }

    @Test
    @Transactional
    void getNonExistingGameTeam() throws Exception {
        // Get the gameTeam
        restGameTeamMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGameTeam() throws Exception {
        // Initialize the database
        insertedGameTeam = gameTeamRepository.saveAndFlush(gameTeam);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gameTeam
        GameTeam updatedGameTeam = gameTeamRepository.findById(gameTeam.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGameTeam are not directly saved in db
        em.detach(updatedGameTeam);
        updatedGameTeam.goals(UPDATED_GOALS);
        GameTeamDTO gameTeamDTO = gameTeamMapper.toDto(updatedGameTeam);

        restGameTeamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameTeamDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(gameTeamDTO))
            )
            .andExpect(status().isOk());

        // Validate the GameTeam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGameTeamToMatchAllProperties(updatedGameTeam);
    }

    @Test
    @Transactional
    void putNonExistingGameTeam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gameTeam.setId(longCount.incrementAndGet());

        // Create the GameTeam
        GameTeamDTO gameTeamDTO = gameTeamMapper.toDto(gameTeam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameTeamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameTeamDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(gameTeamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameTeam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGameTeam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gameTeam.setId(longCount.incrementAndGet());

        // Create the GameTeam
        GameTeamDTO gameTeamDTO = gameTeamMapper.toDto(gameTeam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameTeamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(gameTeamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameTeam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGameTeam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gameTeam.setId(longCount.incrementAndGet());

        // Create the GameTeam
        GameTeamDTO gameTeamDTO = gameTeamMapper.toDto(gameTeam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameTeamMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gameTeamDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameTeam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGameTeamWithPatch() throws Exception {
        // Initialize the database
        insertedGameTeam = gameTeamRepository.saveAndFlush(gameTeam);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gameTeam using partial update
        GameTeam partialUpdatedGameTeam = new GameTeam();
        partialUpdatedGameTeam.setId(gameTeam.getId());

        restGameTeamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameTeam.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGameTeam))
            )
            .andExpect(status().isOk());

        // Validate the GameTeam in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGameTeamUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedGameTeam, gameTeam), getPersistedGameTeam(gameTeam));
    }

    @Test
    @Transactional
    void fullUpdateGameTeamWithPatch() throws Exception {
        // Initialize the database
        insertedGameTeam = gameTeamRepository.saveAndFlush(gameTeam);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gameTeam using partial update
        GameTeam partialUpdatedGameTeam = new GameTeam();
        partialUpdatedGameTeam.setId(gameTeam.getId());

        partialUpdatedGameTeam.goals(UPDATED_GOALS);

        restGameTeamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameTeam.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGameTeam))
            )
            .andExpect(status().isOk());

        // Validate the GameTeam in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGameTeamUpdatableFieldsEquals(partialUpdatedGameTeam, getPersistedGameTeam(partialUpdatedGameTeam));
    }

    @Test
    @Transactional
    void patchNonExistingGameTeam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gameTeam.setId(longCount.incrementAndGet());

        // Create the GameTeam
        GameTeamDTO gameTeamDTO = gameTeamMapper.toDto(gameTeam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameTeamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gameTeamDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(gameTeamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameTeam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGameTeam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gameTeam.setId(longCount.incrementAndGet());

        // Create the GameTeam
        GameTeamDTO gameTeamDTO = gameTeamMapper.toDto(gameTeam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameTeamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(gameTeamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameTeam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGameTeam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gameTeam.setId(longCount.incrementAndGet());

        // Create the GameTeam
        GameTeamDTO gameTeamDTO = gameTeamMapper.toDto(gameTeam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameTeamMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(gameTeamDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameTeam in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGameTeam() throws Exception {
        // Initialize the database
        insertedGameTeam = gameTeamRepository.saveAndFlush(gameTeam);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the gameTeam
        restGameTeamMockMvc
            .perform(delete(ENTITY_API_URL_ID, gameTeam.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return gameTeamRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected GameTeam getPersistedGameTeam(GameTeam gameTeam) {
        return gameTeamRepository.findById(gameTeam.getId()).orElseThrow();
    }

    protected void assertPersistedGameTeamToMatchAllProperties(GameTeam expectedGameTeam) {
        assertGameTeamAllPropertiesEquals(expectedGameTeam, getPersistedGameTeam(expectedGameTeam));
    }

    protected void assertPersistedGameTeamToMatchUpdatableProperties(GameTeam expectedGameTeam) {
        assertGameTeamAllUpdatablePropertiesEquals(expectedGameTeam, getPersistedGameTeam(expectedGameTeam));
    }
}
