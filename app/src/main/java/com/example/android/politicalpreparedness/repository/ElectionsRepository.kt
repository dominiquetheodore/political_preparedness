package com.example.android.politicalpreparedness.repository

import android.net.Network
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.NetworkElectionsContainer
import com.example.android.politicalpreparedness.network.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.json
import org.json.JSONObject
import retrofit2.await
import timber.log.Timber
import java.util.*

class ElectionsRepository(private val database: ElectionDatabase) {
    val upcomingElections: LiveData<List<Election>> =
        Transformations.map(database.electionDao.getElections()) {
            it.asDomainModel()
        }

    val savedElections: LiveData<List<Election>> =
        Transformations.map(database.savedElectionDao.getSavedElections()) {
            it
        }

    suspend fun refreshElections() {
        withContext(Dispatchers.IO) {
            val electionList = CivicsApi.retrofitService.getElections("AIzaSyCFEqcAr27os-jpbhV-r7qYQW5waZaZvyM").await()
           database.electionDao.insertAll(*electionList.asDatabaseModel().toTypedArray())
        }
    }

    suspend fun followElection(election: Election){
        Timber.i("following election " + election.id)
        withContext(Dispatchers.IO) {
            database.savedElectionDao.insert(listOf(election).toSaved()[0])
        }
    }

    suspend fun unfollowElection(electionId: String){
        withContext(Dispatchers.IO) {
            database.savedElectionDao.unfollow(electionId)
        }
    }
}