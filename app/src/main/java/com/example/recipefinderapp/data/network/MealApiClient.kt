package com.example.recipefinderapp.data.network

import com.example.recipefinderapp.data.model.Meal
import com.example.recipefinderapp.data.model.MealResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json

object MealApiClient {
    private val apiClient = HttpClient(CIO){
        install(ContentNegotiation) {
            json(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                isLenient = true
                coerceInputValues = true
            })
        }
    }

    suspend fun getRandomRecipe(): List<Meal> {
        val url = "https://www.themealdb.com/api/json/v1/1/random.php"
        val response = apiClient.get(url).body<MealResponse>()
        return response.meals ?: emptyList()
    }

    suspend fun getSearchedRecipe(query: String): List<Meal> {
        val url = if (query.length == 1) {
            "https://www.themealdb.com/api/json/v1/1/search.php?f=$query"
        } else {
            "https://www.themealdb.com/api/json/v1/1/search.php?s=$query"
        }
        val response = apiClient.get(url).body<MealResponse>()
        return response.meals ?: emptyList()
    }

}