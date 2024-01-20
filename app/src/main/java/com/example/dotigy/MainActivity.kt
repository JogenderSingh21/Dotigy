package com.example.dotigy

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    private lateinit var seekbar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val badHabits = MediaPlayer.create(this,R.raw.badhabits)

        val playbtn = findViewById<ImageButton>(R.id.playPause)
        val forward = findViewById<ImageButton>(R.id.forward)
        val backward = findViewById<ImageButton>(R.id.backward)

        seekbar = findViewById(R.id.seekBar)
        handler = Handler(Looper.getMainLooper())
        seekbar.progress = 0

        badHabits.setOnPreparedListener{
            seekbar.max = badHabits.duration
            badHabits.start()
            seekbar.progress = badHabits.currentPosition
            changeSeekbar(badHabits)
        }

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, pos: Int, changed: Boolean) {
                if(changed){
                    badHabits.seekTo(pos)
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        playbtn.setOnClickListener{
            if (!badHabits.isPlaying){
                badHabits.start()
                playbtn.setImageResource(R.drawable.baseline_pause_24)
            }
            else{
                badHabits.pause()
                playbtn.setImageResource(R.drawable.baseline_play_arrow_24)
            }
        }

        forward.setOnClickListener{
            badHabits.seekTo(badHabits.currentPosition + 5000)
            changeSeekbar(badHabits)
        }

        backward.setOnClickListener{
            badHabits.seekTo(badHabits.currentPosition - 5000)
            changeSeekbar(badHabits)
        }
    }

    private fun changeSeekbar(player: MediaPlayer) {
        seekbar.progress = player.currentPosition
        if (player.isPlaying) {
            runnable = Runnable {
                changeSeekbar(player)
            }
            handler.postDelayed(runnable, 1000)
        }
    }
}
