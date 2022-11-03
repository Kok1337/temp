package com.kok1337.feature_database_preparation.data.repository

import com.kok1337.feature_database_preparation.domain.repository.UserRepository
import org.springframework.jdbc.core.JdbcTemplate
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val jdbcTemplate: JdbcTemplate,
): UserRepository {
    override suspend fun getUserId(): Int? {
        TODO("Not yet implemented")
//        val query = "select * from sys_infotab"
//        val item = jdbcTemplate.queryForObject(query, userIdEntityRowMapper) ?: throw ItemNotFoundException()
//        return item.userId
    }

    override suspend fun saveUserId(id: Int?) {
        TODO("Not yet implemented")
    }
}