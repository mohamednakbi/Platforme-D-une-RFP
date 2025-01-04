package com.satoripop.rfp.repository;

import com.satoripop.rfp.domain.Technology;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TechnologyRepositoryWithBagRelationships {
    Optional<Technology> fetchBagRelationships(Optional<Technology> technology);

    List<Technology> fetchBagRelationships(List<Technology> technologies);

    Page<Technology> fetchBagRelationships(Page<Technology> technologies);
}
