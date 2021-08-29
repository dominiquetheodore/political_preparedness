package com.example.android.politicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.asDatabaseModel
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await
import timber.log.Timber

class RepresentativeViewModel: ViewModel() {

    //DONE: Establish live data for representatives and address
    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    fun getRepresentatives() {
        Timber.i("getting representatives")
        Timber.i("using address " + _address.value.toString())
        val add_str = (_address.value?.line1 ?:"") + ", " + (_address.value?.line2 ?:"") + ", " + (_address.value?.city ?:"") + ", " + (_address.value?.zip ?:"")
        viewModelScope.launch {
            val (offices, officials) = CivicsApi.retrofitService.getRepresentatives("AIzaSyCFEqcAr27os-jpbhV-r7qYQW5waZaZvyM", add_str)
            _representatives.value = offices.flatMap { office ->
                office.getRepresentatives(officials)
            }
        }
    }

    init {
        _address.value = Address("", "", "Washington", "District of Columbia", "")
        getRepresentatives()
    }

    //TODO: Create function to fetch representatives from API from a provided address
    var url = "https://www.googleapis.com/civicinfo/v2/representatives"

      /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location
    fun getAddressFromLocation(address: Address){
        _address.value = address

    }
    //TODO: Create function to get address from individual fields
    fun getAddressFromSearch(line1: String, line2: String, city: String, zip: String, state: String){
        _address.value = Address(line1, line2, city, state, zip)
    }
}
