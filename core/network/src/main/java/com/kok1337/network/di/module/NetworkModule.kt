package com.kok1337.network.di.module

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kok1337.network.di.dep.NetworkDependencies
import com.kok1337.network.di.qualifier.NetBaseUrl
import com.kok1337.network.di.qualifier.NetCache
import com.kok1337.network.di.scope.Network
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {
    @[NetBaseUrl Provides]
    fun provideBaseUrl(networkDependencies: NetworkDependencies): String =
        networkDependencies.baseUrl

    @[NetCache Provides]
    fun provideCache(networkDependencies: NetworkDependencies): Cache = networkDependencies.cache

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .create()
    }

    @Provides
    fun provideOkhttpClient(@NetCache cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .build()
    }

    @[Network Provides]
    fun provideRetrofit(
        @NetBaseUrl baseUrl: String,
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
    }
}