package com.gabrielcastro.buscacep

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabrielcastro.buscacep.model.Address
import com.gabrielcastro.buscacep.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {
    val myResponse: MutableLiveData<Response<Address>> = MutableLiveData()

    fun getAddress(cep: String) {
        viewModelScope.launch {
            val response = repository.getAddress(cep)
            myResponse.value = response
        }
    }
}