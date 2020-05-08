package com.realtyagency.tm.presentation.home

import androidx.lifecycle.MutableLiveData
import com.realtyagency.tm.app.platform.BaseViewModel
import com.realtyagency.tm.app.platform.NavigationEvent
import com.realtyagency.tm.domain.repository.RealtyRepository
import com.realtyagency.tm.presentation.realty.RealtyListFragment

class HomeViewModel(private val realtyRepository: RealtyRepository) : BaseViewModel() {

    val categories = MutableLiveData<List<String>>()

    init {
        getCategories()
    }

    private fun getCategories() {
        launch {
            realtyRepository.getCategories(::handleCategories, ::handleState)
        }
    }

    private fun handleCategories(list: List<String>) {
        categories.value = list
    }

    fun navigateToListRealty(categoryTitle: String) {
        navigate(NavigationEvent.PushFragment(RealtyListFragment.newInstance(categoryTitle)))
    }
}