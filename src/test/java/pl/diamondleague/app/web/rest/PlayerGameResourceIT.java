package pl.diamondleague.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.diamondleague.app.domain.PlayerGameAsserts.*;
import static pl.diamondleague.app.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.diamondleague.app.IntegrationTest;
import pl.diamondleague.app.domain.GameTeam;
import pl.diamondleague.app.domain.Player;
import pl.diamondleague.app.domain.PlayerGame;
import pl.diamondleague.app.repository.PlayerGameRepository;
import pl.diamondleague.app.service.PlayerGameService;
import pl.diamondleague.app.service.dto.PlayerGameDTO;
import pl.diamondleague.app.service.mapper.PlayerGameMapper;

/**
 * Integration tests for the {@link PlayerGameResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PlayerGameResourceIT {

    private static final Integer DEFAULT_GOALS = 0;
    private static final Integer UPDATED_GOALS = 1;

    private static final Integer DEFAULT_ASSISTS = 0;
    private static final Integer UPDATED_ASSISTS = 1;

    private static final Double DEFAULT_ATTACK_SCORE = 1D;
    private static final Double UPDATED_ATTACK_SCORE = 2D;

    private static final Double DEFAULT_DEFENSE_SCORE = 1D;
    private static final Double UPDATED_DEFENSE_SCORE = 2D;

    private static final String ENTITY_API_URL = "/api/player-games";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlayerGameRepository playerGameRepository;

    @Mock
    private PlayerGameRepository playerGameRepositoryMock;

    @Autowired
    private PlayerGameMapper playerGameMapper;

    @Mock
    private PlayerGameService playerGameServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlayerGameMockMvc;

    private PlayerGame playerGame;

    private PlayerGame insertedPlayerGame;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerGame createEntity(EntityManager em) {
        PlayerGame playerGame = new PlayerGame()
            .goals(DEFAULT_GOALS)
            .assists(DEFAULT_ASSISTS)
            .attackScore(DEFAULT_ATTACK_SCORE)
            .defenseScore(DEFAULT_DEFENSE_SCORE);
        // Add required entity
        Player player;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            player = PlayerResourceIT.createEntity(em);
            em.persist(player);
            em.flush();
        } else {
            player = TestUtil.findAll(em, Player.class).get(0);
        }
        playerGame.setPlayer(player);
        // Add required entity
        GameTeam gameTeam;
        if (TestUtil.findAll(em, GameTeam.class).isEmpty()) {
            gameTeam = GameTeamResourceIT.createEntity(em);
            em.persist(gameTeam);
            em.flush();
        } else {
            gameTeam = TestUtil.findAll(em, GameTeam.class).get(0);
        }
        playerGame.setGameTeam(gameTeam);
        return playerGame;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerGame createUpdatedEntity(EntityManager em) {
        PlayerGame updatedPlayerGame = new PlayerGame()
            .goals(UPDATED_GOALS)
            .assists(UPDATED_ASSISTS)
            .attackScore(UPDATED_ATTACK_SCORE)
            .defenseScore(UPDATED_DEFENSE_SCORE);
        // Add required entity
        Player player;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            player = PlayerResourceIT.createUpdatedEntity(em);
            em.persist(player);
            em.flush();
        } else {
            player = TestUtil.findAll(em, Player.class).get(0);
        }
        updatedPlayerGame.setPlayer(player);
        // Add required entity
        GameTeam gameTeam;
        if (TestUtil.findAll(em, GameTeam.class).isEmpty()) {
            gameTeam = GameTeamResourceIT.createUpdatedEntity(em);
            em.persist(gameTeam);
            em.flush();
        } else {
            gameTeam = TestUtil.findAll(em, GameTeam.class).get(0);
        }
        updatedPlayerGame.setGameTeam(gameTeam);
        return updatedPlayerGame;
    }

    @BeforeEach
    public void initTest() {
        playerGame = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPlayerGame != null) {
            playerGameRepository.delete(insertedPlayerGame);
            insertedPlayerGame = null;
        }
    }

    @Test
    @Transactional
    void createPlayerGame() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PlayerGame
        PlayerGameDTO playerGameDTO = playerGameMapper.toDto(playerGame);
        var returnedPlayerGameDTO = om.readValue(
            restPlayerGameMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playerGameDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PlayerGameDTO.class
        );

        // Validate the PlayerGame in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPlayerGame = playerGameMapper.toEntity(returnedPlayerGameDTO);
        assertPlayerGameUpdatableFieldsEquals(returnedPlayerGame, getPersistedPlayerGame(returnedPlayerGame));

        insertedPlayerGame = returnedPlayerGame;
    }

    @Test
    @Transactional
    void createPlayerGameWithExistingId() throws Exception {
        // Create the PlayerGame with an existing ID
        playerGame.setId(1L);
        PlayerGameDTO playerGameDTO = playerGameMapper.toDto(playerGame);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerGameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playerGameDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlayerGame in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGoalsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        playerGame.setGoals(null);

        // Create the PlayerGame, which fails.
        PlayerGameDTO playerGameDTO = playerGameMapper.toDto(playerGame);

        restPlayerGameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playerGameDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAssistsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        playerGame.setAssists(null);

        // Create the PlayerGame, which fails.
        PlayerGameDTO playerGameDTO = playerGameMapper.toDto(playerGame);

        restPlayerGameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playerGameDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlayerGames() throws Exception {
        // Initialize the database
        insertedPlayerGame = playerGameRepository.saveAndFlush(playerGame);

        // Get all the playerGameList
        restPlayerGameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playerGame.getId().intValue())))
            .andExpect(jsonPath("$.[*].goals").value(hasItem(DEFAULT_GOALS)))
            .andExpect(jsonPath("$.[*].assists").value(hasItem(DEFAULT_ASSISTS)))
            .andExpect(jsonPath("$.[*].attackScore").value(hasItem(DEFAULT_ATTACK_SCORE)))
            .andExpect(jsonPath("$.[*].defenseScore").value(hasItem(DEFAULT_DEFENSE_SCORE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlayerGamesWithEagerRelationshipsIsEnabled() throws Exception {
        when(playerGameServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlayerGameMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(playerGameServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlayerGamesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(playerGameServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlayerGameMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(playerGameRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPlayerGame() throws Exception {
        // Initialize the database
        insertedPlayerGame = playerGameRepository.saveAndFlush(playerGame);

        // Get the playerGame
        restPlayerGameMockMvc
            .perform(get(ENTITY_API_URL_ID, playerGame.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(playerGame.getId().intValue()))
            .andExpect(jsonPath("$.goals").value(DEFAULT_GOALS))
            .andExpect(jsonPath("$.assists").value(DEFAULT_ASSISTS))
            .andExpect(jsonPath("$.attackScore").value(DEFAULT_ATTACK_SCORE))
            .andExpect(jsonPath("$.defenseScore").value(DEFAULT_DEFENSE_SCORE));
    }

    @Test
    @Transactional
    void getNonExistingPlayerGame() throws Exception {
        // Get the playerGame
        restPlayerGameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlayerGame() throws Exception {
        // Initialize the database
        insertedPlayerGame = playerGameRepository.saveAndFlush(playerGame);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the playerGame
        PlayerGame updatedPlayerGame = playerGameRepository.findById(playerGame.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPlayerGame are not directly saved in db
        em.detach(updatedPlayerGame);
        updatedPlayerGame
            .goals(UPDATED_GOALS)
            .assists(UPDATED_ASSISTS)
            .attackScore(UPDATED_ATTACK_SCORE)
            .defenseScore(UPDATED_DEFENSE_SCORE);
        PlayerGameDTO playerGameDTO = playerGameMapper.toDto(updatedPlayerGame);

        restPlayerGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playerGameDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(playerGameDTO))
            )
            .andExpect(status().isOk());

        // Validate the PlayerGame in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPlayerGameToMatchAllProperties(updatedPlayerGame);
    }

    @Test
    @Transactional
    void putNonExistingPlayerGame() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playerGame.setId(longCount.incrementAndGet());

        // Create the PlayerGame
        PlayerGameDTO playerGameDTO = playerGameMapper.toDto(playerGame);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playerGameDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(playerGameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerGame in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlayerGame() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playerGame.setId(longCount.incrementAndGet());

        // Create the PlayerGame
        PlayerGameDTO playerGameDTO = playerGameMapper.toDto(playerGame);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(playerGameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerGame in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlayerGame() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playerGame.setId(longCount.incrementAndGet());

        // Create the PlayerGame
        PlayerGameDTO playerGameDTO = playerGameMapper.toDto(playerGame);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerGameMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playerGameDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayerGame in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlayerGameWithPatch() throws Exception {
        // Initialize the database
        insertedPlayerGame = playerGameRepository.saveAndFlush(playerGame);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the playerGame using partial update
        PlayerGame partialUpdatedPlayerGame = new PlayerGame();
        partialUpdatedPlayerGame.setId(playerGame.getId());

        partialUpdatedPlayerGame.goals(UPDATED_GOALS).assists(UPDATED_ASSISTS);

        restPlayerGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayerGame.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlayerGame))
            )
            .andExpect(status().isOk());

        // Validate the PlayerGame in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlayerGameUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPlayerGame, playerGame),
            getPersistedPlayerGame(playerGame)
        );
    }

    @Test
    @Transactional
    void fullUpdatePlayerGameWithPatch() throws Exception {
        // Initialize the database
        insertedPlayerGame = playerGameRepository.saveAndFlush(playerGame);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the playerGame using partial update
        PlayerGame partialUpdatedPlayerGame = new PlayerGame();
        partialUpdatedPlayerGame.setId(playerGame.getId());

        partialUpdatedPlayerGame
            .goals(UPDATED_GOALS)
            .assists(UPDATED_ASSISTS)
            .attackScore(UPDATED_ATTACK_SCORE)
            .defenseScore(UPDATED_DEFENSE_SCORE);

        restPlayerGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayerGame.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlayerGame))
            )
            .andExpect(status().isOk());

        // Validate the PlayerGame in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlayerGameUpdatableFieldsEquals(partialUpdatedPlayerGame, getPersistedPlayerGame(partialUpdatedPlayerGame));
    }

    @Test
    @Transactional
    void patchNonExistingPlayerGame() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playerGame.setId(longCount.incrementAndGet());

        // Create the PlayerGame
        PlayerGameDTO playerGameDTO = playerGameMapper.toDto(playerGame);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, playerGameDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(playerGameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerGame in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlayerGame() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playerGame.setId(longCount.incrementAndGet());

        // Create the PlayerGame
        PlayerGameDTO playerGameDTO = playerGameMapper.toDto(playerGame);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(playerGameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerGame in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlayerGame() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playerGame.setId(longCount.incrementAndGet());

        // Create the PlayerGame
        PlayerGameDTO playerGameDTO = playerGameMapper.toDto(playerGame);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerGameMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(playerGameDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayerGame in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlayerGame() throws Exception {
        // Initialize the database
        insertedPlayerGame = playerGameRepository.saveAndFlush(playerGame);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the playerGame
        restPlayerGameMockMvc
            .perform(delete(ENTITY_API_URL_ID, playerGame.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return playerGameRepository.count();
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

    protected PlayerGame getPersistedPlayerGame(PlayerGame playerGame) {
        return playerGameRepository.findById(playerGame.getId()).orElseThrow();
    }

    protected void assertPersistedPlayerGameToMatchAllProperties(PlayerGame expectedPlayerGame) {
        assertPlayerGameAllPropertiesEquals(expectedPlayerGame, getPersistedPlayerGame(expectedPlayerGame));
    }

    protected void assertPersistedPlayerGameToMatchUpdatableProperties(PlayerGame expectedPlayerGame) {
        assertPlayerGameAllUpdatablePropertiesEquals(expectedPlayerGame, getPersistedPlayerGame(expectedPlayerGame));
    }
}
