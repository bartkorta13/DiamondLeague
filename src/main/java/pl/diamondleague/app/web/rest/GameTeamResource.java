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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.diamondleague.app.repository.GameTeamRepository;
import pl.diamondleague.app.service.GameTeamService;
import pl.diamondleague.app.service.dto.GameTeamDTO;
import pl.diamondleague.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pl.diamondleague.app.domain.GameTeam}.
 */
@RestController
@RequestMapping("/api/game-teams")
public class GameTeamResource {

    private static final Logger LOG = LoggerFactory.getLogger(GameTeamResource.class);

    private static final String ENTITY_NAME = "gameTeam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameTeamService gameTeamService;

    private final GameTeamRepository gameTeamRepository;

    public GameTeamResource(GameTeamService gameTeamService, GameTeamRepository gameTeamRepository) {
        this.gameTeamService = gameTeamService;
        this.gameTeamRepository = gameTeamRepository;
    }

    /**
     * {@code POST  /game-teams} : Create a new gameTeam.
     *
     * @param gameTeamDTO the gameTeamDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameTeamDTO, or with status {@code 400 (Bad Request)} if the gameTeam has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<GameTeamDTO> createGameTeam(@Valid @RequestBody GameTeamDTO gameTeamDTO) throws URISyntaxException {
        LOG.debug("REST request to save GameTeam : {}", gameTeamDTO);
        if (gameTeamDTO.getId() != null) {
            throw new BadRequestAlertException("A new gameTeam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        gameTeamDTO = gameTeamService.save(gameTeamDTO);
        return ResponseEntity.created(new URI("/api/game-teams/" + gameTeamDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, gameTeamDTO.getId().toString()))
            .body(gameTeamDTO);
    }

    /**
     * {@code PUT  /game-teams/:id} : Updates an existing gameTeam.
     *
     * @param id the id of the gameTeamDTO to save.
     * @param gameTeamDTO the gameTeamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameTeamDTO,
     * or with status {@code 400 (Bad Request)} if the gameTeamDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gameTeamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GameTeamDTO> updateGameTeam(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GameTeamDTO gameTeamDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update GameTeam : {}, {}", id, gameTeamDTO);
        if (gameTeamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameTeamDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameTeamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        gameTeamDTO = gameTeamService.update(gameTeamDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gameTeamDTO.getId().toString()))
            .body(gameTeamDTO);
    }

    /**
     * {@code PATCH  /game-teams/:id} : Partial updates given fields of an existing gameTeam, field will ignore if it is null
     *
     * @param id the id of the gameTeamDTO to save.
     * @param gameTeamDTO the gameTeamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameTeamDTO,
     * or with status {@code 400 (Bad Request)} if the gameTeamDTO is not valid,
     * or with status {@code 404 (Not Found)} if the gameTeamDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the gameTeamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GameTeamDTO> partialUpdateGameTeam(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GameTeamDTO gameTeamDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update GameTeam partially : {}, {}", id, gameTeamDTO);
        if (gameTeamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameTeamDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameTeamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GameTeamDTO> result = gameTeamService.partialUpdate(gameTeamDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gameTeamDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /game-teams} : get all the gameTeams.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gameTeams in body.
     */
    @GetMapping("")
    public ResponseEntity<List<GameTeamDTO>> getAllGameTeams(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of GameTeams");
        Page<GameTeamDTO> page;
        if (eagerload) {
            page = gameTeamService.findAllWithEagerRelationships(pageable);
        } else {
            page = gameTeamService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /game-teams/:id} : get the "id" gameTeam.
     *
     * @param id the id of the gameTeamDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gameTeamDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GameTeamDTO> getGameTeam(@PathVariable("id") Long id) {
        LOG.debug("REST request to get GameTeam : {}", id);
        Optional<GameTeamDTO> gameTeamDTO = gameTeamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gameTeamDTO);
    }

    /**
     * {@code DELETE  /game-teams/:id} : delete the "id" gameTeam.
     *
     * @param id the id of the gameTeamDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGameTeam(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete GameTeam : {}", id);
        gameTeamService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
