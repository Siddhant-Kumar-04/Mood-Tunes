package com.example.mood_tunes.service

import com.example.mood_tunes.model.Song
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicApi {
    @GET("playlist")
    suspend fun getMoodPlaylist(@Query("mood") mood: String): List<Song>
}

class MusicService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://your-music-api.com/") // Replace with your actual API endpoint
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(MusicApi::class.java)

    // For now, return mock data. In production, this would call the actual API
    fun getMoodBasedPlaylist(mood: String, callback: (List<Song>) -> Unit) {
        val mockSongs = when (mood.lowercase()) {
            "happy" -> listOf(
                Song("1", "Jazz in Paris", "Media Right Productions", 
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3", "Happy", "Jazz", 242000),
                Song("2", "The Messenger", "Silent Partner",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3", "Happy", "Rock", 289000),
                Song("3", "Talkies", "Jason Shaw",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3", "Happy", "Jazz", 290000),
                Song("4", "On the Bach", "Jingle Punks",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3", "Happy", "Classical", 285000),
                Song("5", "Hey Sailor", "Letter Box",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3", "Happy", "Rock", 248000)
            )
            "sad" -> listOf(
                Song("6", "Longing", "David Szesztay",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3", "Sad", "Ambient", 285000),
                Song("7", "Melancholy", "Audionautix",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3", "Sad", "Classical", 229000),
                Song("8", "Moonlight Sonata", "Beethoven",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3", "Sad", "Classical", 244000),
                Song("9", "Lost Journey", "Jingle Punks",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-9.mp3", "Sad", "Ambient", 260000),
                Song("10", "Night Walk", "Kevin MacLeod",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-10.mp3", "Sad", "Ambient", 232000)
            )
            "angry" -> listOf(
                Song("11", "Thunderstorm", "Kevin MacLeod",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-11.mp3", "Angry", "Rock", 167000),
                Song("12", "Awakening", "Silent Partner",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-12.mp3", "Angry", "Rock", 189000),
                Song("13", "Motorcycle", "Jingle Punks",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-13.mp3", "Angry", "Rock", 292000),
                Song("14", "Rising Sun", "Silent Partner",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-14.mp3", "Angry", "Rock", 257000),
                Song("15", "Renegade Jubilee", "Jeremy Blake",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-15.mp3", "Angry", "Rock", 244000)
            )
            "chill" -> listOf(
                Song("16", "No Frills Salsa", "Kevin MacLeod",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-16.mp3", "Chill", "Latin", 201000),
                Song("17", "Peaceful Garden", "Kevin MacLeod",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-17.mp3", "Chill", "Ambient", 184000),
                Song("18", "Relaxing Drive", "Kevin MacLeod",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3", "Chill", "Jazz", 293000),
                Song("19", "Romance", "Silent Partner",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3", "Chill", "Classical", 268000),
                Song("20", "Smooth Sailing", "Audionautix",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3", "Chill", "Jazz", 245000)
            )
            "energetic" -> listOf(
                Song("21", "Dance Party", "Kevin MacLeod",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3", "Energetic", "Electronic", 270000),
                Song("22", "Energy", "Jingle Punks",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3", "Energetic", "Electronic", 258000),
                Song("23", "Funk Down", "Silent Partner",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3", "Energetic", "Funk", 292000),
                Song("24", "Pop Dance", "Silent Partner",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3", "Energetic", "Pop", 281000),
                Song("25", "Groove Party", "Kevin MacLeod",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3", "Energetic", "Electronic", 248000)
            )
            else -> emptyList()
        }
        callback(mockSongs)
    }
}
