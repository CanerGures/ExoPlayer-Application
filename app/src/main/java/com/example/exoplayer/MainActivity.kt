package com.example.exoplayer

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.ddd.androidutils.DoubleClick
import com.ddd.androidutils.DoubleClickListener
import com.example.exoplayer.api.model.GetJobsList
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util


class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
    private lateinit var playButton: ImageView
    private lateinit var forwardButton: ImageView
    private lateinit var backwardButton: ImageView
    private lateinit var closeButton: ImageView
    private lateinit var doubleClickForward: FrameLayout
    private lateinit var doubleClickBackward: FrameLayout
    private lateinit var liveIcon: ImageView
    private lateinit var timeBarDuration: TextView
    private lateinit var customseekbar: SeekBar
    lateinit var runnable: Runnable

    lateinit var handler: Handler
    private var playerView: PlayerView? = null
    private var player: SimpleExoPlayer? = null
    lateinit var currentJobObject: GetJobsList
    private var currentXmlLink: Any? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playonce = 0
    private var playbackPosition: Long = 0
    lateinit var playerPosition: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playerView = findViewById(R.id.video_view)
        playButton = findViewById(R.id.centerPlayButton)
        forwardButton = findViewById(R.id.centerForwardButton)
        backwardButton = findViewById(R.id.centerBackwardButton)
        doubleClickForward = findViewById(R.id.doubleClickForward)
        doubleClickBackward = findViewById(R.id.doubleClickBackward)
        playerPosition = findViewById(R.id.exo_positionn)
        closeButton = findViewById(R.id.closeButton)
        customseekbar = findViewById(R.id.customseekbar)
        timeBarDuration = findViewById(R.id.exo_durationn)
        liveIcon = findViewById(R.id.liveIcon)


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
            when (currentXmlLink) {
                null -> {
                    initializePlayer()
                    setHandler()
                    initializeSeekBar()
                }
                else -> {
                    initializePlayerXml(currentXmlLink as String)
                    setHandler()
                    initializeSeekBar()
                }
            }
            Log.e("LIVEStart", player!!.isCurrentWindowLive.toString())
        }
    }

    override fun onResume() {
        super.onResume()

        if (Util.SDK_INT <= 23 || player == null) {
            currentXmlLink = intent.extras?.get("currentLink")
            when (currentXmlLink) {
                null -> {
                    initializePlayer()
                    setHandler()
                    initializeSeekBar()
                }
                else -> {
                    initializePlayerXml(currentXmlLink as String)
                    setHandler()
                    initializeSeekBar()
                }
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
        finish()

    }

    private fun initializeSeekBar() {
        customseekbar.progress = 0
        customseekbar.setOnSeekBarChangeListener(this)

        Log.e("LIVE", player!!.isCurrentWindowLive.toString())
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


        val mediaItem =
            MediaItem.Builder()
                .setUri(currentXmlLink)
                .setMimeType(MimeTypes.APPLICATION_MPD)
                .build()
        player!!.addMediaItem(mediaItem)



        playerView!!.player = player
        player!!.playWhenReady = playWhenReady
        player!!.seekTo(currentWindow, playbackPosition)
        player!!.addListener(object : Player.EventListener {
            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                if (player!!.isCurrentWindowDynamic && player!!.isCurrentWindowLive) {
                    customseekbar.progress = player!!.duration.toInt()
                    liveIcon.visibility = View.VISIBLE

                }
                Log.e("timeline", "live:" + player!!.isCurrentWindowLive)
            }

            override fun onPlayerError(error: ExoPlaybackException) {
                Toast.makeText(
                    this@MainActivity,
                    "Error Occured! Player is closing.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("onPlayerError", "onPlayerError:$error")
                finish()
            }

        })
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
                .setUri("https://livesim.dashif.org/livesim/segtimeline_1/testpic_2s/Manifest.mpd")
                .setMimeType(MimeTypes.APPLICATION_MPD)
                .build()
        player!!.addMediaItem(mediaItem)

        playerView!!.player = player
        player!!.playWhenReady = playWhenReady
        player!!.seekTo(currentWindow, playbackPosition)
        player!!.addListener(object : Player.EventListener {
            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                if (player!!.isCurrentWindowDynamic && player!!.isCurrentWindowLive) {
                    liveIcon.visibility = View.VISIBLE


                }


            }

            override fun onPlayerError(error: ExoPlaybackException) {
                Toast.makeText(
                    this@MainActivity,
                    "Error Occured! Player is closing.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("onPlayerError", "onPlayerError:$error")
                finish()
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (playonce == 0 && player!!.isCurrentWindowLive) {
                    player!!.seekTo(player!!.duration)
                    playonce += 1
                }
            }


        })

        player!!.prepare()
        Log.e("LIVEOncreate", player!!.isCurrentWindowLive.toString())
    }

    private fun setHandler() {
        Log.e("LIVEHandlerTop", player!!.isCurrentWindowLive.toString())
        handler = Handler()

        runnable = Runnable {
            customseekbar.max = player!!.duration.toInt()
            if (player!!.isCurrentWindowDynamic && player!!.isCurrentWindowLive) {
                if (player!!.duration > 0) {
                    val currentduration = player!!.currentPosition
                    customseekbar.progress = currentduration.toInt()
                    timeBarDuration.text = "-" + convertTime(player!!.duration)
                    playerPosition.text = "-" + convertTime(player!!.duration - currentduration)
                    Log.e("current", "$currentduration")

                }
            } else {

                if (player!!.duration > 0) {
                    val currentduration = player!!.currentPosition
                    customseekbar.progress = currentduration.toInt()
                    timeBarDuration.text = "" + convertTime(currentduration)
                    playerPosition.text = "" + convertTime(player!!.duration - currentduration)
                }
            }
            handler.postDelayed(runnable, 500)

        }
        handler.post(runnable)
        Log.e("LIVEHandlerBot", player!!.isCurrentWindowLive.toString())

    }


    private fun convertTime(ms: Long): String {
        var time: String = ""
        var x: Int = (ms / 1000).toInt()
        val seconds = x % 60
        x /= 60
        val minutes = x % 60
        x /= 60
        val hours = x % 24
        time = if (hours != 0) {
            String.format("%02d", hours) + ":" + String.format(
                "%02d",
                minutes
            ) + ":" + String.format(
                "%02d",
                seconds
            )
        } else {
            String.format("%02d", minutes) + ":" + String.format("%02d", seconds)
        }
        return time
    }

    private fun releasePlayer() {
        if (player != null) {
            player!!.stop()
            handler.removeCallbacks(runnable)
            playerView!!.player = null
            player!!.release()
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar!!.id) {
            R.id.customseekbar -> {
                if (fromUser && player!!.isCurrentWindowDynamic && player!!.isCurrentWindowLive) {
                    player!!.seekTo(progress.toLong())
                    val currentduration = player!!.currentPosition
                    timeBarDuration.text = "-" + convertTime(player!!.duration)
                    playerPosition.text = "-" + convertTime(player!!.duration - currentduration)
                } else if (fromUser) {
                    player!!.seekTo(progress.toLong())
                    val currentduration = player!!.currentPosition
                    timeBarDuration.text = "" + convertTime(currentduration)
                    playerPosition.text = "" + convertTime(player!!.duration - currentduration)
                }
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }
}

