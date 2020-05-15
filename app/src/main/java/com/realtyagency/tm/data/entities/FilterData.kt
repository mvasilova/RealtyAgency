package com.realtyagency.tm.data.entities

import com.realtyagency.tm.data.enums.FilterSort
import java.io.Serializable

data class FilterData(
    var realtySort: FilterSort? = null,
    var realtyCost: Pair<Long, Long>? = null,
    var realtyTypeSell: Pair<Boolean, Boolean>? = null,
    var realtyType: List<String?>? = null,
    var advertType: List<String?>? = null,
    var realtyRepair: List<String?>? = null
) : Serializable