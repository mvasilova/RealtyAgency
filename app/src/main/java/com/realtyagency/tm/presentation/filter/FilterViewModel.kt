package com.realtyagency.tm.presentation.filter

import androidx.lifecycle.MutableLiveData
import com.realtyagency.tm.app.extensions.refresh
import com.realtyagency.tm.app.platform.BaseViewModel
import com.realtyagency.tm.app.platform.NavigationEvent
import com.realtyagency.tm.app.utils.FilterRequestEvent
import com.realtyagency.tm.data.entities.FilterData
import com.realtyagency.tm.data.enums.FilterSort
import com.realtyagency.tm.domain.repository.FilterRepository
import com.realtyagency.tm.presentation.filter.delegates.RealtyTypeItems
import org.greenrobot.eventbus.EventBus

class FilterViewModel(
    private val category: String?,
    filterData: FilterData,
    private val filterRepository: FilterRepository
) :
    BaseViewModel() {

    val userFilters = MutableLiveData(filterData)
    val availableFilters = MutableLiveData<FilterData>()

    init {
        getAvailableFilters()
    }

    private fun getAvailableFilters() {
        launch {
            filterRepository.getAllFilters(category, ::handleRequestType, ::handleState)
        }
    }

    private fun handleRequestType(data: FilterData) {
        availableFilters.value = data
    }

    fun setRealtySort(filterSort: FilterSort) {
        userFilters.value?.realtySort = filterSort
        userFilters.refresh()
    }

    fun setRealtyCost(pair: Pair<Long, Long>) {
        userFilters.value?.realtyCost = pair
    }

    fun setRealtyTypeSell(flag: Boolean) {
        val second = userFilters.value?.realtyTypeSell?.second ?: false
        userFilters.value?.realtyTypeSell = flag to second
    }

    fun setRealtyTypeRent(flag: Boolean) {
        val first = userFilters.value?.realtyTypeSell?.first ?: false
        userFilters.value?.realtyTypeSell = first to flag
    }

    fun changeFilterTypes(item: RealtyTypeItems) {
        when {
            availableFilters.value?.advertType?.contains(item.realtyTypeName) == true -> {
                val list = mutableListOf<String?>()
                list.addAll(userFilters.value?.advertType.orEmpty())
                userFilters.value?.advertType = updateUserFilterList(list, item)
                userFilters.refresh()
            }
            availableFilters.value?.realtyType?.contains(item.realtyTypeName) == true -> {
                val list = mutableListOf<String?>()
                list.addAll(userFilters.value?.realtyType.orEmpty())
                userFilters.value?.realtyType = updateUserFilterList(list, item)
                userFilters.refresh()
            }
            availableFilters.value?.realtyRepair?.contains(item.realtyTypeName) == true -> {
                val list = mutableListOf<String?>()
                list.addAll(userFilters.value?.realtyRepair.orEmpty())
                userFilters.value?.realtyRepair = updateUserFilterList(list, item)
                userFilters.refresh()
            }
        }
    }

    private fun updateUserFilterList(
        list: MutableList<String?>,
        item: RealtyTypeItems
    ): MutableList<String?> {
        if (list.contains(item.realtyTypeName)) {
            list.remove(item.realtyTypeName)
        } else {
            list.add(item.realtyTypeName)
        }
        return list
    }


    fun navigateToRequest() {
        EventBus.getDefault().postSticky(
            FilterRequestEvent(
                userFilters.value ?: FilterData()
            )
        )
        navigate(NavigationEvent.Exit)
    }

}