package com.satoripop.rfp.service.mapper;

import com.satoripop.rfp.domain.Reference;
import com.satoripop.rfp.domain.UserConfig;
import com.satoripop.rfp.service.dto.ReferenceDTO;
import com.satoripop.rfp.service.dto.UserConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reference} and its DTO {@link ReferenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReferenceMapper extends EntityMapper<ReferenceDTO, Reference> {
    @Mapping(target = "userConfig", source = "userConfig", qualifiedByName = "userConfigId")
    ReferenceDTO toDto(Reference s);

    @Named("userConfigId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    UserConfigDTO toDtoUserConfigId(UserConfig userConfig);
}
