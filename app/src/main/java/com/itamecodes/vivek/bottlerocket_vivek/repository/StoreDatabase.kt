package com.itamecodes.vivek.bottlerocket_vivek.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.itamecodes.vivek.bottlerocket_vivek.models.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Store::class], version = 2)
abstract class StoreDatabase : RoomDatabase() {
    abstract fun storeDataDao(): StoreDao

    companion object {
        private var INSTANCE: StoreDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): StoreDatabase {
            val storeInstance = INSTANCE
            if (storeInstance != null) return storeInstance
            synchronized(this) {
                val instance =
                    Room.databaseBuilder(context.applicationContext, StoreDatabase::class.java, "Store_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(StoreDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }


    private class StoreDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let {
                scope.launch(Dispatchers.IO) {
                    populateDatabase(it.storeDataDao())
                }
            }
        }


        private fun populateDatabase(storeDataDao: StoreDao) {

            //storeDataDao.deleteAll() <-- Uncomment this line for quick deletion of the db on launch. Wanted to delete it ,
            // but this is can be considered as a demo for how to use room callbacks

        }
    }


}