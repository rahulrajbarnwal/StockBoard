package dev.rrb.stocks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.rrb.stocks.models.DashboardCategory
import dev.rrb.stocks.network.ApiService
import dev.rrb.stocks.network.ConfigLoader
import dev.rrb.stocks.network.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _categories = MutableStateFlow<List<DashboardCategory>>(emptyList())
    val categories: StateFlow<List<DashboardCategory>> = _categories

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _filterName = MutableStateFlow("NIFTY 500")
    val filterName: StateFlow<String> = _filterName

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        _isLoading.value = true
        _errorMessage.value = ""

        viewModelScope.launch {
            try {
                // Load config from JSON file
                val configData = ConfigLoader.loadConfig()

                if (configData != null) {
                    ApiService.loadConfigData()
                    _filterName.value = configData.filter.dinName

                    val categoryList = mutableListOf<DashboardCategory>()

                    // Create categories and fetch REAL data from API
                    configData.tabs.forEach { tab ->
                        val stocks = ApiService.fetchStocksFromApi(tab.postData.apiType)
                        categoryList.add(DashboardCategory(
                            id = tab.postData.apiType,
                            name = tab.nm,
                            viewAllText = tab.va,
                            apiType = tab.postData.apiType,
                            stocks = stocks
                        ))
                    }

                    _categories.value = categoryList
                    _isLoading.value = false
                } else {
                    _errorMessage.value = "Failed to load configuration"
                    _isLoading.value = false
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Error: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun refreshData() {
        loadDashboardData()
    }
}
