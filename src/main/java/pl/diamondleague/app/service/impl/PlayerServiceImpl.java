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
import pl.diamondleague.app.domain.Player;
import pl.diamondleague.app.repository.PlayerRepository;
import pl.diamondleague.app.service.PlayerService;
import pl.diamondleague.app.service.dto.PlayerDTO;
import pl.diamondleague.app.service.mapper.PlayerMapper;

/**
 * Service Implementation for managing {@link pl.diamondleague.app.domain.Player}.
 */
@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {

    private static final Logger LOG = LoggerFactory.getLogger(PlayerServiceImpl.class);

    private final PlayerRepository playerRepository;

    private final PlayerMapper playerMapper;

    public PlayerServiceImpl(PlayerRepository playerRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    @Override
    public PlayerDTO save(PlayerDTO playerDTO) {
        LOG.debug("Request to save Player : {}", playerDTO);
        Player player = playerMapper.toEntity(playerDTO);
        player = playerRepository.save(player);
        return playerMapper.toDto(player);
    }

    @Override
    public PlayerDTO update(PlayerDTO playerDTO) {
        LOG.debug("Request to update Player : {}", playerDTO);
        Player player = playerMapper.toEntity(playerDTO);
        player = playerRepository.save(player);
        return playerMapper.toDto(player);
    }

    @Override
    public Optional<PlayerDTO> partialUpdate(PlayerDTO playerDTO) {
        LOG.debug("Request to partially update Player : {}", playerDTO);

        return playerRepository
            .findById(playerDTO.getId())
            .map(existingPlayer -> {
                playerMapper.partialUpdate(existingPlayer, playerDTO);

                return existingPlayer;
            })
            .map(playerRepository::save)
            .map(playerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerDTO> findAll() {
        LOG.debug("Request to get all Players");
        return playerRepository.findAll().stream().map(playerMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<PlayerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return playerRepository.findAllWithEagerRelationships(pageable).map(playerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlayerDTO> findOne(Long id) {
        LOG.debug("Request to get Player : {}", id);
        return playerRepository.findOneWithEagerRelationships(id).map(playerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Player : {}", id);
        playerRepository.deleteById(id);
    }
}
