package org.d3if0110.miniproject.model

data class OpStatus(
    var status: String,
    var message: String?,
    var data: List<Art> = emptyList()
)