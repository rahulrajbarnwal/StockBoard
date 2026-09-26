package dev.rrb.stocks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.rrb.stocks.models.DashboardCategory
import dev.rrb.stocks.models.DefaultPostData
import dev.rrb.stocks.models.FilterCategory
import dev.rrb.stocks.models.FilterOption
import dev.rrb.stocks.models.StockData
import dev.rrb.stocks.network.ApiResponse
import dev.rrb.stocks.network.ApiService
import dev.rrb.stocks.network.ConfigLoader
import dev.rrb.stocks.network.FilterApiImpl
import dev.rrb.stocks.utils.Logger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
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

    private val _filterOptions = MutableStateFlow<Map<String, List<FilterOption>>>(emptyMap())
    val filterOptions: StateFlow<Map<String, List<FilterOption>>> = _filterOptions

    private val _currentPostData = MutableStateFlow<DefaultPostData?>(null)
    val currentPostData: StateFlow<DefaultPostData?> = _currentPostData

    private val _filterType = MutableStateFlow("index")
    val filterType: StateFlow<String> = _filterType

    private val _filterId = MutableStateFlow("2371")
    val filterId: StateFlow<String> = _filterId

    private var isFilterSelectedByUser = false
    private var dashboardJob: Job? = null
    private var filterJob: Job? = null

    init {
        Logger.debug("HomeViewModel", "Init called")
        // ✅ Observe API responses
        viewModelScope.launch {
            ApiService.getApiResponseFlow().collect { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        Logger.debug("HomeViewModel", "API Loading...")
                        _isLoading.value = true
                    }
                    is ApiResponse.Success -> {
                        Logger.debug("HomeViewModel", "API Success: ${response.stocks.size} stocks")
                    }
                    is ApiResponse.Error -> {
                        Logger.error("HomeViewModel", "API Error: ${response.message}")
                        _errorMessage.value = response.message
                    }
                }
            }
        }
        loadFilterOptions()
        loadDashboardData()
    }


    fun loadFilterOptions() {
        if (filterJob?.isActive == true) return
        Logger.debug("HomeViewModel", "loadFilterOptions called")

        filterJob = viewModelScope.launch {
            try {
                Logger.debug("HomeViewModel", "Fetching filter options from API...")
                val filterResponse = FilterApiImpl.getFilterOptions()

                if (filterResponse != null) {
                    val filterOpts = mutableMapOf<String, List<FilterOption>>()

                    filterResponse.keyIndices?.toOptions()?.let { filterOpts["Key Indices"] = it }
                    filterResponse.sectoralIndices?.toOptions()?.let { filterOpts["Sectoral Indices"] = it }
                    filterResponse.otherIndices?.toOptions()?.let { filterOpts["Other Indices"] = it }
                    filterResponse.marketcap?.toOptions()?.let { filterOpts["Market Cap"] = it }

                    filterResponse.all?.let { category ->
                        filterOpts["All Stocks"] = listOf(
                            FilterOption(name = category.name ?: "All Stocks", indexId = "0", exchange = "NSE")
                        )
                    }

                    _filterOptions.value = filterOpts
                    Logger.debug("HomeViewModel", "Filter options loaded: ${filterOpts.size} categories")
                } else {
                    Logger.error("HomeViewModel", "Filter response is null")
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Logger.error("HomeViewModel", "loadFilterOptions error", e)
            }
        }
    }

    // Merges NSE + BSE items tagged with their exchange; null when the category is empty
    private fun FilterCategory.toOptions(): List<FilterOption>? {
        val options = nse.orEmpty().map { it.copy(exchange = "NSE") } +
            bse.orEmpty().map { it.copy(exchange = "BSE") }
        return options.ifEmpty { null }
    }

    fun loadDashboardData() {
        Logger.debug("HomeViewModel", "loadDashboardData called with filterType=${_filterType.value}, filterId=${_filterId.value}")
        _isLoading.value = true
        _errorMessage.value = ""

        // Cancel any in-flight load so a stale filter's result can't overwrite the new one
        dashboardJob?.cancel()
        dashboardJob = viewModelScope.launch {
            try {
                Logger.debug("HomeViewModel", "Loading config...")
                val configData = ConfigLoader.loadConfig()
                Logger.debug("HomeViewModel", "Config loaded: ${configData != null}")

                if (configData != null) {
                    ApiService.loadConfigData()

                    // Only apply the config default until the user picks a filter
                    if (!isFilterSelectedByUser) {
                        _filterName.value = configData.filter?.dinName?.takeIf { it.isNotBlank() } ?: "NIFTY 500"
                    }
                    _currentPostData.value = configData.defaultPostData

                    val categoryList = mutableListOf<DashboardCategory>()
                    Logger.debug("HomeViewModel", "Total tabs: ${configData.tabs.size}")

                    configData.tabs.forEach { tab ->
                        try {
                            Logger.debug("HomeViewModel", "Fetching tab - ${tab.nm} (filter: ${_filterType.value}/${_filterId.value})")
                            val stocks = ApiService.fetchStocksFromApi(
                                apiType = tab.apiType,
                                filterType = _filterType.value,
                                filterId = _filterId.value
                            )
                            Logger.debug("HomeViewModel", "Got ${stocks.size} stocks for ${tab.nm}")

                            categoryList.add(DashboardCategory(
                                id = tab.apiType,
                                name = tab.nm,
                                viewAllText = tab.va,
                                apiType = tab.apiType,
                                stocks = stocks
                            ))
                        } catch (e: CancellationException) {
                            throw e
                        } catch (tabException: Exception) {
                            Logger.error("HomeViewModel", "Error fetching tab ${tab.nm}", tabException)
                        }
                    }

                    _categories.value = categoryList
                    Logger.debug("HomeViewModel", "Total categories: ${categoryList.size}")
                    _isLoading.value = false
                } else {
                    Logger.error("HomeViewModel", "Config is null")
                    _errorMessage.value = "Failed to load configuration"
                    _isLoading.value = false
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Logger.error("HomeViewModel", "loadDashboardData error", e)
                e.printStackTrace()
                _errorMessage.value = "Error: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun updateFilter(filterOption: FilterOption, filterType: String) {
        Logger.debug("HomeViewModel", "======= updateFilter called =======")
        Logger.debug("HomeViewModel", "filterOption.name: ${filterOption.name}")
        Logger.debug("HomeViewModel", "filterOption.indexId: ${filterOption.indexId}")
        Logger.debug("HomeViewModel", "filterType: $filterType")

        isFilterSelectedByUser = true
        _filterName.value = filterOption.name ?: "Filter"
        _filterType.value = filterType
        _filterId.value = filterOption.indexId ?: ""

        Logger.debug("HomeViewModel", "Updated _filterName to: ${_filterName.value}")
        Logger.debug("HomeViewModel", "Updated _filterType to: ${_filterType.value}")
        Logger.debug("HomeViewModel", "Updated _filterId to: ${_filterId.value}")

        loadDashboardData()
    }

    fun refreshData() {
        Logger.debug("HomeViewModel", "refreshData called")
        if (_filterOptions.value.isEmpty()) loadFilterOptions()
        loadDashboardData()
    }
}