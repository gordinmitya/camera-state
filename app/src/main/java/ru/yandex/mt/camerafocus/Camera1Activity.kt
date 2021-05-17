package ru.yandex.mt.camerafocus

import android.content.Context
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.IOException


class Camera1Activity : AppCompatActivity() {

    lateinit var surfaceHolder: SurfaceHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        val surface = SurfaceView(this)
        container.addView(surface)

        surfaceHolder = surface.holder
        surfaceHolder.addCallback(surfaceCallback)
    }

    private fun setMode(mode: String) {
        val parameters = camera!!.parameters
        parameters.focusMode = mode
        camera!!.parameters = parameters
    }

    private val movingCallback = Camera.AutoFocusMoveCallback { start, _ ->
        Log.d("auto", "setAutoFocusMoveCallback $start")
        view_focusing.stateColor(!start)
    }

    private val surfaceCallback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            setCameraFacing()
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            if (holder.surface == null) return
            try {
                camera!!.stopPreview()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                camera!!.setPreviewDisplay(holder)
                setMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)
                camera!!.setAutoFocusMoveCallback(movingCallback)
                camera!!.startPreview()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) = Unit
    }

    private var camera: Camera? = null

    fun setCameraFacing() {
        if (camera != null) {
            camera!!.stopPreview()
            camera!!.release()
        }
        try {
            val cameraId = 0
            camera = Camera.open(cameraId)
            val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val rotation = windowManager.defaultDisplay.rotation
            var windowRotation = 0
            when (rotation) {
                Surface.ROTATION_0 -> windowRotation = 0
                Surface.ROTATION_90 -> windowRotation = 90
                Surface.ROTATION_180 -> windowRotation = 180
                Surface.ROTATION_270 -> windowRotation = 270
            }
            val cameraInfo = Camera.CameraInfo()
            Camera.getCameraInfo(cameraId, cameraInfo)
            val cameraRotation: Int
            val orientation: Int
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraRotation = (cameraInfo.orientation + windowRotation) % 360
                orientation = (360 - cameraRotation) % 360
            } else {
                cameraRotation = (cameraInfo.orientation - windowRotation + 360) % 360
                orientation = cameraRotation
            }
            camera!!.setDisplayOrientation(orientation)
            camera!!.setPreviewDisplay(surfaceHolder)
            camera!!.startPreview()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
