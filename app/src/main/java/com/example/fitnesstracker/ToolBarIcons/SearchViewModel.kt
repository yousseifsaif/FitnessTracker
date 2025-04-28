package com.example.fitnesstracker.ToolBarIcons

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.fitnesstracker.NavigationApp.apiWorkouts.ExerciseEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val dao: SearchDao) : ViewModel() {
    // Flow للتعامل مع استعلامات البحث
    private val _searchQueryFlow = MutableSharedFlow<String>()
    val searchQueryFlow = _searchQueryFlow.asSharedFlow()

    // LiveData لنتائج البحث
    private val _searchResults = MutableLiveData<List<ExerciseEntity>>()
    val searchResults: LiveData<List<ExerciseEntity>> = _searchResults

    // LiveData للبحث الحديث
    val recentSearches = dao.getRecentSearches().asLiveData()

    // LiveData لأي رسائل خطأ
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun performSearch(query: String) {
        viewModelScope.launch {
            try {
                val results = dao.searchExercises(query)
                if (results.isEmpty()) {
                    _errorMessage.postValue("No results found for '$query'")
                }
                _searchResults.postValue(results)
                Log.d("SearchVM", "Found ${results.size} results for: $query")
            } catch (e: Exception) {
                _errorMessage.postValue("Error searching: ${e.message}")
                Log.e("SearchVM", "Search error", e)
            }
        }
    }

    fun saveQuery(query: String) {
        viewModelScope.launch {
            try {
                val recentSearch = RecentSearch(query = query)
                dao.insert(recentSearch)
                _searchQueryFlow.emit(query) // الآن هذا سيعمل بشكل صحيح
                Log.d("SearchVM", "Saved query: $query")
            } catch (e: Exception) {
                _errorMessage.postValue("Error saving query: ${e.message}")
                Log.e("SearchVM", "Save query error", e)
            }
        }
    }
    fun deleteSearch(search: RecentSearch) {
        viewModelScope.launch {
            try {
                dao.deleteSearch(search)
            } catch (e: Exception) {
                _errorMessage.postValue("Failed to delete search")
            }
        }
    }
}