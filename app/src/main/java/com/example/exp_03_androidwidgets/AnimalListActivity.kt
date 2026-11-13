package com.example.exp_03_androidwidgets

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap

class AnimalListActivity : LabActivity() {
    private var pendingAnimal: Animal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_list)
        pendingAnimal = savedInstanceState?.getString(PENDING_ANIMAL)?.let(Animal::valueOf)

        val animals = Animal.entries
        val rows = animals.map { mapOf("name" to it.label, "photo" to it.photo) }
        val list = findViewById<ListView>(R.id.animal_list)
        list.adapter = SimpleAdapter(
            this,
            rows,
            R.layout.item_animal,
            arrayOf("name", "photo"),
            intArrayOf(R.id.animal_name, R.id.animal_photo)
        )
        list.setOnItemClickListener { _, _, position, _ ->
            val animal = animals[position]
            Toast.makeText(this, animal.label, Toast.LENGTH_SHORT).show()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                pendingAnimal = animal
                requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION
                )
            } else {
                postNotification(animal)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PENDING_ANIMAL, pendingAnimal?.name)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION) {
            val animal = pendingAnimal
            pendingAnimal = null
            if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED && animal != null) {
                postNotification(animal)
            }
        }
    }

    private fun postNotification(animal: Animal) {
        val manager = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.animal_notification_channel),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }

        val openList = Intent(this, AnimalListActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val contentIntent = PendingIntent.getActivity(this, 0, openList, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(packageManager.getApplicationIcon(applicationInfo).toBitmap())
            .setContentTitle(animal.label)
            .setContentText(getString(R.string.animal_notification_text, animal.label))
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .build()
        manager.notify(ANIMAL_NOTIFICATION, notification)
    }

    private companion object {
        const val CHANNEL_ID = "animal_selection"
        const val ANIMAL_NOTIFICATION = 1
        const val NOTIFICATION_PERMISSION = 100
        const val PENDING_ANIMAL = "pending_animal"
    }
}
