package ru.yandex.mt.camerafocus

import android.graphics.Color
import android.view.View

object StateColors {
    val CHANGING = Color.argb(255, 180, 0, 0)
    val STABLE = Color.argb(255, 0, 180, 0)
}

public fun View.stateColor(stable: Boolean) {
    this.setBackgroundColor(if (stable) StateColors.STABLE else StateColors.CHANGING)
}
