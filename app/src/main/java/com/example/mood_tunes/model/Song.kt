package com.example.mood_tunes.model

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val url: String,
    val mood: String,
    val genre: String,
    val duration: Long
)
