package com.realtyagency.tm.domain.repository

import com.realtyagency.tm.app.platform.State
import com.realtyagency.tm.data.entities.News

interface NewsRepository {

    suspend fun getNewsDetail(id: Int, onSuccess: (News) -> Unit, onState: (State) -> Unit)
    suspend fun readNews(id: Int, onSuccess: (Unit) -> Unit, onState: (State) -> Unit)

}