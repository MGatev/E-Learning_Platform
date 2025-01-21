package course.spring.elearningplatform.dto.mapper;

import org.springframework.beans.BeanUtils;

public class EntityMapper {

    public static <T, K> T mapCreateDtoToEntity(K dto, Class<T> entityClass) {
        T entity = BeanUtils.instantiateClass(entityClass);
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static <T, K> K mapEntityToDto(T entity, Class<K> dtoClass) {
        K dto = BeanUtils.instantiateClass(dtoClass);
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
