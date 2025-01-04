package com.satoripop.rfp.service.mapper;

import com.satoripop.rfp.domain.Role;
import com.satoripop.rfp.service.dto.RoleDTO;
import java.util.List;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Role} and its DTO {@link RoleDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {}
