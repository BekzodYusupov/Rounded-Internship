package uz.gita.rounded_internship.server

import android.app.DownloadManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.IBinder
import android.widget.Toast

class DownloadService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        fun downloadFromUrl(url: String) {
            try {
                val request = DownloadManager.Request(Uri.parse(url))
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                request.setTitle("Download")
                request.setDescription("Downloading Your File")
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    System.currentTimeMillis().toString()
                )
                val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.enqueue(request)
            } catch (exception: Exception) {
                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }
}