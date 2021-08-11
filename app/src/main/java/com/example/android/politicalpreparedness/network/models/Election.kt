package com.example.android.politicalpreparedness.network.models

import androidx.room.*
import com.example.android.politicalpreparedness.network.NetworkElectionsContainer
import com.squareup.moshi.*
import java.util.*

@Entity(tableName = "election_table")
data class Election(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name")val name: String,
        @ColumnInfo(name = "electionDay")val electionDay: Date,
        @Embedded(prefix = "division_") @Json(name="ocdDivisionId") val division: Division
)


fun List<Election>.asDomainModel(): List<Election> {
        return map {
                Election(
                        id=it.id,
                        name = it.name,
                        electionDay = it.electionDay,
                        division = it.division
                )
        }
}

fun List<Election>.asDatabaseModel(): List<Election> {
        return map {
                Election (
                        id=it.id,
                        name = it.name,
                        electionDay = it.electionDay,
                        division = it.division
                )
        }
}