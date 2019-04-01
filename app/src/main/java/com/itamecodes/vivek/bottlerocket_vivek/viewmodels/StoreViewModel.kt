package com.itamecodes.vivek.bottlerocket_vivek.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.itamecodes.vivek.bottlerocket_vivek.models.Store
import com.itamecodes.vivek.bottlerocket_vivek.models.StoreList
import com.itamecodes.vivek.bottlerocket_vivek.networking.NetworkFactory
import com.itamecodes.vivek.bottlerocket_vivek.repository.StoreDatabase
import com.itamecodes.vivek.bottlerocket_vivek.repository.StoreRepository
import com.itamecodes.vivek.bottlerocket_vivek.utils.ConnectionLiveData
import com.itamecodes.vivek.bottlerocket_vivek.utils.Event
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class StoreViewModel(application: Application) : AndroidViewModel(application) {

    private var mJob = SupervisorJob()
    private val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Default

    private val mScope = CoroutineScope(coroutineContext)
    val storeListLiveData = MutableLiveData<List<Store>>()
    val errorServer = MutableLiveData<Boolean>()
    var selectedStore = MutableLiveData<Store>()
    private val storeRepository: StoreRepository

    private val appContext = application
    private val _navigateToDetails = MutableLiveData<Event<Boolean>>()
    private val _shouldShowSnackbar = MutableLiveData<Boolean>()

    val navigateToDetail: LiveData<Event<Boolean>>
        get() = _navigateToDetails

    val shouldShowSnackbar:LiveData<Boolean>
        get() = _shouldShowSnackbar




    init {
        val storeDao = StoreDatabase.getDatabase(application, mScope).storeDataDao()
        storeRepository = StoreRepository(storeDao)
        val connectionLiveData = ConnectionLiveData(appContext)
        connectionLiveData.observeForever(Observer {
            showSnackBar("No Internet", it)
        })
    }

    fun getData() {
        mJob = mScope.launch {
            val result: List<Store> = storeRepository.getStores()
            val storeList: List<Store> = if (result.isEmpty() && isInternetConnected()) {
                val resultFromNetwork = NetworkFactory.storeApi.getListOfStores().await()
                insert(resultFromNetwork.stores)
                resultFromNetwork.stores
            } else {
                result
            }
            withContext(Dispatchers.Main) {
                populateResult(storeList)
            }
        }
    }

    private fun populateResult(result: List<Store>) {
        storeListLiveData.postValue(result)
    }

    fun onSelectStore(store: Store) {
        selectedStore.postValue(store)
        _navigateToDetails.value = Event(true)
    }

    fun insert(storeList: List<Store>) = mScope.launch {
        storeRepository.insert(storeList)
    }

    override fun onCleared() {
        super.onCleared()
        mJob.cancel()
    }

    private fun isInternetConnected(): Boolean {
        val cm = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

    private fun showSnackBar(message:String,connected:Boolean){
        _shouldShowSnackbar.postValue(connected)
    }

}