package com.example.exoplayer

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.ddd.androidutils.DoubleClick
import com.ddd.androidutils.DoubleClickListener
import com.example.exoplayer.api.model.GetJobsList
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.TimeBar
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private lateinit var playbackStateListener: Player.EventListener
    private lateinit var playButton: ImageView
    private lateinit var forwardButton: ImageView
    private lateinit var backwardButton: ImageView
    private lateinit var closeButton: ImageView
    private lateinit var doubleClickForward: FrameLayout
    private lateinit var doubleClickBackward: FrameLayout
    private lateinit var liveTimeBar: LinearLayout
    private lateinit var liveIcon: ImageView
    private lateinit var timeBarBottom: TimeBar
    private lateinit var timeBarDuration: TextView
    private var playerView: PlayerView? = null
    private var player: SimpleExoPlayer? = null
    lateinit var currentJobObject: GetJobsList
    private var currentXmlLink: Any? = null

    //lateinit var timeBar : LinearLayout
    private var playWhenReady = true
    private var currentWindow = 0
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
        playerPosition = findViewById(R.id.exo_position)
        closeButton = findViewById(R.id.closeButton)
        //timeBar = findViewById(R.id.timebarLayout)
        timeBarBottom = findViewById(R.id.exo_progress)
        timeBarDuration = findViewById(R.id.exo_duration)
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
        finish()

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
                    liveIcon.visibility = View.VISIBLE
                    //timeBar.visibility = View.GONE
                    liveTimeBar.visibility = View.VISIBLE


                }
                Log.e("timeline", "live:" + player!!.isCurrentWindowLive)
            }

            override fun onExperimentalOffloadSchedulingEnabledChanged(offloadSchedulingEnabled: Boolean) {
                Log.e("onexperimenta off load", "$offloadSchedulingEnabled")
            }

            override fun onIsLoadingChanged(isLoading: Boolean) {
                Log.e("onIsLoadingChanged", "$isLoading")
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                Log.e("onIsPlayingChanged", "$isPlaying")
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                Log.e("onMediaItemTransition", "mediaItem:$mediaItem" + "   reason" + reason)
            }

            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                Log.e(
                    "onPlayWhenReadyChanged",
                    "playwhenready:$playWhenReady" + "   reason" + reason
                )
            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                Log.e("PlaybkParametersChanged", "playbackParameters:$playbackParameters")
            }

            override fun onPlaybackStateChanged(state: Int) {
                Log.e("onPlaybackStateChanged", "onPlaybackStateChanged:$state")
            }

            override fun onPlaybackSuppressionReasonChanged(playbackSuppressionReason: Int) {
                Log.e("supressingreason", "supressingreason:$playbackSuppressionReason")
            }

            override fun onPlayerError(error: ExoPlaybackException) {
                Log.e("onPlayerError", "onPlayerError:$error")
            }

            override fun onPositionDiscontinuity(reason: Int) {
                Log.e("onPositionDiscontinuity", "onPositionDiscontinuity:$reason")
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                Log.e("onRepeatModeChanged", "onRepeatModeChanged:$repeatMode")
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                Log.e("ShuffleEnabledChanged", "ShuffleEnabledChanged:$shuffleModeEnabled")
            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray
            ) {
                Log.e(
                    "onTracksChanged",
                    "trackGroups:$trackGroups" + "trackSelections$trackSelections"
                )
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
                .setUri("http://livesim.dashif.org/livesim/segtimeline_1/testpic_2s/Manifest.mpd")
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
                    timeBarDuration.id = R.id.exo_position


                }
                Log.e("timeline", "live:" + player!!.isCurrentWindowLive)
                Log.e("timeline", "duration:" + player!!.duration)
                Log.e("timeline", "contentduration:" + player!!.contentDuration)
                Log.e("timeline", "renderer count:" + player!!.rendererCount)
                Log.e("timeline", "contentPosition:" + player!!.contentPosition)
                Log.e("timeline", "currentPosition:" + player!!.currentPosition)
                Log.e("timeline", "bufferedPosition:" + player!!.bufferedPosition)
                Log.e("timeline", "totalBufferedDuration:" + player!!.totalBufferedDuration)
                Log.e("timeline", "timeBarPosition:" + playerPosition.text)
                Log.e("timeline", "timeBarDuration:" + timeBarDuration.text)
                Log.e("timeline", "timeline:$timeline")

                val duration = player!!.duration
                val position = player!!.currentPosition

                val hours = String.format("%02d", TimeUnit.MILLISECONDS.toHours(duration))
                val minutes = String.format(
                    "%02d",
                    TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(
                            duration
                        )
                    )
                )
                val seconds = String.format(
                    "%02d",
                    TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(
                            duration
                        )
                    )
                )
                val hoursPosition = String.format("%02d", TimeUnit.MILLISECONDS.toHours(duration))
                val minutesPostion = String.format(
                    "%02d",
                    TimeUnit.MILLISECONDS.toMinutes(position) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(
                            position
                        )
                    )
                )
                val secondsPosition = String.format(
                    "%02d",
                    TimeUnit.MILLISECONDS.toSeconds(position) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(
                            position
                        )
                    )
                )
                if (hours == "00") {
                    val timeFormat = "-$minutes.$seconds"
                    Log.e("timeline", "timeFormat:$timeFormat")
                    timeBarDuration.text = timeFormat
                } else {
                    val timeFormat = "-$hours.$minutes.$seconds"
                    Log.e("timeline", "timeFormat:$timeFormat")
                    timeBarDuration.text = timeFormat
                }


            }

            override fun onExperimentalOffloadSchedulingEnabledChanged(offloadSchedulingEnabled: Boolean) {
                Log.e("Experimental off load", "$offloadSchedulingEnabled")
            }

            override fun onIsLoadingChanged(isLoading: Boolean) {
                Log.e("onIsLoadingChanged", "$isLoading")
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                Log.e("onIsPlayingChanged", "$isPlaying")
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                Log.e("onMediaItemTransition", "mediaItem:$mediaItem" + "   reason" + reason)
            }

            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                Log.e(
                    "onPlayWhenReadyChanged",
                    "playwhenready:$playWhenReady" + "   reason" + reason
                )
            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                Log.e("PlaybkParametersChanged", "playbackParameters:$playbackParameters")
            }

            override fun onPlaybackStateChanged(state: Int) {
                Log.e("onPlaybackStateChanged", "onPlaybackStateChanged:$state")
            }

            override fun onPlaybackSuppressionReasonChanged(playbackSuppressionReason: Int) {
                Log.e("supressingreason", "supressingreason:$playbackSuppressionReason")
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

            override fun onPositionDiscontinuity(reason: Int) {
                Log.e("onPositionDiscontinuity", "onPositionDiscontinuity:$reason")
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                Log.e("onRepeatModeChanged", "onRepeatModeChanged:$repeatMode")
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                Log.e("ShuffleEnabledChanged", "ShuffleEnabledChanged:$shuffleModeEnabled")
            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray
            ) {
                Log.e(
                    "onTracksChanged",
                    "trackGroups:$trackGroups" + "trackSelections$trackSelections"
                )
            }


        })
        player!!.prepare()
    }

    private fun releasePlayer() {
        if (player != null) {
            player!!.stop()
            playerView!!.player = null
            player!!.release()
        }
    }

    fun convertSecondsToHMmSs(seconds: Long): String? {
        val s = seconds % 60
        val m = seconds / 60 % 60
        val h = seconds / (60 * 60) % 24
        return String.format("%d:%02d:%02d", h, m, s)
    }

}

