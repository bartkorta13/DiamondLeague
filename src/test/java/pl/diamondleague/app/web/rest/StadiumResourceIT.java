package pl.diamondleague.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.diamondleague.app.domain.StadiumAsserts.*;
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
import pl.diamondleague.app.domain.Stadium;
import pl.diamondleague.app.repository.StadiumRepository;
import pl.diamondleague.app.service.dto.StadiumDTO;
import pl.diamondleague.app.service.mapper.StadiumMapper;

/**
 * Integration tests for the {@link StadiumResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StadiumResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/stadiums";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private StadiumMapper stadiumMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStadiumMockMvc;

    private Stadium stadium;

    private Stadium insertedStadium;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stadium createEntity() {
        return new Stadium().name(DEFAULT_NAME).imagePath(DEFAULT_IMAGE_PATH);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stadium createUpdatedEntity() {
        return new Stadium().name(UPDATED_NAME).imagePath(UPDATED_IMAGE_PATH);
    }

    @BeforeEach
    public void initTest() {
        stadium = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedStadium != null) {
            stadiumRepository.delete(insertedStadium);
            insertedStadium = null;
        }
    }

    @Test
    @Transactional
    void createStadium() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Stadium
        StadiumDTO stadiumDTO = stadiumMapper.toDto(stadium);
        var returnedStadiumDTO = om.readValue(
            restStadiumMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stadiumDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StadiumDTO.class
        );

        // Validate the Stadium in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStadium = stadiumMapper.toEntity(returnedStadiumDTO);
        assertStadiumUpdatableFieldsEquals(returnedStadium, getPersistedStadium(returnedStadium));

        insertedStadium = returnedStadium;
    }

    @Test
    @Transactional
    void createStadiumWithExistingId() throws Exception {
        // Create the Stadium with an existing ID
        stadium.setId(1L);
        StadiumDTO stadiumDTO = stadiumMapper.toDto(stadium);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStadiumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stadiumDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Stadium in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        stadium.setName(null);

        // Create the Stadium, which fails.
        StadiumDTO stadiumDTO = stadiumMapper.toDto(stadium);

        restStadiumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stadiumDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStadiums() throws Exception {
        // Initialize the database
        insertedStadium = stadiumRepository.saveAndFlush(stadium);

        // Get all the stadiumList
        restStadiumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stadium.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imagePath").value(hasItem(DEFAULT_IMAGE_PATH)));
    }

    @Test
    @Transactional
    void getStadium() throws Exception {
        // Initialize the database
        insertedStadium = stadiumRepository.saveAndFlush(stadium);

        // Get the stadium
        restStadiumMockMvc
            .perform(get(ENTITY_API_URL_ID, stadium.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stadium.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.imagePath").value(DEFAULT_IMAGE_PATH));
    }

    @Test
    @Transactional
    void getNonExistingStadium() throws Exception {
        // Get the stadium
        restStadiumMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStadium() throws Exception {
        // Initialize the database
        insertedStadium = stadiumRepository.saveAndFlush(stadium);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stadium
        Stadium updatedStadium = stadiumRepository.findById(stadium.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStadium are not directly saved in db
        em.detach(updatedStadium);
        updatedStadium.name(UPDATED_NAME).imagePath(UPDATED_IMAGE_PATH);
        StadiumDTO stadiumDTO = stadiumMapper.toDto(updatedStadium);

        restStadiumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stadiumDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stadiumDTO))
            )
            .andExpect(status().isOk());

        // Validate the Stadium in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStadiumToMatchAllProperties(updatedStadium);
    }

    @Test
    @Transactional
    void putNonExistingStadium() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stadium.setId(longCount.incrementAndGet());

        // Create the Stadium
        StadiumDTO stadiumDTO = stadiumMapper.toDto(stadium);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStadiumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stadiumDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stadiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stadium in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStadium() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stadium.setId(longCount.incrementAndGet());

        // Create the Stadium
        StadiumDTO stadiumDTO = stadiumMapper.toDto(stadium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStadiumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(stadiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stadium in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStadium() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stadium.setId(longCount.incrementAndGet());

        // Create the Stadium
        StadiumDTO stadiumDTO = stadiumMapper.toDto(stadium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStadiumMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(stadiumDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Stadium in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStadiumWithPatch() throws Exception {
        // Initialize the database
        insertedStadium = stadiumRepository.saveAndFlush(stadium);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stadium using partial update
        Stadium partialUpdatedStadium = new Stadium();
        partialUpdatedStadium.setId(stadium.getId());

        restStadiumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStadium.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStadium))
            )
            .andExpect(status().isOk());

        // Validate the Stadium in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStadiumUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedStadium, stadium), getPersistedStadium(stadium));
    }

    @Test
    @Transactional
    void fullUpdateStadiumWithPatch() throws Exception {
        // Initialize the database
        insertedStadium = stadiumRepository.saveAndFlush(stadium);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the stadium using partial update
        Stadium partialUpdatedStadium = new Stadium();
        partialUpdatedStadium.setId(stadium.getId());

        partialUpdatedStadium.name(UPDATED_NAME).imagePath(UPDATED_IMAGE_PATH);

        restStadiumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStadium.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStadium))
            )
            .andExpect(status().isOk());

        // Validate the Stadium in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStadiumUpdatableFieldsEquals(partialUpdatedStadium, getPersistedStadium(partialUpdatedStadium));
    }

    @Test
    @Transactional
    void patchNonExistingStadium() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stadium.setId(longCount.incrementAndGet());

        // Create the Stadium
        StadiumDTO stadiumDTO = stadiumMapper.toDto(stadium);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStadiumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stadiumDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stadiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stadium in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStadium() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stadium.setId(longCount.incrementAndGet());

        // Create the Stadium
        StadiumDTO stadiumDTO = stadiumMapper.toDto(stadium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStadiumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(stadiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stadium in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStadium() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        stadium.setId(longCount.incrementAndGet());

        // Create the Stadium
        StadiumDTO stadiumDTO = stadiumMapper.toDto(stadium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStadiumMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(stadiumDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Stadium in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStadium() throws Exception {
        // Initialize the database
        insertedStadium = stadiumRepository.saveAndFlush(stadium);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the stadium
        restStadiumMockMvc
            .perform(delete(ENTITY_API_URL_ID, stadium.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return stadiumRepository.count();
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

    protected Stadium getPersistedStadium(Stadium stadium) {
        return stadiumRepository.findById(stadium.getId()).orElseThrow();
    }

    protected void assertPersistedStadiumToMatchAllProperties(Stadium expectedStadium) {
        assertStadiumAllPropertiesEquals(expectedStadium, getPersistedStadium(expectedStadium));
    }

    protected void assertPersistedStadiumToMatchUpdatableProperties(Stadium expectedStadium) {
        assertStadiumAllUpdatablePropertiesEquals(expectedStadium, getPersistedStadium(expectedStadium));
    }
}
