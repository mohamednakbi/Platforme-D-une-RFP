package com.satoripop.rfp.service.mapper;

import com.satoripop.rfp.domain.CV;
import com.satoripop.rfp.domain.UserConfig;
import com.satoripop.rfp.service.dto.CVDTO;
import com.satoripop.rfp.service.dto.UserConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CV} and its DTO {@link CVDTO}.
 */
@Mapper(componentModel = "spring")
public interface CVMapper extends EntityMapper<CVDTO, CV> {
    @Mapping(target = "userConfig", source = "userConfig", qualifiedByName = "userConfigUsername")
    CVDTO toDto(CV s);

    @Named("userConfigUsername")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    UserConfigDTO toDtoUserConfigUsername(UserConfig userConfig);
}
