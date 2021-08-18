package com.gabrielcastro.buscacep.api

import com.gabrielcastro.buscacep.model.Address
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SimpleApi {
    @GET("{cep}/json/")
    suspend fun getAddress(@Path("cep") cep: String) : Response<Address>
}