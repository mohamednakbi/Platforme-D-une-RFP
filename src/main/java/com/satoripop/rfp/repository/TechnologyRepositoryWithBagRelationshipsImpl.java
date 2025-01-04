package com.satoripop.rfp.repository;

import com.satoripop.rfp.domain.Technology;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class TechnologyRepositoryWithBagRelationshipsImpl implements TechnologyRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String TECHNOLOGIES_PARAMETER = "technologies";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Technology> fetchBagRelationships(Optional<Technology> technology) {
        return technology.map(this::fetchUserConfigs);
    }

    @Override
    public Page<Technology> fetchBagRelationships(Page<Technology> technologies) {
        return new PageImpl<>(
            fetchBagRelationships(technologies.getContent()),
            technologies.getPageable(),
            technologies.getTotalElements()
        );
    }

    @Override
    public List<Technology> fetchBagRelationships(List<Technology> technologies) {
        return Optional.of(technologies).map(this::fetchUserConfigs).orElse(Collections.emptyList());
    }

    Technology fetchUserConfigs(Technology result) {
        return entityManager
            .createQuery(
                "select technology from Technology technology left join fetch technology.userConfigs where technology.id = :id",
                Technology.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Technology> fetchUserConfigs(List<Technology> technologies) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, technologies.size()).forEach(index -> order.put(technologies.get(index).getId(), index));
        List<Technology> result = entityManager
            .createQuery(
                "select technology from Technology technology left join fetch technology.userConfigs where technology in :technologies",
                Technology.class
            )
            .setParameter(TECHNOLOGIES_PARAMETER, technologies)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
