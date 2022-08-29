package com.innovaocean.stockmarketstudy.presentation.companyListings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.innovaocean.stockmarketstudy.data.repository.StockRepository
import com.innovaocean.stockmarketstudy.domain.model.CompanyListing
import com.innovaocean.stockmarketstudy.util.Result
import com.innovaocean.stockmarketstudy.util.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListingsViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {

    private var searchJob: Job? = null

    private val companies: Flow<Result<List<CompanyListing>>> = getCompanyLists()

    val uiState: StateFlow<CompanyListUiState> = combine(
        companies
    ) { companiesResult ->
        when (companiesResult[0]) {
            is Result.Success -> {
                val companiesList = companiesResult[0] as Result.Success
                CompanyListUiState.Loaded(
                    companies = companiesList.data
                )
            }
            is Result.Error -> CompanyListUiState.Error
            Result.Loading -> {
                CompanyListUiState.Loading
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CompanyListUiState.Loading
    )

    fun onRefresh() {
        getCompanyLists(fetchFromRemote = true)
    }

    fun onSearchQueryChanged(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            getCompanyLists(query = query)
        }
    }

    private fun getCompanyLists(
        query: String = "", fetchFromRemote: Boolean = false
    ): Flow<Result<List<CompanyListing>>> {
       return repository.getCompanyListings(fetchFromRemote, query)
            .catch {
                //todo
            }
    }
}