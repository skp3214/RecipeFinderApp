package com.example.recipefinderapp.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinderapp.data.network.MealApiClient
import kotlinx.coroutines.launch

class RecipeViewModel: ViewModel() {
    private val _state = mutableStateOf<RecipeViewState>(RecipeViewState.Loading)
    val state: State<RecipeViewState> = _state

    fun processIntent(intent: RecipeViewIntent) {
        when(intent) {
            is RecipeViewIntent.LoadRandomRecipe -> loadRandomRecipe()
            is RecipeViewIntent.SearchRecipes -> searchRecipe(intent.query)
        }
    }

    private fun loadRandomRecipe() {
        viewModelScope.launch {
            _state.value = RecipeViewState.Loading
            try {
                val recipes = MealApiClient.getRandomRecipe()
                if (recipes.isEmpty()) {
                    _state.value = RecipeViewState.Error("No recipes found")
                } else {
                    _state.value = RecipeViewState.Success(recipes)
                }
            } catch(e: Exception) {
                e.printStackTrace()
                _state.value = RecipeViewState.Error("Error: ${e.message ?: "Unknown error"}")
            }
        }
    }

    private fun searchRecipe(query: String) {
        viewModelScope.launch {
            _state.value = RecipeViewState.Loading
            try {
                val recipes = MealApiClient.getSearchedRecipe(query)
                if (recipes.isEmpty()) {
                    _state.value = RecipeViewState.Error("No recipes found for '$query'")
                } else {
                    _state.value = RecipeViewState.Success(recipes)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = RecipeViewState.Error("Error: ${e.message ?: "Unknown error"}")
            }
        }
    }

}