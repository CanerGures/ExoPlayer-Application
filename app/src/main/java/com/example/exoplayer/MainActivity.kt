package com.example.exoplayer

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.exoplayer.api.model.GetJobsList
import com.example.exoplayer.model.MainDashboardModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util

class MainActivity : AppCompatActivity() {
    private var playbackStateListener: PlaybackStateListener? = null
    private lateinit var playButton: ImageView
    private lateinit var forwardButton: ImageView
    private lateinit var backwardButton: ImageView
    private lateinit var doubleClickForward: FrameLayout
    private lateinit var doubleClickBackward: FrameLayout
    private var playerView: PlayerView? = null
    private var player: SimpleExoPlayer? = null
    lateinit var currentCity: MainDashboardModel
    lateinit var currentJobObject: GetJobsList
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playerView = findViewById(R.id.video_view)
        playbackStateListener = PlaybackStateListener()
        currentCity = intent.extras?.get("currentItem") as MainDashboardModel
        playButton = findViewById(R.id.centerPlayButton)
        forwardButton = findViewById(R.id.centerForwardButton)
        backwardButton = findViewById(R.id.centerBackwardButton)
        doubleClickForward = findViewById(R.id.doubleClickBackward)
        doubleClickBackward = findViewById(R.id.doubleClickForward)
        currentJobObject = intent.extras?.get("currentItem") as GetJobsList
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()

        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        if (player == null) {
            val trackSelector = DefaultTrackSelector(this)
            trackSelector.setParameters(
                trackSelector.buildUponParameters().setMaxVideoSizeSd()
            )
            player = SimpleExoPlayer.Builder(this)
                .setTrackSelector(trackSelector)
                .build()
        }
        if (player!!.bufferedPosition == 0L) {
            playButton.setImageResource(R.drawable.ic_pause)
        }

        playButton.setOnClickListener {
            if (player!!.isPlaying) {
                player!!.pause()
                playButton.setImageResource(R.drawable.ic_play)
            } else {
                player!!.play()
                playButton.setImageResource(R.drawable.ic_pause)
            }

        }


        forwardButton.setOnClickListener {
            val currentPosition = player!!.currentPosition
            player!!.seekTo(currentPosition + 10000)
        }
        backwardButton.setOnClickListener {
            val currentPosition = player!!.currentPosition
            player!!.seekTo(currentPosition - 10000)

        }

        playerView!!.player = player
        val mediaItem =
                MediaItem.Builder()
                        .setUri(currentJobObject.streamLink)
                .setMimeType(MimeTypes.APPLICATION_MPD)
                .build()
        player!!.addMediaItem(mediaItem)
        player!!.playWhenReady = playWhenReady
        player!!.seekTo(currentWindow, playbackPosition)
        playbackStateListener?.let { player!!.addListener(it) }
        player!!.prepare()
    }

    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            playWhenReady = player!!.playWhenReady
            playbackStateListener?.let { player!!.removeListener(it) }
            player!!.release()
            player = null
        }
    }

    private class PlaybackStateListener : Player.EventListener {
        override fun onPlaybackStateChanged(playbackState: Int) {

            when (playbackState) {
                ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
                ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING -"
                ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY     -"
                ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
                else -> "UNKNOWN_STATE             -"
            }

        }
    }

}