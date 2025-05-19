package com.example.mood_tunes

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mood_tunes.model.Song
import com.example.mood_tunes.service.MusicService

class MusicPlayerActivity : AppCompatActivity() {
    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var musicService: MusicService
    private lateinit var songList: List<Song>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        val mood = intent.getStringExtra("MOOD") ?: "Happy"
        findViewById<TextView>(R.id.tvMood).text = "Current Mood: $mood"

        setupPlayer()
        setupMusicService(mood)
    }

    private fun setupPlayer() {
        // Configure ExoPlayer with better buffering and retry settings
        player = ExoPlayer.Builder(this)
            .setLoadControl(
                androidx.media3.exoplayer.DefaultLoadControl.Builder()
                    .setBufferDurationsMs(
                        5000,  // Min buffer
                        15000, // Max buffer
                        1500,  // Buffer for playback
                        2000   // Buffer for rebuffering
                    ).build()
            )
            .build()

        playerView = findViewById(R.id.playerView)
        playerView.player = player
        
        // Show loading indicator
        playerView.setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)

        player.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                // Update UI with current song info
                mediaItem?.mediaId?.let { songId ->
                    songList.find { it.id == songId }?.let { song ->
                        updateSongInfo(song)
                    }
                }
            }

            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                // Log the error for debugging
                android.util.Log.e("MoodTunes", "Playback error: ${error.message}")
                android.util.Log.e("MoodTunes", "Error cause: ${error.cause}")

                // Show error to user
                val errorMessage = "Error playing the song. Trying next song..."
                android.widget.Toast.makeText(this@MusicPlayerActivity, errorMessage, android.widget.Toast.LENGTH_SHORT).show()

                // Retry current song once
                if (error.errorCode == androidx.media3.common.PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED) {
                    player.prepare()
                    player.play()
                } else {
                    // If retry fails or it's a different error, skip to next song
                    if (player.hasNextMediaItem()) {
                        player.seekToNext()
                    }
                }
            }

            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    Player.STATE_READY -> {
                        // Player is ready to play
                        playerView.hideController()
                    }
                    Player.STATE_BUFFERING -> {
                        // Show loading indicator if needed
                    }
                    Player.STATE_ENDED -> {
                        // Playlist ended
                        if (player.mediaItemCount > 0) {
                            player.seekTo(0, 0) // Loop back to first song
                        }
                    }
                }
            }
        })

        // Set repeat mode to repeat the playlist
        player.repeatMode = Player.REPEAT_MODE_ALL
    }

    private fun setupMusicService(mood: String) {
        musicService = MusicService()
        musicService.getMoodBasedPlaylist(mood) { songs ->
            songList = songs
            setupPlaylist(songs)
        }
    }

    private fun setupPlaylist(songs: List<Song>) {
        val mediaItems = songs.map { song ->
            MediaItem.Builder()
                .setMediaId(song.id)
                .setUri(song.url)
                .setMediaMetadata(
                    androidx.media3.common.MediaMetadata.Builder()
                        .setTitle(song.title)
                        .setArtist(song.artist)
                        .setGenre(song.genre)
                        .build()
                )
                .build()
        }

        player.setMediaItems(mediaItems)
        player.prepare()
        player.play()

        // Setup RecyclerView for playlist
        findViewById<RecyclerView>(R.id.rvPlaylist).apply {
            layoutManager = LinearLayoutManager(this@MusicPlayerActivity)
            adapter = PlaylistAdapter(songs) { position ->
                player.seekTo(position, 0)
                player.play()
            }
        }
    }

    private fun updateSongInfo(song: Song) {
        findViewById<TextView>(R.id.tvSongTitle).text = song.title
        findViewById<TextView>(R.id.tvArtist).text = song.artist
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}
