package pl.diamondleague.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.diamondleague.app.domain.Rating;
import pl.diamondleague.app.repository.RatingRepository;
import pl.diamondleague.app.service.RatingService;
import pl.diamondleague.app.service.dto.RatingDTO;
import pl.diamondleague.app.service.mapper.RatingMapper;

/**
 * Service Implementation for managing {@link pl.diamondleague.app.domain.Rating}.
 */
@Service
@Transactional
public class RatingServiceImpl implements RatingService {

    private static final Logger LOG = LoggerFactory.getLogger(RatingServiceImpl.class);

    private final RatingRepository ratingRepository;

    private final RatingMapper ratingMapper;

    public RatingServiceImpl(RatingRepository ratingRepository, RatingMapper ratingMapper) {
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
    }

    @Override
    public RatingDTO save(RatingDTO ratingDTO) {
        LOG.debug("Request to save Rating : {}", ratingDTO);
        Rating rating = ratingMapper.toEntity(ratingDTO);
        rating = ratingRepository.save(rating);
        return ratingMapper.toDto(rating);
    }

    @Override
    public RatingDTO update(RatingDTO ratingDTO) {
        LOG.debug("Request to update Rating : {}", ratingDTO);
        Rating rating = ratingMapper.toEntity(ratingDTO);
        rating = ratingRepository.save(rating);
        return ratingMapper.toDto(rating);
    }

    @Override
    public Optional<RatingDTO> partialUpdate(RatingDTO ratingDTO) {
        LOG.debug("Request to partially update Rating : {}", ratingDTO);

        return ratingRepository
            .findById(ratingDTO.getId())
            .map(existingRating -> {
                ratingMapper.partialUpdate(existingRating, ratingDTO);

                return existingRating;
            })
            .map(ratingRepository::save)
            .map(ratingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RatingDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Ratings");
        return ratingRepository.findAll(pageable).map(ratingMapper::toDto);
    }

    public Page<RatingDTO> findAllWithEagerRelationships(Pageable pageable) {
        return ratingRepository.findAllWithEagerRelationships(pageable).map(ratingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RatingDTO> findOne(Long id) {
        LOG.debug("Request to get Rating : {}", id);
        return ratingRepository.findOneWithEagerRelationships(id).map(ratingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Rating : {}", id);
        ratingRepository.deleteById(id);
    }
}
