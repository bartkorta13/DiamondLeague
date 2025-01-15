package pl.diamondleague.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.diamondleague.app.domain.Game;
import pl.diamondleague.app.repository.GameRepository;
import pl.diamondleague.app.service.GameService;
import pl.diamondleague.app.service.dto.GameDTO;
import pl.diamondleague.app.service.mapper.GameMapper;

/**
 * Service Implementation for managing {@link pl.diamondleague.app.domain.Game}.
 */
@Service
@Transactional
public class GameServiceImpl implements GameService {

    private static final Logger LOG = LoggerFactory.getLogger(GameServiceImpl.class);

    private final GameRepository gameRepository;

    private final GameMapper gameMapper;

    public GameServiceImpl(GameRepository gameRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
    }

    @Override
    public GameDTO save(GameDTO gameDTO) {
        LOG.debug("Request to save Game : {}", gameDTO);
        Game game = gameMapper.toEntity(gameDTO);
        game = gameRepository.save(game);
        return gameMapper.toDto(game);
    }

    @Override
    public GameDTO update(GameDTO gameDTO) {
        LOG.debug("Request to update Game : {}", gameDTO);
        Game game = gameMapper.toEntity(gameDTO);
        game = gameRepository.save(game);
        return gameMapper.toDto(game);
    }

    @Override
    public Optional<GameDTO> partialUpdate(GameDTO gameDTO) {
        LOG.debug("Request to partially update Game : {}", gameDTO);

        return gameRepository
            .findById(gameDTO.getId())
            .map(existingGame -> {
                gameMapper.partialUpdate(existingGame, gameDTO);

                return existingGame;
            })
            .map(gameRepository::save)
            .map(gameMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameDTO> findAll() {
        LOG.debug("Request to get all Games");
        return gameRepository.findAll().stream().map(gameMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GameDTO> findOne(Long id) {
        LOG.debug("Request to get Game : {}", id);
        return gameRepository.findById(id).map(gameMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Game : {}", id);
        gameRepository.deleteById(id);
    }
}
