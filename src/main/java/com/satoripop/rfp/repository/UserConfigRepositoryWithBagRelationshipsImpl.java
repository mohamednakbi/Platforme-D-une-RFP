package com.satoripop.rfp.repository;

import com.satoripop.rfp.domain.UserConfig;
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
public class UserConfigRepositoryWithBagRelationshipsImpl implements UserConfigRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String USERCONFIGS_PARAMETER = "userConfigs";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserConfig> fetchBagRelationships(Optional<UserConfig> userConfig) {
        return userConfig.map(this::fetchTechnologys);
    }

    @Override
    public Page<UserConfig> fetchBagRelationships(Page<UserConfig> userConfigs) {
        return new PageImpl<>(fetchBagRelationships(userConfigs.getContent()), userConfigs.getPageable(), userConfigs.getTotalElements());
    }

    @Override
    public List<UserConfig> fetchBagRelationships(List<UserConfig> userConfigs) {
        return Optional.of(userConfigs).map(this::fetchTechnologys).orElse(Collections.emptyList());
    }

    UserConfig fetchTechnologys(UserConfig result) {
        return entityManager
            .createQuery(
                "select userConfig from UserConfig userConfig left join fetch userConfig.technologies where userConfig.id = :id",
                UserConfig.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<UserConfig> fetchTechnologys(List<UserConfig> userConfigs) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, userConfigs.size()).forEach(index -> order.put(userConfigs.get(index).getId(), index));
        List<UserConfig> result = entityManager
            .createQuery(
                "select userConfig from UserConfig userConfig left join fetch userConfig.technologies where userConfig in :userConfigs",
                UserConfig.class
            )
            .setParameter(USERCONFIGS_PARAMETER, userConfigs)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
