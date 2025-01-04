package com.satoripop.rfp.repository;

import com.satoripop.rfp.domain.UserConfig;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface UserConfigRepositoryWithBagRelationships {
    Optional<UserConfig> fetchBagRelationships(Optional<UserConfig> userConfig);

    List<UserConfig> fetchBagRelationships(List<UserConfig> userConfigs);

    Page<UserConfig> fetchBagRelationships(Page<UserConfig> userConfigs);
}
