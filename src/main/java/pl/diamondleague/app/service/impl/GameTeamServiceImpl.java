package pl.diamondleague.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.diamondleague.app.domain.GameTeam;
import pl.diamondleague.app.repository.GameTeamRepository;
import pl.diamondleague.app.service.GameTeamService;
import pl.diamondleague.app.service.dto.GameTeamDTO;
import pl.diamondleague.app.service.mapper.GameTeamMapper;

/**
 * Service Implementation for managing {@link pl.diamondleague.app.domain.GameTeam}.
 */
@Service
@Transactional
public class GameTeamServiceImpl implements GameTeamService {

    private static final Logger LOG = LoggerFactory.getLogger(GameTeamServiceImpl.class);

    private final GameTeamRepository gameTeamRepository;

    private final GameTeamMapper gameTeamMapper;

    public GameTeamServiceImpl(GameTeamRepository gameTeamRepository, GameTeamMapper gameTeamMapper) {
        this.gameTeamRepository = gameTeamRepository;
        this.gameTeamMapper = gameTeamMapper;
    }

    @Override
    public GameTeamDTO save(GameTeamDTO gameTeamDTO) {
        LOG.debug("Request to save GameTeam : {}", gameTeamDTO);
        GameTeam gameTeam = gameTeamMapper.toEntity(gameTeamDTO);
        gameTeam = gameTeamRepository.save(gameTeam);
        return gameTeamMapper.toDto(gameTeam);
    }

    @Override
    public GameTeamDTO update(GameTeamDTO gameTeamDTO) {
        LOG.debug("Request to update GameTeam : {}", gameTeamDTO);
        GameTeam gameTeam = gameTeamMapper.toEntity(gameTeamDTO);
        gameTeam = gameTeamRepository.save(gameTeam);
        return gameTeamMapper.toDto(gameTeam);
    }

    @Override
    public Optional<GameTeamDTO> partialUpdate(GameTeamDTO gameTeamDTO) {
        LOG.debug("Request to partially update GameTeam : {}", gameTeamDTO);

        return gameTeamRepository
            .findById(gameTeamDTO.getId())
            .map(existingGameTeam -> {
                gameTeamMapper.partialUpdate(existingGameTeam, gameTeamDTO);

                return existingGameTeam;
            })
            .map(gameTeamRepository::save)
            .map(gameTeamMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameTeamDTO> findAll() {
        LOG.debug("Request to get all GameTeams");
        return gameTeamRepository.findAll().stream().map(gameTeamMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<GameTeamDTO> findAllWithEagerRelationships(Pageable pageable) {
        return gameTeamRepository.findAllWithEagerRelationships(pageable).map(gameTeamMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GameTeamDTO> findOne(Long id) {
        LOG.debug("Request to get GameTeam : {}", id);
        return gameTeamRepository.findOneWithEagerRelationships(id).map(gameTeamMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete GameTeam : {}", id);
        gameTeamRepository.deleteById(id);
    }
}
