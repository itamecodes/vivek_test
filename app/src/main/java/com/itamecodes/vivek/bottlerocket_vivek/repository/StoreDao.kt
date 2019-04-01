package com.itamecodes.vivek.bottlerocket_vivek.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.itamecodes.vivek.bottlerocket_vivek.models.Store

@Dao
interface StoreDao{

    @Query("SELECT * FROM store_table")
    fun getStores():List<Store>

    @Insert(onConflict = REPLACE)
    fun insertAllStores(vararg store:Store)

    @Query("DELETE from store_table")
    fun deleteAll()
}