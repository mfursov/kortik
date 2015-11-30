package com.github.mfursov.kortik

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.github.mfursov.kortik.action.pausePlayback
import com.github.mfursov.kortik.action.playNextFile
import com.github.mfursov.kortik.util.log
import org.jetbrains.anko.debug

class PlaybackService : Service() {

    override fun onCreate() {
        log.debug { "PlaybackService::onCreate" }
        super.onCreate()
    }

    override fun onDestroy() {
        log.debug { "PlaybackService::onDestroy" }
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action ?: return START_NOT_STICKY;
        log.debug { "PlaybackService::onStartCommand: " + action }
        when (action) {
            Constants.START_FOREGROUND_ACTION -> {
                val notificationIntent = Intent(this, MainActivity::class.java)
                notificationIntent.setAction(Constants.MAIN_ACTION)
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

                val prevIntent = Intent(this, PlaybackService::class.java);
                prevIntent.setAction(Constants.PREV_ACTION);
                val pendingPrevIntent = PendingIntent.getService(this, 0, prevIntent, 0);

                val playIntent = Intent(this, PlaybackService::class.java);
                playIntent.setAction(Constants.PLAY_ACTION);
                val pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0);

                val nextIntent = Intent(this, PlaybackService::class.java);
                nextIntent.setAction(Constants.NEXT_ACTION);
                val pendingNextIntent = PendingIntent.getService(this, 0, nextIntent, 0);

                val notification = NotificationCompat.Builder(this)
                        .setContentTitle("Kortik Music Player") //TODO:
                        .setTicker("Kortik Music Player") //TODO:
                        .setContentText(Kortik.state.playingFile?.nameWithoutExtension)
                        .setSmallIcon(android.R.drawable.sym_def_app_icon)
                        // .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                        .setContentIntent(pendingIntent)
                        .setOngoing(true)
                        .addAction(android.R.drawable.ic_media_previous, "Previous", pendingPrevIntent)
                        .addAction(android.R.drawable.ic_media_play, "Play", pendingPlayIntent)
                        .addAction(android.R.drawable.ic_media_next, "Next", pendingNextIntent).build();
                log.debug { "Staring foreground notification!" }
                startForeground(Constants.PLAYBACK_NOTIFICATION_ID, notification);
            }
            Constants.PREV_ACTION -> {
                playNextFile(true)
            }
            Constants.PLAY_ACTION -> {
                pausePlayback()
            }
            Constants.NEXT_ACTION -> {
                playNextFile(false)
            }
            Constants.STOP_FOREGROUND_ACTION -> {
                stopForeground(true);
            }
        }
        return START_STICKY;

    }

    override fun onBind(intent: Intent?): IBinder? = null

}

