package com.kok1337.network.di.component

import com.kok1337.network.di.dep.NetworkDependencies
import com.kok1337.network.di.module.NetworkModule
import com.kok1337.network.di.scope.Network
import dagger.Component
import retrofit2.Retrofit

@[Network Component(
    dependencies = [NetworkDependencies::class],
    modules = [NetworkModule::class]
)]
interface NetworkComponent {
    val retrofit: Retrofit
}