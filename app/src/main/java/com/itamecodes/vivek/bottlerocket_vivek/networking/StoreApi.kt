package com.itamecodes.vivek.bottlerocket_vivek.networking

import com.itamecodes.vivek.bottlerocket_vivek.models.StoreList
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface StoreApi {

    @GET("BR_Android_CodingExam_2015_Server/stores.json")
    fun getListOfStores():Deferred<StoreList>
}