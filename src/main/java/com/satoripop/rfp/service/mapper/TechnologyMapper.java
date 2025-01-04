package com.satoripop.rfp.service.mapper;

import com.satoripop.rfp.domain.Technology;
import com.satoripop.rfp.domain.UserConfig;
import com.satoripop.rfp.service.dto.TechnologyDTO;
import com.satoripop.rfp.service.dto.UserConfigDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Technology} and its DTO {@link TechnologyDTO}.
 */
@Mapper(componentModel = "spring")
public interface TechnologyMapper extends EntityMapper<TechnologyDTO, Technology> {
    @Mapping(target = "userConfigs", source = "userConfigs", qualifiedByName = "userConfigIdSet")
    TechnologyDTO toDto(Technology s);

    @Mapping(target = "removeUserConfigs", ignore = true)
    Technology toEntity(TechnologyDTO technologyDTO);

    @Named("userConfigId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    UserConfigDTO toDtoUserConfigId(UserConfig userConfig);

    @Named("userConfigIdSet")
    default Set<UserConfigDTO> toDtoUserConfigIdSet(Set<UserConfig> userConfig) {
        return userConfig.stream().map(this::toDtoUserConfigId).collect(Collectors.toSet());
    }
}
