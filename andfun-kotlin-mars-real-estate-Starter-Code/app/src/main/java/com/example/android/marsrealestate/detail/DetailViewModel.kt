package com.example.android.marsrealestate.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.marsrealestate.R
import com.example.android.marsrealestate.network.MarsProperty

class DetailViewModel(marsProperty: MarsProperty, app: Application) : AndroidViewModel(app) {

    private val _selectedProperty = MutableLiveData<MarsProperty>()
    val property: LiveData<MarsProperty> get() = _selectedProperty

    val displayPrice: LiveData<String> = Transformations.map(_selectedProperty) {
        app.applicationContext.getString(when (it.isRental) {
            true -> R.string.display_price_monthly_rental
            false -> R.string.display_price
        }, it.price)
    }

    val displayType: LiveData<String> = Transformations.map(_selectedProperty) {
        app.applicationContext.getString(R.string.display_type,
                app.applicationContext.getString(when (it.isRental) {
                    true -> R.string.type_rent
                    false -> R.string.type_sale
                }))
    }

    init {
        _selectedProperty.value = marsProperty
    }

}
