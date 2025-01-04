package com.satoripop.rfp.service.impl;

import com.satoripop.rfp.domain.CV;
import com.satoripop.rfp.domain.Context;
import com.satoripop.rfp.domain.Reference;
import com.satoripop.rfp.domain.Technology;
import com.satoripop.rfp.repository.CVRepository;
import com.satoripop.rfp.repository.ContextRepository;
import com.satoripop.rfp.repository.ReferenceRepository;
import com.satoripop.rfp.repository.TechnologyRepository;
import com.satoripop.rfp.service.dto.*;
import com.satoripop.rfp.service.mapper.CVMapper;
import com.satoripop.rfp.service.mapper.ContextMapper;
import com.satoripop.rfp.service.mapper.ReferenceMapper;
import com.satoripop.rfp.service.mapper.TechnologyMapper;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ResponseService {

    private final Random random = new Random();
    private final CVRepository cvRepository;
    private final TechnologyRepository technologyRepository;
    private final ReferenceRepository referenceRepository;
    private final ContextRepository contextRepository;
    private final ReferenceMapper referenceMapper;
    private final CVMapper cvMapper;
    private final TechnologyMapper technologyMapper;
    private final ContextMapper contextMapper;

    public ResponseService(
        CVRepository cvRepository,
        TechnologyRepository technologyRepository,
        ReferenceRepository referenceRepository,
        ContextRepository contextRepository,
        ReferenceMapper referenceMapper,
        CVMapper cvMapper,
        TechnologyMapper technologyMapper,
        ContextMapper contextMapper
    ) {
        this.cvRepository = cvRepository;
        this.technologyRepository = technologyRepository;
        this.referenceRepository = referenceRepository;
        this.contextRepository = contextRepository;
        this.referenceMapper = referenceMapper;
        this.cvMapper = cvMapper;
        this.technologyMapper = technologyMapper;
        this.contextMapper = contextMapper;
    }

    public Response generateRandomResponse() {
        Response response = new Response();

        List<CV> cvs = cvRepository.findAll().stream().filter(Objects::nonNull).collect(Collectors.toList());
        List<Technology> technologies = technologyRepository.findAll().stream().filter(Objects::nonNull).collect(Collectors.toList());
        List<Reference> references = referenceRepository.findAll().stream().filter(Objects::nonNull).collect(Collectors.toList());
        List<Context> contexts = contextRepository.findAll().stream().filter(Objects::nonNull).collect(Collectors.toList());

        List<ReferenceDTO> randomReferences = getRandomElements(references, 2, 5)
            .stream()
            .map(reference -> referenceMapper.toDto(reference))
            .collect(Collectors.toList());
        response.setReferences(randomReferences);

        List<ContextDTO> randomContexts = getRandomElements(contexts, 2, 5).stream().map(contextMapper::toDto).collect(Collectors.toList());
        response.setContexts(randomContexts);

        HashMap<TechnologyDTO, Double> technologyMap = new HashMap<>();
        List<TechnologyDTO> randomTechnologyDTOs = getRandomElements(technologies, 5, 6)
            .stream()
            .map(technologyMapper::toDto)
            .collect(Collectors.toList());
        randomTechnologyDTOs.forEach(techDto -> technologyMap.put(techDto, calculateTechnologyScore(techDto)));
        response.setTechnologyMap(technologyMap);

        HashMap<CVDTO, Double> cvMap = new HashMap<>();
        List<CVDTO> randomCVDTOs = getRandomElements(cvs, 4, 10).stream().map(cvMapper::toDto).collect(Collectors.toList());
        randomCVDTOs.forEach(cvDto -> cvMap.put(cvDto, calculateCvScore(cvDto)));
        response.setCvMap(cvMap);

        return response;
    }

    private <T> List<T> getRandomElements(List<T> list, int min, int max) {
        int count = random.nextInt(max - min + 1) + min;
        Collections.shuffle(list);
        return list.stream().limit(count).collect(Collectors.toList());
    }

    private Double calculateCvScore(CVDTO cv) {
        return random.nextDouble() * 10;
    }

    private Double calculateTechnologyScore(TechnologyDTO technology) {
        return random.nextDouble() * 10;
    }
}
