package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch
import android.util.Log

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(application: Application): ViewModel() {
    private val database = getInstance(application)

    private val electionsRepository = ElectionsRepository(database)

    private val _election = MutableLiveData<String>()
    val election: LiveData<String> get() = _election

    init {
        viewModelScope.launch {
           electionsRepository.refreshElections()
        }
    }
    //DONE: Create live data val for upcoming elections
    val upcomingElections = electionsRepository.upcomingElections

    //DONE: Create live data val for saved elections
    val savedElections = electionsRepository.savedElections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info
    private val _navigateToElection = MutableLiveData<Election?>()
    val navigateToElection : LiveData<Election?>
        get() = _navigateToElection

    fun onElectionClicked(election: Election) {
        _navigateToElection.value = election
    }

    fun onElectionNavigated() {
        _navigateToElection.value = null
    }
}