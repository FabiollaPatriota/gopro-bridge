package com.fabilea.goprobridge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabilea.goprobridge.network.GoProApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val api = GoProApi()

    private val _result = MutableStateFlow("Pronto")
    val result = _result.asStateFlow()

    fun status() {
        viewModelScope.launch {
            _result.value = api.status()
        }
    }

    fun takePhoto() {
        viewModelScope.launch {
            api.setPhotoMode()
            _result.value = api.takePhoto()
        }
    }

    fun startVideo() {
        viewModelScope.launch {
            api.setVideoMode()
            _result.value = api.startVideo()
        }
    }

    fun stopVideo() {
        viewModelScope.launch {
            _result.value = api.stopVideo()
        }
    }
}
