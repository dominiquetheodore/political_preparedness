package com.example.android.politicalpreparedness.repository

import android.net.Network
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.NetworkElectionsContainer
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.json
import org.json.JSONObject
import retrofit2.await
import java.util.*

class ElectionsRepository(private val database: ElectionDatabase) {
    /*
    val upcomingElections: LiveData<List<Election>> =
        Transformations.map(database.electionDao.getElections()) {
            it.asDomainModel()
        }*/

    suspend fun refreshElections() {
        Log.i("CivicsApiService", "inside getElections")
        withContext(Dispatchers.IO) {
            val electionList = CivicsApi.retrofitService.getElections("AIzaSyCFEqcAr27os-jpbhV-r7qYQW5waZaZvyM").await()
            Log.i("ElectionsRepository", electionList.toString())
            Log.i("ElectionsRepository", "${electionList::class.simpleName}")
            val elections = electionList.asDatabaseModel()
            Log.i("ElectionsRepository", "${elections::class.simpleName}")
           database.electionDao.insertAll(*electionList.asDatabaseModel().toTypedArray())
        }
    }
}