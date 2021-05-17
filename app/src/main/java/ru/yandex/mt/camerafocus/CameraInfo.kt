package ru.yandex.mt.camerafocus

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import com.jaredrummler.android.device.DeviceName

class CameraInfo(context: Context) {
    private val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    val facingTexts = mutableMapOf(
        CameraCharacteristics.LENS_FACING_BACK to "back",
        CameraCharacteristics.LENS_FACING_FRONT to "front"
    ).also {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            it[CameraCharacteristics.LENS_FACING_EXTERNAL] = "external"
        }
    }

    val levelTexts = mutableMapOf(
        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED to "LIMITED",
        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL to "FULL",
        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY to "LEGACY",
    ).also {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            it[CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3] = "3"
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            it[CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_EXTERNAL] = "EXTERNAL"
        }
    }

    fun getInfo(): String {
        val model = DeviceName.getDeviceName()
        val sb = StringBuilder()
        sb.append(model + "\n\n")
        for (cameraId in manager.cameraIdList) {
            sb.append(getInfo(cameraId))
            sb.append("\n\n")
        }
        return sb.toString()
    }

    fun getInfo(cameraId: String): String {
        val characteristics = manager.getCameraCharacteristics(cameraId)
        val facing = characteristics[CameraCharacteristics.LENS_FACING]
        val level = characteristics[CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL]
        val partialCount = characteristics[CameraCharacteristics.REQUEST_PARTIAL_RESULT_COUNT]
        return "$cameraId: facing ${facingTexts[facing]}, level ${levelTexts[level]}, partialCount $partialCount"
    }
}
