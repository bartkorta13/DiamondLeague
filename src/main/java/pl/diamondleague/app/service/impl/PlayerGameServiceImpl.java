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
import pl.diamondleague.app.domain.PlayerGame;
import pl.diamondleague.app.repository.PlayerGameRepository;
import pl.diamondleague.app.service.PlayerGameService;
import pl.diamondleague.app.service.dto.PlayerGameDTO;
import pl.diamondleague.app.service.mapper.PlayerGameMapper;

/**
 * Service Implementation for managing {@link pl.diamondleague.app.domain.PlayerGame}.
 */
@Service
@Transactional
public class PlayerGameServiceImpl implements PlayerGameService {

    private static final Logger LOG = LoggerFactory.getLogger(PlayerGameServiceImpl.class);

    private final PlayerGameRepository playerGameRepository;

    private final PlayerGameMapper playerGameMapper;

    public PlayerGameServiceImpl(PlayerGameRepository playerGameRepository, PlayerGameMapper playerGameMapper) {
        this.playerGameRepository = playerGameRepository;
        this.playerGameMapper = playerGameMapper;
    }

    @Override
    public PlayerGameDTO save(PlayerGameDTO playerGameDTO) {
        LOG.debug("Request to save PlayerGame : {}", playerGameDTO);
        PlayerGame playerGame = playerGameMapper.toEntity(playerGameDTO);
        playerGame = playerGameRepository.save(playerGame);
        return playerGameMapper.toDto(playerGame);
    }

    @Override
    public PlayerGameDTO update(PlayerGameDTO playerGameDTO) {
        LOG.debug("Request to update PlayerGame : {}", playerGameDTO);
        PlayerGame playerGame = playerGameMapper.toEntity(playerGameDTO);
        playerGame = playerGameRepository.save(playerGame);
        return playerGameMapper.toDto(playerGame);
    }

    @Override
    public Optional<PlayerGameDTO> partialUpdate(PlayerGameDTO playerGameDTO) {
        LOG.debug("Request to partially update PlayerGame : {}", playerGameDTO);

        return playerGameRepository
            .findById(playerGameDTO.getId())
            .map(existingPlayerGame -> {
                playerGameMapper.partialUpdate(existingPlayerGame, playerGameDTO);

                return existingPlayerGame;
            })
            .map(playerGameRepository::save)
            .map(playerGameMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerGameDTO> findAll() {
        LOG.debug("Request to get all PlayerGames");
        return playerGameRepository.findAll().stream().map(playerGameMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<PlayerGameDTO> findAllWithEagerRelationships(Pageable pageable) {
        return playerGameRepository.findAllWithEagerRelationships(pageable).map(playerGameMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlayerGameDTO> findOne(Long id) {
        LOG.debug("Request to get PlayerGame : {}", id);
        return playerGameRepository.findOneWithEagerRelationships(id).map(playerGameMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PlayerGame : {}", id);
        playerGameRepository.deleteById(id);
    }
}
