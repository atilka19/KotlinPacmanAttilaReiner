package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Ghost(context: Context,
            spawnX: Int, spawnY: Int, sp: Int, dir: Int,
            w: Int, h: Int) {

    //Game dimensions for movement
    private var gameW: Int = 0
    private var gameH: Int = 0

    //Location coords
    var cordX: Int = 0
    var cordY: Int = 0

    //Directions
    private val RIGHT = 1
    private val LEFT = 2
    private val UP = 3
    private val DOWN = 4

    var direction = RIGHT

    //Distance moved per tick
    var speed: Int = 0
    val bitMap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.custom_ghost_8b)

    //Assigning values
    init {
        cordX = spawnX
        cordY = spawnY
        speed = sp
        direction = dir
        gameW = w
        gameH = h
    }

    fun move() {
        when(direction){
            RIGHT -> {
                if (cordX + speed + bitMap.width < gameW) {
                    cordX += speed
                } else changeDirection("l")
            }
            LEFT -> {
                if (cordX - speed > 0) {
                    cordX -= speed
                } else changeDirection("r")
            }
            UP -> {
                if (cordY - speed > 0) {
                    cordY -= speed
                } else changeDirection("d")
            }
            DOWN -> {
                if (cordY + speed + bitMap.height < gameH) {
                    cordY += speed
                } else changeDirection("u")
            }
        }
    }

    //Function to change direction of movement
    fun changeDirection(newDir: String) {
        when(newDir) {
            "r" -> direction = RIGHT
            "l" -> direction = LEFT
            "u" -> direction = UP
            "d" -> direction = DOWN
        }
    }
}