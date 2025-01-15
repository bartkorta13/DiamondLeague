package pl.diamondleague.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.diamondleague.app.domain.Stadium;
import pl.diamondleague.app.repository.StadiumRepository;
import pl.diamondleague.app.service.StadiumService;
import pl.diamondleague.app.service.dto.StadiumDTO;
import pl.diamondleague.app.service.mapper.StadiumMapper;

/**
 * Service Implementation for managing {@link pl.diamondleague.app.domain.Stadium}.
 */
@Service
@Transactional
public class StadiumServiceImpl implements StadiumService {

    private static final Logger LOG = LoggerFactory.getLogger(StadiumServiceImpl.class);

    private final StadiumRepository stadiumRepository;

    private final StadiumMapper stadiumMapper;

    public StadiumServiceImpl(StadiumRepository stadiumRepository, StadiumMapper stadiumMapper) {
        this.stadiumRepository = stadiumRepository;
        this.stadiumMapper = stadiumMapper;
    }

    @Override
    public StadiumDTO save(StadiumDTO stadiumDTO) {
        LOG.debug("Request to save Stadium : {}", stadiumDTO);
        Stadium stadium = stadiumMapper.toEntity(stadiumDTO);
        stadium = stadiumRepository.save(stadium);
        return stadiumMapper.toDto(stadium);
    }

    @Override
    public StadiumDTO update(StadiumDTO stadiumDTO) {
        LOG.debug("Request to update Stadium : {}", stadiumDTO);
        Stadium stadium = stadiumMapper.toEntity(stadiumDTO);
        stadium = stadiumRepository.save(stadium);
        return stadiumMapper.toDto(stadium);
    }

    @Override
    public Optional<StadiumDTO> partialUpdate(StadiumDTO stadiumDTO) {
        LOG.debug("Request to partially update Stadium : {}", stadiumDTO);

        return stadiumRepository
            .findById(stadiumDTO.getId())
            .map(existingStadium -> {
                stadiumMapper.partialUpdate(existingStadium, stadiumDTO);

                return existingStadium;
            })
            .map(stadiumRepository::save)
            .map(stadiumMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StadiumDTO> findAll() {
        LOG.debug("Request to get all Stadiums");
        return stadiumRepository.findAll().stream().map(stadiumMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StadiumDTO> findOne(Long id) {
        LOG.debug("Request to get Stadium : {}", id);
        return stadiumRepository.findById(id).map(stadiumMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Stadium : {}", id);
        stadiumRepository.deleteById(id);
    }
}
