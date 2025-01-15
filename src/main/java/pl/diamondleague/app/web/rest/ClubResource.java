package pl.diamondleague.app.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.diamondleague.app.repository.ClubRepository;
import pl.diamondleague.app.service.ClubService;
import pl.diamondleague.app.service.dto.ClubDTO;
import pl.diamondleague.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.diamondleague.app.domain.Club}.
 */
@RestController
@RequestMapping("/api/clubs")
public class ClubResource {

    private static final Logger LOG = LoggerFactory.getLogger(ClubResource.class);

    private static final String ENTITY_NAME = "club";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClubService clubService;

    private final ClubRepository clubRepository;

    public ClubResource(ClubService clubService, ClubRepository clubRepository) {
        this.clubService = clubService;
        this.clubRepository = clubRepository;
    }

    /**
     * {@code POST  /clubs} : Create a new club.
     *
     * @param clubDTO the clubDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clubDTO, or with status {@code 400 (Bad Request)} if the club has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ClubDTO> createClub(@Valid @RequestBody ClubDTO clubDTO) throws URISyntaxException {
        LOG.debug("REST request to save Club : {}", clubDTO);
        if (clubDTO.getId() != null) {
            throw new BadRequestAlertException("A new club cannot already have an ID", ENTITY_NAME, "idexists");
        }
        clubDTO = clubService.save(clubDTO);
        return ResponseEntity.created(new URI("/api/clubs/" + clubDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, clubDTO.getId().toString()))
            .body(clubDTO);
    }

    /**
     * {@code PUT  /clubs/:id} : Updates an existing club.
     *
     * @param id the id of the clubDTO to save.
     * @param clubDTO the clubDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clubDTO,
     * or with status {@code 400 (Bad Request)} if the clubDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clubDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClubDTO> updateClub(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClubDTO clubDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Club : {}, {}", id, clubDTO);
        if (clubDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clubDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clubRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        clubDTO = clubService.update(clubDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clubDTO.getId().toString()))
            .body(clubDTO);
    }

    /**
     * {@code PATCH  /clubs/:id} : Partial updates given fields of an existing club, field will ignore if it is null
     *
     * @param id the id of the clubDTO to save.
     * @param clubDTO the clubDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clubDTO,
     * or with status {@code 400 (Bad Request)} if the clubDTO is not valid,
     * or with status {@code 404 (Not Found)} if the clubDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the clubDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClubDTO> partialUpdateClub(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClubDTO clubDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Club partially : {}, {}", id, clubDTO);
        if (clubDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clubDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clubRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClubDTO> result = clubService.partialUpdate(clubDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clubDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /clubs} : get all the clubs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clubs in body.
     */
    @GetMapping("")
    public List<ClubDTO> getAllClubs() {
        LOG.debug("REST request to get all Clubs");
        return clubService.findAll();
    }

    /**
     * {@code GET  /clubs/:id} : get the "id" club.
     *
     * @param id the id of the clubDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clubDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClubDTO> getClub(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Club : {}", id);
        Optional<ClubDTO> clubDTO = clubService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clubDTO);
    }

    /**
     * {@code DELETE  /clubs/:id} : delete the "id" club.
     *
     * @param id the id of the clubDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClub(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Club : {}", id);
        clubService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
