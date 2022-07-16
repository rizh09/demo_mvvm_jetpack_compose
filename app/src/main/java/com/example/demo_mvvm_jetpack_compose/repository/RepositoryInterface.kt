package com.example.demo_mvvm_jetpack_compose.repository

interface RepositoryInterface {
    suspend fun refreshLocalDataByAPICall()
}