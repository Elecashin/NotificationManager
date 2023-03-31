package com.example.notificationmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.TextUtils


class MyNotificationListenerService : NotificationListenerService() {
    private val TAG = this.javaClass.simpleName
    private var nlservicereciver: NLServiceReceiver? = null
    private val mPreviousNotificationKey: String? = null
    private var mPreviousNotification: StatusBarNotification? = null

    override fun onCreate() {
        super.onCreate()
        nlservicereciver = NLServiceReceiver()
        val filter = IntentFilter()
        filter.addAction("com.example.notificationmanager.NOTIFICATION_LISTENER_APP")
        registerReceiver(nlservicereciver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(nlservicereciver)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {

        if(TextUtils.isEmpty(mPreviousNotification.toString()) || !TextUtils.isEmpty(mPreviousNotification.toString()) && !sbn.key.equals(mPreviousNotificationKey)) {
            val i = Intent("com.example.notificationmanager.NOTIFICATION_LISTENER_APP")
            i.putExtra("package_name",sbn.packageName)
            i.putExtra("text", sbn.notification.extras.getString("android.text"))
            i.putExtra("title", sbn.notification.extras.getString("android.title"))
            i.putExtra("id", sbn.id)

            sendBroadcast(i)
        }

    }





    internal inner class NLServiceReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.getStringExtra("command") == "clearall") {
                cancelAllNotifications()
            } else if (intent.getStringExtra("command") == "list") {
                val i1 = Intent("com.example.notificationmanager.NOTIFICATION_LISTENER_APP")
                i1.putExtra("notification_event", "=====================")
                sendBroadcast(i1)
                var i = 1
                for (sbn in this@MyNotificationListenerService.activeNotifications) {
                    val i2 = Intent("com.example.notificationmanager.NOTIFICATION_LISTENER_APP")
                    i2.putExtra("notification_event", i.toString() + " " + sbn.packageName + "n")
                    sendBroadcast(i2)
                    i++
                }
                val i3 = Intent("com.example.notificationmanager.NOTIFICATION_LISTENER_APP")
                i3.putExtra("notification_event", "===== Notification List ====")
                sendBroadcast(i3)
            }
        }
    }
}