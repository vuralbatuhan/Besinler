package com.example.besinprojesi.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.besinprojesi.model.Besin
import com.example.besinprojesi.roomdb.BesinDatabase
import com.example.besinprojesi.service.BesinAPIService
import com.example.besinprojesi.util.SpecialSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BesinListeViewModel(application: Application) : AndroidViewModel(application){

    val besinler = MutableLiveData<List<Besin>>()
    val besinHataMessage = MutableLiveData<Boolean>()
    val besinLoading = MutableLiveData<Boolean>()

    private val besinApiService = BesinAPIService()
    private val specialSharedPreferences = SpecialSharedPreferences(getApplication())

    private  val refreshTime = 10 * 60 * 1000 * 1000 * 1000L

    fun refreshData() {
        val saveTime = specialSharedPreferences.getTime()

        if(saveTime != null && saveTime != 0L && System.nanoTime() - saveTime < refreshTime) {
            getDataRoom()
        } else {
            getDataEthernet()
        }
    }

    fun getDataRoom() {
        besinLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val besinList = BesinDatabase(getApplication()).besinDao().getAll()
            withContext(Dispatchers.Main) {
                showBesin(besinList)
            }
        }
    }

    fun getDataEthernet() {
        besinLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val besinList =besinApiService.getData()
            withContext(Dispatchers.Main) {
                besinLoading.value = false
                saveRoom(besinList)
            }
        }
    }

    fun showBesin(besinList: List<Besin>) {
        besinler.value = besinList
        besinHataMessage.value = false
        besinLoading.value = false
    }

    fun saveRoom(besinList: List<Besin>) {
        viewModelScope.launch {
            val dao = BesinDatabase(getApplication()).besinDao()
            dao.deleteAll()
            val uuidList = dao.insertAll(*besinList.toTypedArray())
            var i = 0
            while (i < besinList.size) {
                besinList[i].uuid = uuidList[i].toInt()
                i = i + 1
            }

            showBesin(besinList)
        }
        specialSharedPreferences.saveTime(System.nanoTime())
    }

}