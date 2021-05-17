package ru.yandex.mt.camerafocus

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var buttons: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttons = listOf<Button>(
            findViewById(R.id.button_camera1),
            findViewById(R.id.button_camera2),
            findViewById(R.id.button_camerax),
        )
        buttons.forEach { it.setOnClickListener(this::onClick) }

        if (allPermissionsGranted()) {
            gotPermissions()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            buttons.forEach { it.isEnabled = false }
        }
    }

    private fun gotPermissions() {
        buttons.forEach { it.isEnabled = true }
        text_characteristics.text = CameraInfo(this).getInfo()
    }

    private fun onClick(view: View) {
        val targetClass = when (view.id) {
            R.id.button_camera1 -> Camera1Activity::class.java
            R.id.button_camera2 -> Camera2Activity::class.java
            R.id.button_camerax -> CameraXActivity::class.java
            else -> throw IllegalArgumentException()
        }
        startActivity(Intent(this, targetClass))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (allPermissionsGranted()) {
            gotPermissions()
        } else {
            Toast.makeText(this, "grant permissions", Toast.LENGTH_SHORT).show()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
