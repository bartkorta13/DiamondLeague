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
import pl.diamondleague.app.repository.StadiumRepository;
import pl.diamondleague.app.service.StadiumService;
import pl.diamondleague.app.service.dto.StadiumDTO;
import pl.diamondleague.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.diamondleague.app.domain.Stadium}.
 */
@RestController
@RequestMapping("/api/stadiums")
public class StadiumResource {

    private static final Logger LOG = LoggerFactory.getLogger(StadiumResource.class);

    private static final String ENTITY_NAME = "stadium";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StadiumService stadiumService;

    private final StadiumRepository stadiumRepository;

    public StadiumResource(StadiumService stadiumService, StadiumRepository stadiumRepository) {
        this.stadiumService = stadiumService;
        this.stadiumRepository = stadiumRepository;
    }

    /**
     * {@code POST  /stadiums} : Create a new stadium.
     *
     * @param stadiumDTO the stadiumDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stadiumDTO, or with status {@code 400 (Bad Request)} if the stadium has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StadiumDTO> createStadium(@Valid @RequestBody StadiumDTO stadiumDTO) throws URISyntaxException {
        LOG.debug("REST request to save Stadium : {}", stadiumDTO);
        if (stadiumDTO.getId() != null) {
            throw new BadRequestAlertException("A new stadium cannot already have an ID", ENTITY_NAME, "idexists");
        }
        stadiumDTO = stadiumService.save(stadiumDTO);
        return ResponseEntity.created(new URI("/api/stadiums/" + stadiumDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, stadiumDTO.getId().toString()))
            .body(stadiumDTO);
    }

    /**
     * {@code PUT  /stadiums/:id} : Updates an existing stadium.
     *
     * @param id the id of the stadiumDTO to save.
     * @param stadiumDTO the stadiumDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stadiumDTO,
     * or with status {@code 400 (Bad Request)} if the stadiumDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stadiumDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StadiumDTO> updateStadium(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StadiumDTO stadiumDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Stadium : {}, {}", id, stadiumDTO);
        if (stadiumDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stadiumDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stadiumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        stadiumDTO = stadiumService.update(stadiumDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stadiumDTO.getId().toString()))
            .body(stadiumDTO);
    }

    /**
     * {@code PATCH  /stadiums/:id} : Partial updates given fields of an existing stadium, field will ignore if it is null
     *
     * @param id the id of the stadiumDTO to save.
     * @param stadiumDTO the stadiumDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stadiumDTO,
     * or with status {@code 400 (Bad Request)} if the stadiumDTO is not valid,
     * or with status {@code 404 (Not Found)} if the stadiumDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the stadiumDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StadiumDTO> partialUpdateStadium(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StadiumDTO stadiumDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Stadium partially : {}, {}", id, stadiumDTO);
        if (stadiumDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stadiumDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stadiumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StadiumDTO> result = stadiumService.partialUpdate(stadiumDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stadiumDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /stadiums} : get all the stadiums.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stadiums in body.
     */
    @GetMapping("")
    public List<StadiumDTO> getAllStadiums() {
        LOG.debug("REST request to get all Stadiums");
        return stadiumService.findAll();
    }

    /**
     * {@code GET  /stadiums/:id} : get the "id" stadium.
     *
     * @param id the id of the stadiumDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stadiumDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StadiumDTO> getStadium(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Stadium : {}", id);
        Optional<StadiumDTO> stadiumDTO = stadiumService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stadiumDTO);
    }

    /**
     * {@code DELETE  /stadiums/:id} : delete the "id" stadium.
     *
     * @param id the id of the stadiumDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStadium(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Stadium : {}", id);
        stadiumService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
