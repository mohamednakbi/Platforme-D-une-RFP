package com.satoripop.rfp.service.mapper;

import com.satoripop.rfp.domain.Context;
import com.satoripop.rfp.domain.UserConfig;
import com.satoripop.rfp.service.dto.ContextDTO;
import com.satoripop.rfp.service.dto.UserConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Context} and its DTO {@link ContextDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContextMapper extends EntityMapper<ContextDTO, Context> {
    @Mapping(target = "userConfig", source = "userConfig", qualifiedByName = "userConfigId")
    ContextDTO toDto(Context s);

    @Named("userConfigId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    UserConfigDTO toDtoUserConfigId(UserConfig userConfig);
}
