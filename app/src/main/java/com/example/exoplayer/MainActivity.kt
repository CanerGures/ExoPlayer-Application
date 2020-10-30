package com.example.exoplayer

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ddd.androidutils.DoubleClick
import com.ddd.androidutils.DoubleClickListener
import com.example.exoplayer.api.model.GetJobsList
import com.example.exoplayer.model.MainDashboardModel
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util


class MainActivity : AppCompatActivity() {
    private lateinit var playbackStateListener: Player.EventListener
    private lateinit var playButton: ImageView
    private lateinit var forwardButton: ImageView
    private lateinit var backwardButton: ImageView
    private lateinit var closeButton: ImageView
    private lateinit var doubleClickForward: FrameLayout
    private lateinit var doubleClickBackward: FrameLayout
    private var playerView: PlayerView? = null
    private var player: SimpleExoPlayer? = null
    lateinit var currentCity: MainDashboardModel
    lateinit var currentJobObject: GetJobsList
    private var currentXmlLink: Any? = null

    //private val player: SimpleExoPlayer by lazy { SimpleExoPlayer.Builder(this).build()}
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playerView = findViewById(R.id.video_view)
        playbackStateListener = PlaybackStateListener()
        playButton = findViewById(R.id.centerPlayButton)
        forwardButton = findViewById(R.id.centerForwardButton)
        backwardButton = findViewById(R.id.centerBackwardButton)
        doubleClickForward = findViewById(R.id.doubleClickForward)
        doubleClickBackward = findViewById(R.id.doubleClickBackward)
        closeButton = findViewById(R.id.closeButton)
        supportActionBar?.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val temp = intent.extras?.get("currentItem") as GetJobsList?
        if (temp != null) {
            currentJobObject = temp
        }

        closeButton.setOnClickListener {
            this@MainActivity.finish()
        }

    }


    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            currentXmlLink = intent.extras?.get("currentLink")
            if (currentXmlLink != null) {
                initializePlayerXml(currentXmlLink as String)
            } else {
                initializePlayer()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (Util.SDK_INT <= 23 || player == null) {
            if (currentXmlLink != null) {
                initializePlayerXml(currentXmlLink as String)
            } else {
                initializePlayer()
            }

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

    override fun onBackPressed() {
        super.onBackPressed()
        player!!.stop()
        releasePlayer()


    }



    private fun initializePlayerXml(currentXmlLink: String) {
        if (player == null) {
            val trackSelector = DefaultTrackSelector(this)
            trackSelector.setParameters(
                trackSelector.buildUponParameters().setMaxVideoSizeSd()
            )
            try {
                player = SimpleExoPlayer.Builder(this)
                    .build()
            } catch (e: Exception) {

            }

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

        val doubleClickForwardFun = DoubleClick(object : DoubleClickListener {
            override fun onSingleClickEvent(view: View?) {

            }

            override fun onDoubleClickEvent(view: View?) {
                val currentPosition = player!!.currentPosition
                player!!.seekTo(currentPosition + 10000)
            }
        })

        val doubleClickBackwardFun = DoubleClick(object : DoubleClickListener {
            override fun onSingleClickEvent(view: View?) {

            }

            override fun onDoubleClickEvent(view: View?) {
                val currentPosition = player!!.currentPosition
                player!!.seekTo(currentPosition - 10000)
            }
        })

        doubleClickBackward.setOnClickListener(doubleClickBackwardFun)
        doubleClickForward.setOnClickListener(doubleClickForwardFun)


        forwardButton.setOnClickListener {
            val currentPosition = player!!.currentPosition
            player!!.seekTo(currentPosition + 10000)
        }
        backwardButton.setOnClickListener {
            val currentPosition = player!!.currentPosition
            player!!.seekTo(currentPosition - 10000)

        }

        /*doubleClickBackward.setOnClickListener(object : View.OnClickListener {

            val gestureDetector =
                GestureDetector(object : GestureDetector.SimpleOnGestureListener() {
                    override fun onDoubleTap(e: MotionEvent?): Boolean {
                        val currentPosition = player!!.currentPosition
                        player!!.seekTo(currentPosition - 10000)
                        return super.onDoubleTap(e)
                    }
                })

            override fun onClick(v: View?) {

            }


        }
        )

        doubleClickForward.setOnClickListener(object : View.OnClickListener {
            val gestureDetector =
                GestureDetector(object : GestureDetector.SimpleOnGestureListener() {
                    override fun onDoubleTap(e: MotionEvent?): Boolean {
                        val currentPosition = player!!.currentPosition
                        player!!.seekTo(currentPosition + 10000)
                        return super.onDoubleTap(e)
                    }
                })

            override fun onClick(v: View?) {

            }


        })*/

        val mediaItem =
            MediaItem.Builder()
                .setUri(currentXmlLink)
                .setMimeType(MimeTypes.APPLICATION_MPD)
                .build()
        player!!.addMediaItem(mediaItem)

        if (player!!.isCurrentWindowDynamic && player!!.duration == C.TIME_UNSET) {
            Toast.makeText(this, "video is Live", Toast.LENGTH_LONG).show()
        }
        playerView!!.player = player
        player!!.playWhenReady = playWhenReady
        player!!.seekTo(currentWindow, playbackPosition)
        playbackStateListener.let { player!!.addListener(it) }
        player!!.prepare()


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
        val doubleClickForwardFun = DoubleClick(object : DoubleClickListener {
            override fun onSingleClickEvent(view: View?) {

            }

            override fun onDoubleClickEvent(view: View?) {
                val currentPosition = player!!.currentPosition
                player!!.seekTo(currentPosition + 10000)
            }
        })

        val doubleClickBackwardFun = DoubleClick(object : DoubleClickListener {
            override fun onSingleClickEvent(view: View?) {

            }

            override fun onDoubleClickEvent(view: View?) {
                val currentPosition = player!!.currentPosition
                player!!.seekTo(currentPosition - 10000)
            }
        })

        doubleClickBackward.setOnClickListener(doubleClickBackwardFun)
        doubleClickForward.setOnClickListener(doubleClickForwardFun)


        forwardButton.setOnClickListener {
            val currentPosition = player!!.currentPosition
            player!!.seekTo(currentPosition + 10000)
        }
        backwardButton.setOnClickListener {
            val currentPosition = player!!.currentPosition
            player!!.seekTo(currentPosition - 10000)

        }
        val link = "https://" + currentJobObject.streamLink
        val mediaItem =
            MediaItem.Builder()
                .setUri(link)
                .setMimeType(MimeTypes.APPLICATION_MPD)
                .build()
        player!!.addMediaItem(mediaItem)
        if (player!!.isCurrentWindowDynamic || player!!.duration == C.TIME_UNSET) {
            Toast.makeText(this, "video is Live", Toast.LENGTH_LONG).show()
        }
        playerView!!.player = player
        player!!.playWhenReady = playWhenReady
        player!!.seekTo(currentWindow, playbackPosition)
        player!!.addListener(playbackStateListener)
        player!!.prepare()
    }


    private fun releasePlayer() {
        if (player != null) {
            player!!.stop()
            player!!.removeListener(playbackStateListener)
            playerView!!.player = null
            player!!.release()


        }
    }

    class PlaybackStateListener : Player.EventListener {
        override fun onPlaybackStateChanged(playbackState: Int) {

            when (playbackState) {
                ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
                ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING -"
                ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY     -"
                ExoPlayer.STATE_ENDED -> ""
                else -> "UNKNOWN_STATE             -"
            }

        }
    }
}

