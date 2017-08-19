package com.savvasdalkitsis.gameframe.composition.model

import android.graphics.Color

data class ARGB(val a: Int, val r: Int, val g: Int, val b: Int) {

    constructor(color: Int) : this(
                a = Color.alpha(color),
                r = Color.red(color),
                g = Color.green(color),
                b = Color.blue(color)
    )

    operator fun times(m: Float): ARGB = ARGB((this.a * m).toInt(), (this.r * m).toInt(), (this.g * m).toInt(), (this.b * m).toInt())

    operator fun plus(argb: ARGB) = ARGB(this.a + argb.a, this.r + argb.r, this.g + argb.g, this.b + argb.b)

    fun color() = Color.argb(a, r, g, b)

    fun multiplyAlpha(alpha: Float) = ARGB((a * alpha).toInt(), r, g, b)

    fun withAlphaValue(alpha: Int) = ARGB(alpha, r, g, b)
}
