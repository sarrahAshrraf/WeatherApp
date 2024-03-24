package com.example.weatherapppoject.alerts.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.weatherapppoject.R
import com.example.weatherapppoject.alerts.NotificationFragment

class AlarmReciver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent1: Intent?) {

        Toast.makeText(context, "response done", Toast.LENGTH_SHORT).show()

        if (context != null) {
//            MusicControl.getInstance(context).playMusic(context)
        }
        val dismissIntent = Intent(context, MediaNotificationReceiver::class.java)
        val piDismiss = PendingIntent.getBroadcast(context,
            2,
            dismissIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)

        val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, NotificationFragment::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent1!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Alarm Manager")
            .setContentText("try using alarm manager")
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(Notification.PRIORITY_HIGH)
            .addAction(
                R.drawable.ic_launcher_background, "Stop", piDismiss
            )
            .setContentIntent(pendingIntent).build()

        notificationManager.notify(1, builder)

        fun closeMusic(){
//            MusicControl.getInstance(context).stopMusic()
        }

    }

    companion object{
        const val CHANNEL_ID = "alarmManager_channel"
    }
}