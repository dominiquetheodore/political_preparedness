package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await
import timber.log.Timber

class VoterInfoViewModel(electionId: Int, division: Division, application: Application) : ViewModel() {
    private val database = ElectionDatabase.getInstance(application)

    private val electionsRepository = ElectionsRepository(database)

    //DONE: Add live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    init {
        viewModelScope.launch {
            isElectionFollowed(electionId.toString())
            getVoterInfo(electionId.toString(), division)
        }
    }


    //DONE: Add var and methods to populate voter info
    suspend fun getVoterInfo(electionId: String, division: Division){
        var address = ""
        Timber.i(division.state)
        if (division.state == ""){
            address = "dc, " + division.country
        }
        else {
            address = division.state + ", " + division.country
        }

        Timber.i("received division " + address)
        _voterInfo.value = CivicsApi.retrofitService.getVoterInfo("AIzaSyCFEqcAr27os-jpbhV-r7qYQW5waZaZvyM",
            address, electionId).await()
        Timber.i(_voterInfo.value.toString())
    }

    //DONE: Add var and methods to support loading URLs
    private val _votingLocationsUrl = MutableLiveData<String?>()
    val votingLocationsUrl: LiveData<String?>
        get() = _votingLocationsUrl

    private val _ballotInformationUrl = MutableLiveData<String?>()
    val ballotInformationUrl: LiveData<String?>
        get() = _ballotInformationUrl


    fun onVotingLocationClick() {
        _votingLocationsUrl.value =
            _voterInfo.value?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl
    }

    fun onBallotInformationClick() {
        _ballotInformationUrl.value =
            _voterInfo.value?.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl
    }

    fun onVotingLocationNavigated() {
        _votingLocationsUrl.value = null
    }

    fun onBallotInformationNavigated() {
        _ballotInformationUrl.value = null
    }


    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */
    fun followElection(election: Election){
        viewModelScope.launch {
            electionsRepository.followElection(election)
            _isElectionFollowed.postValue(true)
        }
    }

    fun unfollowElection(election: Election){
        viewModelScope.launch {
            electionsRepository.unfollowElection(election.id.toString())
            _isElectionFollowed.postValue(false)
        }
    }

    private val _isElectionFollowed = MutableLiveData<Boolean?>()
    val isElectionFollowed : LiveData<Boolean?>
        get() = _isElectionFollowed

    fun isElectionFollowed(electionId: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val isFollowed = database.savedElectionDao.exists(electionId)
                _isElectionFollowed.postValue(isFollowed)
            }
        }
    }
}