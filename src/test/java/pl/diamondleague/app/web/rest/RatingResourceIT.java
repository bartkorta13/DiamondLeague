package pl.diamondleague.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.diamondleague.app.domain.RatingAsserts.*;
import static pl.diamondleague.app.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import pl.diamondleague.app.domain.Player;
import pl.diamondleague.app.domain.Rating;
import pl.diamondleague.app.repository.RatingRepository;
import pl.diamondleague.app.service.dto.RatingDTO;
import pl.diamondleague.app.service.mapper.RatingMapper;

/**
 * Integration tests for the {@link RatingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RatingResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_ATTACK = 1;
    private static final Integer UPDATED_ATTACK = 2;

    private static final Integer DEFAULT_DEFENSE = 1;
    private static final Integer UPDATED_DEFENSE = 2;

    private static final Integer DEFAULT_ENGAGEMENT = 1;
    private static final Integer UPDATED_ENGAGEMENT = 2;

    private static final Integer DEFAULT_OVERALL = 70;
    private static final Integer UPDATED_OVERALL = 71;

    private static final String ENTITY_API_URL = "/api/ratings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingMapper ratingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRatingMockMvc;

    private Rating rating;

    private Rating insertedRating;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rating createEntity(EntityManager em) {
        Rating rating = new Rating()
            .date(DEFAULT_DATE)
            .attack(DEFAULT_ATTACK)
            .defense(DEFAULT_DEFENSE)
            .engagement(DEFAULT_ENGAGEMENT)
            .overall(DEFAULT_OVERALL);
        // Add required entity
        Player player;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            player = PlayerResourceIT.createEntity(em);
            em.persist(player);
            em.flush();
        } else {
            player = TestUtil.findAll(em, Player.class).get(0);
        }
        rating.setPlayer(player);
        return rating;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rating createUpdatedEntity(EntityManager em) {
        Rating updatedRating = new Rating()
            .date(UPDATED_DATE)
            .attack(UPDATED_ATTACK)
            .defense(UPDATED_DEFENSE)
            .engagement(UPDATED_ENGAGEMENT)
            .overall(UPDATED_OVERALL);
        // Add required entity
        Player player;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            player = PlayerResourceIT.createUpdatedEntity(em);
            em.persist(player);
            em.flush();
        } else {
            player = TestUtil.findAll(em, Player.class).get(0);
        }
        updatedRating.setPlayer(player);
        return updatedRating;
    }

    @BeforeEach
    public void initTest() {
        rating = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedRating != null) {
            ratingRepository.delete(insertedRating);
            insertedRating = null;
        }
    }

    @Test
    @Transactional
    void createRating() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);
        var returnedRatingDTO = om.readValue(
            restRatingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RatingDTO.class
        );

        // Validate the Rating in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRating = ratingMapper.toEntity(returnedRatingDTO);
        assertRatingUpdatableFieldsEquals(returnedRating, getPersistedRating(returnedRating));

        insertedRating = returnedRating;
    }

    @Test
    @Transactional
    void createRatingWithExistingId() throws Exception {
        // Create the Rating with an existing ID
        rating.setId(1L);
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRatingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rating.setDate(null);

        // Create the Rating, which fails.
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        restRatingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAttackIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rating.setAttack(null);

        // Create the Rating, which fails.
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        restRatingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDefenseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rating.setDefense(null);

        // Create the Rating, which fails.
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        restRatingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEngagementIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rating.setEngagement(null);

        // Create the Rating, which fails.
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        restRatingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOverallIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rating.setOverall(null);

        // Create the Rating, which fails.
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        restRatingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRatings() throws Exception {
        // Initialize the database
        insertedRating = ratingRepository.saveAndFlush(rating);

        // Get all the ratingList
        restRatingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rating.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].attack").value(hasItem(DEFAULT_ATTACK)))
            .andExpect(jsonPath("$.[*].defense").value(hasItem(DEFAULT_DEFENSE)))
            .andExpect(jsonPath("$.[*].engagement").value(hasItem(DEFAULT_ENGAGEMENT)))
            .andExpect(jsonPath("$.[*].overall").value(hasItem(DEFAULT_OVERALL)));
    }

    @Test
    @Transactional
    void getRating() throws Exception {
        // Initialize the database
        insertedRating = ratingRepository.saveAndFlush(rating);

        // Get the rating
        restRatingMockMvc
            .perform(get(ENTITY_API_URL_ID, rating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rating.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.attack").value(DEFAULT_ATTACK))
            .andExpect(jsonPath("$.defense").value(DEFAULT_DEFENSE))
            .andExpect(jsonPath("$.engagement").value(DEFAULT_ENGAGEMENT))
            .andExpect(jsonPath("$.overall").value(DEFAULT_OVERALL));
    }

    @Test
    @Transactional
    void getNonExistingRating() throws Exception {
        // Get the rating
        restRatingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRating() throws Exception {
        // Initialize the database
        insertedRating = ratingRepository.saveAndFlush(rating);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRating are not directly saved in db
        em.detach(updatedRating);
        updatedRating
            .date(UPDATED_DATE)
            .attack(UPDATED_ATTACK)
            .defense(UPDATED_DEFENSE)
            .engagement(UPDATED_ENGAGEMENT)
            .overall(UPDATED_OVERALL);
        RatingDTO ratingDTO = ratingMapper.toDto(updatedRating);

        restRatingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ratingDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Rating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRatingToMatchAllProperties(updatedRating);
    }

    @Test
    @Transactional
    void putNonExistingRating() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rating.setId(longCount.incrementAndGet());

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRatingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ratingDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRating() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rating.setId(longCount.incrementAndGet());

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ratingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRating() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rating.setId(longCount.incrementAndGet());

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRatingWithPatch() throws Exception {
        // Initialize the database
        insertedRating = ratingRepository.saveAndFlush(rating);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rating using partial update
        Rating partialUpdatedRating = new Rating();
        partialUpdatedRating.setId(rating.getId());

        partialUpdatedRating.date(UPDATED_DATE).engagement(UPDATED_ENGAGEMENT);

        restRatingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRating.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRating))
            )
            .andExpect(status().isOk());

        // Validate the Rating in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRatingUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRating, rating), getPersistedRating(rating));
    }

    @Test
    @Transactional
    void fullUpdateRatingWithPatch() throws Exception {
        // Initialize the database
        insertedRating = ratingRepository.saveAndFlush(rating);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rating using partial update
        Rating partialUpdatedRating = new Rating();
        partialUpdatedRating.setId(rating.getId());

        partialUpdatedRating
            .date(UPDATED_DATE)
            .attack(UPDATED_ATTACK)
            .defense(UPDATED_DEFENSE)
            .engagement(UPDATED_ENGAGEMENT)
            .overall(UPDATED_OVERALL);

        restRatingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRating.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRating))
            )
            .andExpect(status().isOk());

        // Validate the Rating in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRatingUpdatableFieldsEquals(partialUpdatedRating, getPersistedRating(partialUpdatedRating));
    }

    @Test
    @Transactional
    void patchNonExistingRating() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rating.setId(longCount.incrementAndGet());

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRatingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ratingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ratingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRating() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rating.setId(longCount.incrementAndGet());

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ratingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRating() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rating.setId(longCount.incrementAndGet());

        // Create the Rating
        RatingDTO ratingDTO = ratingMapper.toDto(rating);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ratingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rating in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRating() throws Exception {
        // Initialize the database
        insertedRating = ratingRepository.saveAndFlush(rating);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the rating
        restRatingMockMvc
            .perform(delete(ENTITY_API_URL_ID, rating.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ratingRepository.count();
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

    protected Rating getPersistedRating(Rating rating) {
        return ratingRepository.findById(rating.getId()).orElseThrow();
    }

    protected void assertPersistedRatingToMatchAllProperties(Rating expectedRating) {
        assertRatingAllPropertiesEquals(expectedRating, getPersistedRating(expectedRating));
    }

    protected void assertPersistedRatingToMatchUpdatableProperties(Rating expectedRating) {
        assertRatingAllUpdatablePropertiesEquals(expectedRating, getPersistedRating(expectedRating));
    }
}
