package com.innovaocean.stockmarketstudy.data.mapper

import com.innovaocean.stockmarketstudy.data.local.CompanyListingEntity
import com.innovaocean.stockmarketstudy.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}