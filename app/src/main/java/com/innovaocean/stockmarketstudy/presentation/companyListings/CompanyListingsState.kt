package com.innovaocean.stockmarketstudy.presentation.companyListings

import com.innovaocean.stockmarketstudy.domain.model.CompanyListing

sealed interface CompanyListUiState {
    object Loading: CompanyListUiState
    object Empty: CompanyListUiState
    object Error: CompanyListUiState
    data class Loaded(val companies: List<CompanyListing>) : CompanyListUiState
}