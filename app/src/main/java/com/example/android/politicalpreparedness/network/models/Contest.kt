package com.example.android.politicalpreparedness.network.models

data class Contest (
    val type: String,
    val office: String? = null,
    val candidates: List<Candidate>? = null
)