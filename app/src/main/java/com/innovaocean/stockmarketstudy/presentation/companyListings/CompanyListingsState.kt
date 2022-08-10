package com.innovaocean.stockmarketstudy.presentation.companyListings

import com.innovaocean.stockmarketstudy.domain.model.CompanyListing

data class CompanyListingsState(
    val companies: List<CompanyListing> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = ""
)