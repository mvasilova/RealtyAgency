package com.realtyagency.tm.presentation

import androidx.lifecycle.MutableLiveData
import com.realtyagency.tm.app.platform.BaseViewModel
import com.realtyagency.tm.app.platform.Event
import com.realtyagency.tm.app.platform.State
import com.realtyagency.tm.data.entities.response.FirebaseDataBaseResponse
import com.realtyagency.tm.domain.repository.RealtyRepository

class SplashViewModel(private val realtyRepository: RealtyRepository) : BaseViewModel() {

    val eventLoader = MutableLiveData<Event<Int>>()

    init {
        getDataBase()
    }

    private fun getDataBase() {
        launch {
            realtyRepository.getFirebaseDataBase(::handleData, ::handleErrorBase)
        }
    }

    private fun handleData(firebaseDataBaseResponse: FirebaseDataBaseResponse) {
        launch {
            firebaseDataBaseResponse.realty?.let {
                realtyRepository.setLocalDataBase(it, {
                    eventLoader.value = Event(1)
                }, ::handleErrorBase)
            }
        }
    }

    private fun handleErrorBase(state: State) {
        if (state is State.Error) {
            launch {
                realtyRepository.getCountLocalDataBase({
                    if (it > 0) {
                        eventLoader.value = Event(1)
                    } else {
                        handleError(state)
                    }
                }, ::handleError)
            }
        }
    }
}