package com.example.daniwebandroidqueryaudiomediastore

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission

private const val TAG = "AUDIO_QUERY"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)

        val permissionResultLauncher = registerForActivityResult(RequestPermission()){ isGranted ->
            if (isGranted) queryAudio()
        }

        button.setOnClickListener {
            permissionResultLauncher.launch(READ_EXTERNAL_STORAGE)
        }

    }

    private fun queryAudio(){
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM
        )

        val selection = null //not filtering out any row.
        val selectionArgs = null //this can be null because selection is also null
        val sortOrder = null //sorting order is not needed

        applicationContext.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->

            val titleColIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val albumColIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)

            Log.d(TAG, "Query found ${cursor.count} rows")

            while (cursor.moveToNext()) {
                val title = cursor.getString(titleColIndex)
                val album = cursor.getString(albumColIndex)

                Log.d(TAG, "$title - $album")
            }
        }
    }

}