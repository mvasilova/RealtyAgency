package com.realtyagency.tm.presentation.filterrequest

import androidx.lifecycle.MutableLiveData
import com.realtyagency.tm.app.extensions.refresh
import com.realtyagency.tm.app.platform.BaseViewModel
import com.realtyagency.tm.app.platform.NavigationEvent
import com.realtyagency.tm.app.utils.FilterRequestEvent
import com.realtyagency.tm.data.entities.FilterData
import com.realtyagency.tm.data.entities.RequestType
import com.realtyagency.tm.domain.repository.RequestRepository
import com.realtyagency.tm.presentation.filterrequest.delegates.RequestTypeItems
import org.greenrobot.eventbus.EventBus

class FilterRequestViewModel(filterData: FilterData, private val requestRepository: RequestRepository) : BaseViewModel() {

    val filters = MutableLiveData(filterData)
    val requestType = MutableLiveData<List<RequestType>>()

    fun getRequestType() {
        launch {
            requestRepository.getRequestType(::handleRequestType, ::handleState)
        }
    }

    private fun handleRequestType(list: List<RequestType>) {
        requestType.value = list
    }

    fun changeTypeRequests(item: RequestTypeItems) {
        val list = mutableListOf<Int>().apply {
            filters.value?.requestType?.let { addAll(it) }
        }
        if (list.contains(item.typeId.toInt())) {
            list.remove(item.typeId.toInt())
        } else {
            list.add(item.typeId.toInt())
        }
        filters.value?.requestType = list
        filters.refresh()
    }

    fun changePriorityRequests(item: RequestTypeItems) {
        val list = mutableListOf<String>().apply {
            filters.value?.requestCriticality?.let { addAll(it) }
        }
        if (list.contains(item.typeId)) {
            list.remove(item.typeId)
        } else {
            list.add(item.typeId)
        }
        filters.value?.requestCriticality = list
        filters.refresh()
    }



    fun navigateToRequest() {
//        EventBus.getDefault().postSticky(
//            FilterRequestEvent(
//                filters.value ?: FilterData()
//            )
//        )
        navigate(NavigationEvent.Exit)
    }

}