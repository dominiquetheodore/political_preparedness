package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(application: Application): ViewModel() {
    private val database = getInstance(application)

    private val electionsRepository = ElectionsRepository(database)

    private val _election = MutableLiveData<String>()
    val election: LiveData<String> get() = _election

    val currentName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    init {
        viewModelScope.launch {
           electionsRepository.refreshElections()
            currentName.value = "James"
        }
    }
    //TODO: Create live data val for upcoming elections
    // val upcomingElections = electionsRepository.upcomingElections

    /*
    val electionsList = Transformations.switchMap() {
        return electionsRepository.savedElections
    }*/

    //TODO: Create live data val for saved elections


    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

}