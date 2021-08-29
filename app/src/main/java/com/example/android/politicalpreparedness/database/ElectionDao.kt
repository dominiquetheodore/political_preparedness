package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.SavedElection
import java.util.*

@Dao
interface ElectionDao {

    //TODO: Add insert query

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg elections: Election)

    //TODO: Add select all election query
    @Query("select * from election_table")
    fun getElections(): LiveData<List<Election>>

    //TODO: Add select single election query
    @Query("SELECT * FROM election_table WHERE id = :id")
    fun getElection(id: String): LiveData<List<Election>>



    //TODO: Add clear query

}

@Dao
interface SavedElectionDao {
    //COMPLETE: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(election: SavedElection)

    //COMPLETE: Add select all election query
    @Query("select * from saved_elections")
    fun getSavedElections(): LiveData<List<Election>>

    //TODO: Add select all election query
    @Query("SELECT EXISTS (SELECT 1 FROM saved_elections WHERE id = :id)")
    fun exists(id: String): Boolean

    //TODO: Add select all election query
    //COMPLETE: Add delete query
    @Query("DELETE from saved_elections WHERE id = :id")
    fun unfollow(id: String)

}