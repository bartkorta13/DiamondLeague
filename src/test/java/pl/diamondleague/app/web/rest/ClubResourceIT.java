package pl.diamondleague.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.diamondleague.app.domain.ClubAsserts.*;
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
import pl.diamondleague.app.domain.Club;
import pl.diamondleague.app.repository.ClubRepository;
import pl.diamondleague.app.service.dto.ClubDTO;
import pl.diamondleague.app.service.mapper.ClubMapper;

/**
 * Integration tests for the {@link ClubResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClubResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_PATH = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/clubs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubMapper clubMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClubMockMvc;

    private Club club;

    private Club insertedClub;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Club createEntity() {
        return new Club().name(DEFAULT_NAME).logoPath(DEFAULT_LOGO_PATH);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Club createUpdatedEntity() {
        return new Club().name(UPDATED_NAME).logoPath(UPDATED_LOGO_PATH);
    }

    @BeforeEach
    public void initTest() {
        club = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedClub != null) {
            clubRepository.delete(insertedClub);
            insertedClub = null;
        }
    }

    @Test
    @Transactional
    void createClub() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Club
        ClubDTO clubDTO = clubMapper.toDto(club);
        var returnedClubDTO = om.readValue(
            restClubMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clubDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClubDTO.class
        );

        // Validate the Club in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedClub = clubMapper.toEntity(returnedClubDTO);
        assertClubUpdatableFieldsEquals(returnedClub, getPersistedClub(returnedClub));

        insertedClub = returnedClub;
    }

    @Test
    @Transactional
    void createClubWithExistingId() throws Exception {
        // Create the Club with an existing ID
        club.setId(1L);
        ClubDTO clubDTO = clubMapper.toDto(club);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClubMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clubDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Club in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        club.setName(null);

        // Create the Club, which fails.
        ClubDTO clubDTO = clubMapper.toDto(club);

        restClubMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clubDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClubs() throws Exception {
        // Initialize the database
        insertedClub = clubRepository.saveAndFlush(club);

        // Get all the clubList
        restClubMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(club.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].logoPath").value(hasItem(DEFAULT_LOGO_PATH)));
    }

    @Test
    @Transactional
    void getClub() throws Exception {
        // Initialize the database
        insertedClub = clubRepository.saveAndFlush(club);

        // Get the club
        restClubMockMvc
            .perform(get(ENTITY_API_URL_ID, club.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(club.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.logoPath").value(DEFAULT_LOGO_PATH));
    }

    @Test
    @Transactional
    void getNonExistingClub() throws Exception {
        // Get the club
        restClubMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClub() throws Exception {
        // Initialize the database
        insertedClub = clubRepository.saveAndFlush(club);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the club
        Club updatedClub = clubRepository.findById(club.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClub are not directly saved in db
        em.detach(updatedClub);
        updatedClub.name(UPDATED_NAME).logoPath(UPDATED_LOGO_PATH);
        ClubDTO clubDTO = clubMapper.toDto(updatedClub);

        restClubMockMvc
            .perform(put(ENTITY_API_URL_ID, clubDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clubDTO)))
            .andExpect(status().isOk());

        // Validate the Club in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClubToMatchAllProperties(updatedClub);
    }

    @Test
    @Transactional
    void putNonExistingClub() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        club.setId(longCount.incrementAndGet());

        // Create the Club
        ClubDTO clubDTO = clubMapper.toDto(club);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClubMockMvc
            .perform(put(ENTITY_API_URL_ID, clubDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clubDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Club in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClub() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        club.setId(longCount.incrementAndGet());

        // Create the Club
        ClubDTO clubDTO = clubMapper.toDto(club);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClubMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clubDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Club in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClub() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        club.setId(longCount.incrementAndGet());

        // Create the Club
        ClubDTO clubDTO = clubMapper.toDto(club);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClubMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clubDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Club in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClubWithPatch() throws Exception {
        // Initialize the database
        insertedClub = clubRepository.saveAndFlush(club);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the club using partial update
        Club partialUpdatedClub = new Club();
        partialUpdatedClub.setId(club.getId());

        partialUpdatedClub.name(UPDATED_NAME).logoPath(UPDATED_LOGO_PATH);

        restClubMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClub.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClub))
            )
            .andExpect(status().isOk());

        // Validate the Club in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClubUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedClub, club), getPersistedClub(club));
    }

    @Test
    @Transactional
    void fullUpdateClubWithPatch() throws Exception {
        // Initialize the database
        insertedClub = clubRepository.saveAndFlush(club);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the club using partial update
        Club partialUpdatedClub = new Club();
        partialUpdatedClub.setId(club.getId());

        partialUpdatedClub.name(UPDATED_NAME).logoPath(UPDATED_LOGO_PATH);

        restClubMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClub.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClub))
            )
            .andExpect(status().isOk());

        // Validate the Club in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClubUpdatableFieldsEquals(partialUpdatedClub, getPersistedClub(partialUpdatedClub));
    }

    @Test
    @Transactional
    void patchNonExistingClub() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        club.setId(longCount.incrementAndGet());

        // Create the Club
        ClubDTO clubDTO = clubMapper.toDto(club);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClubMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clubDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(clubDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Club in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClub() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        club.setId(longCount.incrementAndGet());

        // Create the Club
        ClubDTO clubDTO = clubMapper.toDto(club);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClubMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clubDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Club in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClub() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        club.setId(longCount.incrementAndGet());

        // Create the Club
        ClubDTO clubDTO = clubMapper.toDto(club);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClubMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(clubDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Club in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClub() throws Exception {
        // Initialize the database
        insertedClub = clubRepository.saveAndFlush(club);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the club
        restClubMockMvc
            .perform(delete(ENTITY_API_URL_ID, club.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return clubRepository.count();
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

    protected Club getPersistedClub(Club club) {
        return clubRepository.findById(club.getId()).orElseThrow();
    }

    protected void assertPersistedClubToMatchAllProperties(Club expectedClub) {
        assertClubAllPropertiesEquals(expectedClub, getPersistedClub(expectedClub));
    }

    protected void assertPersistedClubToMatchUpdatableProperties(Club expectedClub) {
        assertClubAllUpdatablePropertiesEquals(expectedClub, getPersistedClub(expectedClub));
    }
}
