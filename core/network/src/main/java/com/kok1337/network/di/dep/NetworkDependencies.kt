package com.kok1337.network.di.dep

import okhttp3.Cache

interface NetworkDependencies {
    val baseUrl: String
    val cache: Cache
}