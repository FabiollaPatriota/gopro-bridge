package com.fabilea.goprobridge.domain

data class GoProStatus(
    val connected: Boolean = false,
    val cameraSsid: String = "",
    val lastResponseCode: Int? = null,
    val lastMessage: String = "",
)
