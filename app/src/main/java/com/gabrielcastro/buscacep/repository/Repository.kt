package com.gabrielcastro.buscacep.repository

import com.gabrielcastro.buscacep.api.RetrofitInstance
import com.gabrielcastro.buscacep.model.Address
import retrofit2.Response

class Repository {
    suspend fun getAddress(cep: String): Response<Address> {
        return RetrofitInstance.api.getAddress(cep)
    }
}