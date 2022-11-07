package com.kok1337.feature_database_preparation.data.database.entity

import com.kok1337.database.annotationmapper.annotation.Column
import com.kok1337.database.annotationmapper.annotation.Entity

@Entity
class UserIdEntity {
    @Column("user_id") var userId: Int? = null
}