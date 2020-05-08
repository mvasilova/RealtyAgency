package com.realtyagency.tm.data.entities

import java.io.Serializable

data class FilterData(
    var requestOrder: String? = null,
    var requestCriticality: List<String>? = null,
    var requestType: List<Int>? = null
) : Serializable