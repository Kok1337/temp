package com.kok1337.database.di.dep

interface DatabaseDependencies {
    val host: String
    val port: String
    val name: String
    val username: String
    val password: String
    val driverName: String
}