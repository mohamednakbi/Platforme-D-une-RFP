package com.satoripop.rfp.service.mapper;

import com.satoripop.rfp.domain.Role;
import com.satoripop.rfp.domain.Technology;
import com.satoripop.rfp.domain.UserConfig;
import com.satoripop.rfp.service.dto.RoleDTO;
import com.satoripop.rfp.service.dto.TechnologyDTO;
import com.satoripop.rfp.service.dto.UserConfigDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserConfig} and its DTO {@link UserConfigDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserConfigMapper extends EntityMapper<UserConfigDTO, UserConfig> {
    @Mapping(target = "role", source = "role", qualifiedByName = "roleId")
    @Mapping(target = "technologies", source = "technologies", qualifiedByName = "technologyIdSet")
    UserConfigDTO toDto(UserConfig s);

    @Mapping(target = "removeTechnologys", ignore = true)
    UserConfig toEntity(UserConfigDTO userConfigDTO);

    @Named("roleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RoleDTO toDtoRoleId(Role role);

    @Named("technologyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    TechnologyDTO toDtoTechnologyId(Technology technology);

    @Named("technologyIdSet")
    default Set<TechnologyDTO> toDtoTechnologyIdSet(Set<Technology> technology) {
        return technology.stream().map(this::toDtoTechnologyId).collect(Collectors.toSet());
    }
}
