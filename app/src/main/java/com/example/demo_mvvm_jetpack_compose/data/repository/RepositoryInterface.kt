package com.example.demo_mvvm_jetpack_compose.data.repository

interface RepositoryInterface {
    suspend fun refreshLocalDataByAPICall()
}
