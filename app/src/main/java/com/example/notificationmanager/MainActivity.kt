package com.example.notificationmanager

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private var nReceiver: NotificationReceiver? = null
    private val notificationSender = NotificationSender()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nReceiver = NotificationReceiver();
        val filter =  IntentFilter();
        filter.addAction("com.example.notificationmanager.NOTIFICATION_LISTENER_APP");
        registerReceiver(nReceiver,filter)


    }

    override fun onResume() {
        super.onResume()
        val button = findViewById<Button>(R.id.btn_permission)

        if (!notificationSender.checkPermission(applicationContext)){
            button.visibility = View.VISIBLE
            button.setOnClickListener{
                val intent= Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent)
            }

        }else{
            button.visibility = View.GONE

        }
    }

    internal class NotificationReceiver : BroadcastReceiver() {
        private val notificationSender = NotificationSender()

        override fun onReceive(context: Context, intent: Intent) {
            val packageName = intent.getStringExtra("package_name")
            val text = intent.getStringExtra("text")
            val title = intent.getStringExtra("title")
            val id = intent.getIntExtra("id", 0)
            CoroutineScope(Dispatchers.Default).launch {
                notificationSender.sendNotification(context, id, title ?: "", text ?: "", packageName ?: "")
            }
        }
    }


}