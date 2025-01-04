package com.satoripop.rfp.service.mapper;

import com.satoripop.rfp.domain.Document;
import com.satoripop.rfp.domain.UserConfig;
import com.satoripop.rfp.service.dto.DocumentDTO;
import com.satoripop.rfp.service.dto.UserConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Document} and its DTO {@link DocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentMapper extends EntityMapper<DocumentDTO, Document> {
    @Mapping(target = "userConfig", source = "userConfig", qualifiedByName = "userConfigId")
    DocumentDTO toDto(Document s);

    @Named("userConfigId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    UserConfigDTO toDtoUserConfigId(UserConfig userConfig);
}
