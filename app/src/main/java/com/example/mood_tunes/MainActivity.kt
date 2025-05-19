package com.example.mood_tunes

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startCameraActivity()
        } else {
            Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<MaterialButton>(R.id.btnCaptureMood).setOnClickListener {
            checkCameraPermission()
        }

        setupMoodCards()
    }

    private fun setupMoodCards() {
        val moods = listOf("Happy", "Sad", "Angry", "Chill", "Energetic")
        val cardIds = listOf(
            R.id.cardHappy,
            R.id.cardSad,
            R.id.cardAngry,
            R.id.cardChill,
            R.id.cardEnergetic
        )

        cardIds.forEachIndexed { index, cardId ->
            findViewById<MaterialCardView>(cardId).setOnClickListener {
                startMusicPlayer(moods[index])
            }
        }
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                startCameraActivity()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun startCameraActivity() {
        startActivity(Intent(this, CameraActivity::class.java))
    }

    private fun startMusicPlayer(mood: String) {
        val intent = Intent(this, MusicPlayerActivity::class.java).apply {
            putExtra("MOOD", mood)
        }
        startActivity(intent)
    }
}
