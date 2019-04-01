package com.itamecodes.vivek.bottlerocket_vivek.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.itamecodes.vivek.bottlerocket_vivek.models.Store

class StoreRepository(private val storeDao: StoreDao){

    suspend fun insert(storeList:List<Store>){
        storeDao.insertAllStores(*storeList.toTypedArray())
    }

    suspend fun getStores():List<Store>{
        return storeDao.getStores()
    }
}