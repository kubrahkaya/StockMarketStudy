package com.innovaocean.stockmarketstudy.presentation.companyListings

sealed interface CompanyListingsEvent {
    object Refresh : CompanyListingsEvent
    data class OnSearchQueryChanged(val query: String) : CompanyListingsEvent
}