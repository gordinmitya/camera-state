package ru.yandex.mt.camerafocus

import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.Surface
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.impl.CaptureProcessor
import androidx.camera.core.impl.CaptureStage
import androidx.camera.core.impl.ImageInfoProcessor
import androidx.camera.core.impl.ImageProxyBundle
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File

class CameraXActivity : AppCompatActivity() {
    private lateinit var previewView: PreviewView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        previewView = PreviewView(this)
        container.addView(previewView)

        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                val camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview)
            } catch(exc: Exception) {
                Toast.makeText(this, "Use case binding failed", Toast.LENGTH_SHORT).show()
                Log.e(this.javaClass.simpleName, "startCamera", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }
}
