package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class NetworkElectionsContainer(val elections: List<NetworkElection>)

@JsonClass(generateAdapter = true)
data class NetworkElection(
    val id: Int,
    val name: String,
    val electionDay: Date,
    val ocdDivisionId: Division)

/**
 * Convert Network results to database objects
 */
/*
fun List<Election>.asDatabaseModel(): List<Election> {
    return map {
        Election (
            id = it.id.toInt(),
            name = it.name,
            electionDay = it.electionDay
        )
    }
}*/