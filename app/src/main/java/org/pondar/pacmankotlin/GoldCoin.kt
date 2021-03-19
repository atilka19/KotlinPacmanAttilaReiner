package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

//Here you need to fill out what should be in a GoldCoin and what should the constructor be
class GoldCoin(context: Context, x: Int, y: Int) {
    var cordX: Int = 0
    var cordY: Int = 0
    var taken: Boolean = false
    val bitMap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.custom_coin_8b)

    init {
        cordX = x
        cordY = y
    }
}