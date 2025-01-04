package com.satoripop.rfp.service.impl;

import com.satoripop.rfp.domain.Reference;
import com.satoripop.rfp.repository.ReferenceRepository;
import com.satoripop.rfp.service.ReferenceService;
import com.satoripop.rfp.service.dto.ReferenceDTO;
import com.satoripop.rfp.service.dto.TechnologyDTO;
import com.satoripop.rfp.service.mapper.ReferenceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.satoripop.rfp.domain.Reference}.
 */
@Service
@Transactional
public class ReferenceServiceImpl implements ReferenceService {

    private final Logger log = LoggerFactory.getLogger(ReferenceServiceImpl.class);

    private final ReferenceRepository referenceRepository;

    private final ReferenceMapper referenceMapper;

    public ReferenceServiceImpl(ReferenceRepository referenceRepository, ReferenceMapper referenceMapper) {
        this.referenceRepository = referenceRepository;
        this.referenceMapper = referenceMapper;
    }

    @Override
    public ReferenceDTO save(ReferenceDTO referenceDTO) {
        log.debug("Request to save Reference : {}", referenceDTO);
        Reference reference = referenceMapper.toEntity(referenceDTO);
        reference = referenceRepository.save(reference);
        return referenceMapper.toDto(reference);
    }

    @Override
    public ReferenceDTO update(ReferenceDTO referenceDTO) {
        log.debug("Request to update Reference : {}", referenceDTO);
        Reference reference = referenceMapper.toEntity(referenceDTO);
        reference = referenceRepository.save(reference);
        return referenceMapper.toDto(reference);
    }

    @Override
    public Optional<ReferenceDTO> partialUpdate(ReferenceDTO referenceDTO) {
        log.debug("Request to partially update Reference : {}", referenceDTO);

        return referenceRepository
            .findById(referenceDTO.getId())
            .map(existingReference -> {
                referenceMapper.partialUpdate(existingReference, referenceDTO);

                return existingReference;
            })
            .map(referenceRepository::save)
            .map(referenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReferenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all References");
        return referenceRepository.findAll(pageable).map(referenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReferenceDTO> findOne(Long id) {
        log.debug("Request to get Reference : {}", id);
        return referenceRepository.findById(id).map(referenceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Reference : {}", id);
        referenceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReferenceDTO> searchBytitle(String title, Pageable pageable) {
        log.debug("Request to search Roles by name : {}", title);
        return referenceRepository.findByTitleContainingIgnoreCase(title, pageable).map(referenceMapper::toDto);
    }
}
