package com.kok1337.feature_database_preparation.data.repository

import com.kok1337.database.annotationmapper.exception.ItemNotFoundException
import com.kok1337.database.annotationmapper.mapper.EntityRowMapper
import com.kok1337.feature_database_preparation.data.database.entity.UserIdEntity
import com.kok1337.feature_database_preparation.domain.repository.UserRepository
import org.springframework.jdbc.core.JdbcTemplate
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val jdbcTemplate: JdbcTemplate,
) : UserRepository {
    override suspend fun getUserId(): Int? {
        val query = "select * from sys_infotab"
        val entityRowMapper = EntityRowMapper.create(UserIdEntity::class.java)
        val item = jdbcTemplate.queryForObject(query, entityRowMapper)
            ?: throw ItemNotFoundException(UserIdEntity::class.java)
        return item.userId
    }

    override suspend fun saveUserId(id: Int?) {
        val query = "update sys_infotab set user_id=?"
        jdbcTemplate.update(query, id)
    }
}