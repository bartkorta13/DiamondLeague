package pl.diamondleague.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.diamondleague.app.domain.GameAsserts.*;
import static pl.diamondleague.app.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import pl.diamondleague.app.domain.Game;
import pl.diamondleague.app.domain.Stadium;
import pl.diamondleague.app.repository.GameRepository;
import pl.diamondleague.app.service.GameService;
import pl.diamondleague.app.service.dto.GameDTO;
import pl.diamondleague.app.service.mapper.GameMapper;

/**
 * Integration tests for the {@link GameResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class GameResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/games";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GameRepository gameRepository;

    @Mock
    private GameRepository gameRepositoryMock;

    @Autowired
    private GameMapper gameMapper;

    @Mock
    private GameService gameServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameMockMvc;

    private Game game;

    private Game insertedGame;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Game createEntity(EntityManager em) {
        Game game = new Game().date(DEFAULT_DATE);
        // Add required entity
        Stadium stadium;
        if (TestUtil.findAll(em, Stadium.class).isEmpty()) {
            stadium = StadiumResourceIT.createEntity();
            em.persist(stadium);
            em.flush();
        } else {
            stadium = TestUtil.findAll(em, Stadium.class).get(0);
        }
        game.setStadium(stadium);
        return game;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Game createUpdatedEntity(EntityManager em) {
        Game updatedGame = new Game().date(UPDATED_DATE);
        // Add required entity
        Stadium stadium;
        if (TestUtil.findAll(em, Stadium.class).isEmpty()) {
            stadium = StadiumResourceIT.createUpdatedEntity();
            em.persist(stadium);
            em.flush();
        } else {
            stadium = TestUtil.findAll(em, Stadium.class).get(0);
        }
        updatedGame.setStadium(stadium);
        return updatedGame;
    }

    @BeforeEach
    public void initTest() {
        game = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedGame != null) {
            gameRepository.delete(insertedGame);
            insertedGame = null;
        }
    }

    @Test
    @Transactional
    void createGame() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);
        var returnedGameDTO = om.readValue(
            restGameMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gameDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GameDTO.class
        );

        // Validate the Game in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedGame = gameMapper.toEntity(returnedGameDTO);
        assertGameUpdatableFieldsEquals(returnedGame, getPersistedGame(returnedGame));

        insertedGame = returnedGame;
    }

    @Test
    @Transactional
    void createGameWithExistingId() throws Exception {
        // Create the Game with an existing ID
        game.setId(1L);
        GameDTO gameDTO = gameMapper.toDto(game);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gameDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        game.setDate(null);

        // Create the Game, which fails.
        GameDTO gameDTO = gameMapper.toDto(game);

        restGameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gameDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGames() throws Exception {
        // Initialize the database
        insertedGame = gameRepository.saveAndFlush(game);

        // Get all the gameList
        restGameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(game.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGamesWithEagerRelationshipsIsEnabled() throws Exception {
        when(gameServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGameMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(gameServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGamesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(gameServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGameMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(gameRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getGame() throws Exception {
        // Initialize the database
        insertedGame = gameRepository.saveAndFlush(game);

        // Get the game
        restGameMockMvc
            .perform(get(ENTITY_API_URL_ID, game.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(game.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingGame() throws Exception {
        // Get the game
        restGameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGame() throws Exception {
        // Initialize the database
        insertedGame = gameRepository.saveAndFlush(game);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the game
        Game updatedGame = gameRepository.findById(game.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGame are not directly saved in db
        em.detach(updatedGame);
        updatedGame.date(UPDATED_DATE);
        GameDTO gameDTO = gameMapper.toDto(updatedGame);

        restGameMockMvc
            .perform(put(ENTITY_API_URL_ID, gameDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gameDTO)))
            .andExpect(status().isOk());

        // Validate the Game in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGameToMatchAllProperties(updatedGame);
    }

    @Test
    @Transactional
    void putNonExistingGame() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        game.setId(longCount.incrementAndGet());

        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(put(ENTITY_API_URL_ID, gameDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gameDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGame() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        game.setId(longCount.incrementAndGet());

        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(gameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGame() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        game.setId(longCount.incrementAndGet());

        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gameDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Game in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGameWithPatch() throws Exception {
        // Initialize the database
        insertedGame = gameRepository.saveAndFlush(game);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the game using partial update
        Game partialUpdatedGame = new Game();
        partialUpdatedGame.setId(game.getId());

        restGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGame.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGame))
            )
            .andExpect(status().isOk());

        // Validate the Game in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGameUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedGame, game), getPersistedGame(game));
    }

    @Test
    @Transactional
    void fullUpdateGameWithPatch() throws Exception {
        // Initialize the database
        insertedGame = gameRepository.saveAndFlush(game);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the game using partial update
        Game partialUpdatedGame = new Game();
        partialUpdatedGame.setId(game.getId());

        partialUpdatedGame.date(UPDATED_DATE);

        restGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGame.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGame))
            )
            .andExpect(status().isOk());

        // Validate the Game in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGameUpdatableFieldsEquals(partialUpdatedGame, getPersistedGame(partialUpdatedGame));
    }

    @Test
    @Transactional
    void patchNonExistingGame() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        game.setId(longCount.incrementAndGet());

        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gameDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(gameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGame() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        game.setId(longCount.incrementAndGet());

        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(gameDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGame() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        game.setId(longCount.incrementAndGet());

        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(gameDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Game in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGame() throws Exception {
        // Initialize the database
        insertedGame = gameRepository.saveAndFlush(game);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the game
        restGameMockMvc
            .perform(delete(ENTITY_API_URL_ID, game.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return gameRepository.count();
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

    protected Game getPersistedGame(Game game) {
        return gameRepository.findById(game.getId()).orElseThrow();
    }

    protected void assertPersistedGameToMatchAllProperties(Game expectedGame) {
        assertGameAllPropertiesEquals(expectedGame, getPersistedGame(expectedGame));
    }

    protected void assertPersistedGameToMatchUpdatableProperties(Game expectedGame) {
        assertGameAllUpdatablePropertiesEquals(expectedGame, getPersistedGame(expectedGame));
    }
}
