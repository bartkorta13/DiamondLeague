package pl.diamondleague.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.diamondleague.app.domain.Club;
import pl.diamondleague.app.repository.ClubRepository;
import pl.diamondleague.app.service.ClubService;
import pl.diamondleague.app.service.dto.ClubDTO;
import pl.diamondleague.app.service.mapper.ClubMapper;

/**
 * Service Implementation for managing {@link pl.diamondleague.app.domain.Club}.
 */
@Service
@Transactional
public class ClubServiceImpl implements ClubService {

    private static final Logger LOG = LoggerFactory.getLogger(ClubServiceImpl.class);

    private final ClubRepository clubRepository;

    private final ClubMapper clubMapper;

    public ClubServiceImpl(ClubRepository clubRepository, ClubMapper clubMapper) {
        this.clubRepository = clubRepository;
        this.clubMapper = clubMapper;
    }

    @Override
    public ClubDTO save(ClubDTO clubDTO) {
        LOG.debug("Request to save Club : {}", clubDTO);
        Club club = clubMapper.toEntity(clubDTO);
        club = clubRepository.save(club);
        return clubMapper.toDto(club);
    }

    @Override
    public ClubDTO update(ClubDTO clubDTO) {
        LOG.debug("Request to update Club : {}", clubDTO);
        Club club = clubMapper.toEntity(clubDTO);
        club = clubRepository.save(club);
        return clubMapper.toDto(club);
    }

    @Override
    public Optional<ClubDTO> partialUpdate(ClubDTO clubDTO) {
        LOG.debug("Request to partially update Club : {}", clubDTO);

        return clubRepository
            .findById(clubDTO.getId())
            .map(existingClub -> {
                clubMapper.partialUpdate(existingClub, clubDTO);

                return existingClub;
            })
            .map(clubRepository::save)
            .map(clubMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubDTO> findAll() {
        LOG.debug("Request to get all Clubs");
        return clubRepository.findAll().stream().map(clubMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClubDTO> findOne(Long id) {
        LOG.debug("Request to get Club : {}", id);
        return clubRepository.findById(id).map(clubMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Club : {}", id);
        clubRepository.deleteById(id);
    }
}
