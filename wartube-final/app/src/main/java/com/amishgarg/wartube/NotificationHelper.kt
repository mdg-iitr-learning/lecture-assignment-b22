package com.amishgarg.wartube

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.amishgarg.wartube.Activity.MainActivity

class NotificationHelper {


    companion object {
        val CHANNEL_ID = "wartube";
        val CHANNEL_NAME = "Wartube"
        val CHANNEL_DESC = "Posts Notifications"

        fun displayNotification(context : Context, title : String, body: String) {


            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                    context,
                    100,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT
            )

            val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.new_post_icon)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)


            val mNotifMgr: NotificationManagerCompat = NotificationManagerCompat.from(context)
            mNotifMgr.notify(1, mBuilder.build())
        }
    }

}