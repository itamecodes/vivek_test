package com.itamecodes.vivek.bottlerocket_vivek.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.itamecodes.vivek.bottlerocket_vivek.models.Store
import com.itamecodes.vivek.bottlerocket_vivek.networking.NetworkFactory
import com.itamecodes.vivek.bottlerocket_vivek.repository.StoreDatabase
import com.itamecodes.vivek.bottlerocket_vivek.repository.StoreRepository
import com.itamecodes.vivek.bottlerocket_vivek.utils.ConnectionLiveData
import com.itamecodes.vivek.bottlerocket_vivek.utils.Event
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class StoreViewModel(application: Application) : AndroidViewModel(application) {

    private var mJob = SupervisorJob()
    private val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Default

    private val mScope = CoroutineScope(coroutineContext)

    private val _errorServer = MutableLiveData<Boolean>()
    private var _selectedStore = MutableLiveData<Store>()
    private val storeRepository: StoreRepository

    private val appContext = application
    private val _navigateToDetails = MutableLiveData<Event<Boolean>>()
    private val _shouldShowSnackbarNetworkConnection = MutableLiveData<Boolean>()
    private val _storeListLiveData = MutableLiveData<List<Store>>()

    val navigateToDetail: LiveData<Event<Boolean>>
        get() = _navigateToDetails

    val shouldShowSnackbarNetworkConnection: LiveData<Boolean>
        get() = _shouldShowSnackbarNetworkConnection

    val storeListLiveData: LiveData<List<Store>>
        get() = _storeListLiveData

    val selectedStore: LiveData<Store>
        get() = _selectedStore

    val errorServer: LiveData<Boolean>
        get() = _errorServer


    init {
        val storeDao = StoreDatabase.getDatabase(application, mScope).storeDataDao()
        storeRepository = StoreRepository(storeDao)
        val connectionLiveData = ConnectionLiveData(appContext)
        connectionLiveData.observeForever(Observer {
            showSnackBar(it)
        })
    }

    fun getData() {
        _errorServer.value = false
        mJob = mScope.launch {
            try {
                val result: List<Store> = storeRepository.getStores()
                val storeList: List<Store> = if (result.isEmpty() && isInternetConnected()) {
                    val resultFromNetwork = NetworkFactory.storeApi.getListOfStoresAsync().await()
                    insert(resultFromNetwork.stores)
                    resultFromNetwork.stores
                } else {
                    result
                }
                withContext(Dispatchers.Main) {
                    populateResult(storeList)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorServer.value = true
                }
            }
        }
    }

    private fun populateResult(result: List<Store>) {
        _storeListLiveData.postValue(result)
    }

    fun onSelectStore(store: Store) {
        _selectedStore.postValue(store)
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

    private fun showSnackBar(connected: Boolean) {
        _shouldShowSnackbarNetworkConnection.postValue(connected)
    }

}