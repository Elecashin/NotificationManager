package com.example.notificationmanager

import android.content.ComponentName
import android.content.Context
import android.provider.Settings
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.json.JSONObject


class NotificationSender {

    private val clientAuth = HttpClient(OkHttp)

    suspend fun sendNotification(id : Int, title : String, text : String, packageName : String) : Boolean{
            try {

                val toSend = JSONObject().put("id", id).put("title", title).put("text", text).put("packageName", packageName)
                println(toSend.toString())
                val resultAsString = clientAuth.post(
                    "http://" +
                            BuildConfig.IP +
                            "/add-notification"
                ) {
                    setBody(toSend.toString())
                }.status
                println(resultAsString)

                return resultAsString== HttpStatusCode.OK
            }catch (e : Exception){

            }
        return false


    }

    fun checkPermission(context : Context) : Boolean{
        val cn = ComponentName(context, MyNotificationListenerService::class.java)
        val flat: String = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        return flat.contains(cn.flattenToString())
    }


}