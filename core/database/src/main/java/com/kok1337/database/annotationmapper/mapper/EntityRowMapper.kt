package com.kok1337.database.annotationmapper.mapper

import org.springframework.jdbc.core.RowMapper

object EntityRowMapper {
    fun <E> create(clazz: Class<E>): RowMapper<E> {
        val entityAnnotationRowMapper = EntityAnnotationRowMapper(clazz)::map
        return RowMapper<E> { resultSet, _ -> entityAnnotationRowMapper.invoke(resultSet) }
    }
}