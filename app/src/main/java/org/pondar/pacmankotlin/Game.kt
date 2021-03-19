package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.widget.TextView
import kotlin.math.sqrt


/**
 *
 * This class should contain all your game logic
 */

class Game(private var context: Context,pointsView: TextView,levelView: TextView) {

    private var pointsView: TextView = pointsView
    private var levelView: TextView = levelView
    //Keeping an untouched bitmap var for rotation and mirroring purposes
    var pacBitMap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pacman)

    //Bitmaps for local references, project could be refactored so these are not needed here
    var bitmap: Bitmap = pacBitMap
    var coinBitMap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.custom_coin_8b)
    var ghostBitMap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.custom_ghost_8b)
    var pacx: Int = 0
    var pacy: Int = 0

    //Directions
    private val RIGHT = 1
    private val LEFT = 2
    private val UP = 3
    private val DOWN = 4

    var direction = RIGHT

    //did we initialize the coins?
    var coinsInitialized = false

    //the list of goldcoins - initially empty
    var coins = ArrayList<GoldCoin>()
    var coinsNotTakenCount: Int = 0

    //List of enemies + init
    var enemies = ArrayList<Ghost>()
    var enemiesInitialized = false

    //a reference to the gameview
    private var gameView: GameView? = null
    private var h: Int = 0
    private var w: Int = 0

    //level counter
    var level: Int = 1
    var levelTimeLeft: Int = 60
    //point counter
    private var points : Int = 0
    //player alive
    private var alive: Boolean = true

    fun setGameView(view: GameView) {
        this.gameView = view
    }

    fun initializeGoldcoins(count: Int)
    {
        for (i in 1..count) {
            val randomX = (0..(w - coinBitMap.width)).random()
            val randomY = (0..(h - coinBitMap.height)).random()
            coins.add(GoldCoin(context, randomX, randomY))
        }

        coinsInitialized = true
    }

    //Adding enemies for different levels
    //I couldn't think of a way of doing this dynamically or using randoms without making it at least sometimes impossible
    //So I created enemies for 10 levels that should be doable
    fun initializeEnemies()
    {
        when(level) {
            1 -> {
                val randomX = (w/4..(w - ghostBitMap.width)).random()
                val randomY = (h/4..(h - ghostBitMap.height)).random()
                enemies.add(Ghost(context,randomX,randomY,6, DOWN, w, h))
            }
            2 -> {
                val randomX = (w/3..(w - ghostBitMap.width)).random()
                val randomY = (h/3..(h - ghostBitMap.height)).random()
                enemies.add(Ghost(context,randomX,randomY,6, RIGHT, w, h))
            }
            3 -> {
                val randomX = (w/2..(w - ghostBitMap.width)).random()
                val randomY = (h/2..(h - ghostBitMap.height)).random()
                enemies.add(Ghost(context,randomX,randomY,8, UP, w, h))
            }
            4 -> {
                val randomX = (w/2..(w - ghostBitMap.width)).random()
                val randomY = (h/2..(h - ghostBitMap.height)).random()
                enemies.add(Ghost(context,randomX,randomY,8, LEFT, w, h))
            }
            5 -> {
                val randomX1 = (w/2..(w - ghostBitMap.width)).random()
                val randomY1 = (h/2..(h - ghostBitMap.height)).random()
                enemies.add(Ghost(context,randomX1,randomY1,10, DOWN, w, h))
                val randomX2 = (200..(w - ghostBitMap.width)).random()
                val randomY2 = (200..(h - ghostBitMap.height)).random()
                enemies.add(Ghost(context,randomX2,randomY2,12, RIGHT, w, h))
            }
            6 -> {
                val randomX = (w/2..(w - ghostBitMap.width)).random()
                val randomY = (h/2..(h - ghostBitMap.height)).random()
                enemies.add(Ghost(context,randomX,randomY,10, RIGHT, w, h))
                val randomX2 = (200..(w - ghostBitMap.width)).random()
                val randomY2 = (200..(h - ghostBitMap.height)).random()
                enemies.add(Ghost(context,randomX2,randomY2,12, UP, w, h))
            }
            7 -> {
                val randomX = (w/2..(w - ghostBitMap.width)).random()
                val randomY = (h/2..(h - ghostBitMap.height)).random()
                enemies.add(Ghost(context,randomX,randomY,10, UP, w, h))
                val randomX2 = (200..(w - ghostBitMap.width)).random()
                val randomY2 = (200..(h - ghostBitMap.height)).random()
                enemies.add(Ghost(context,randomX2,randomY2,12, LEFT, w, h))
            }
            8 -> {
                val randomX = (w/2..(w - ghostBitMap.width)).random()
                val randomY = (h/2..(h - ghostBitMap.height)).random()
                enemies.add(Ghost(context,randomX,randomY,10, LEFT, w, h))
                val randomX2 = (200..(w - ghostBitMap.width)).random()
                val randomY2 = (200..(h - ghostBitMap.height)).random()
                enemies.add(Ghost(context,randomX2,randomY2,12, DOWN, w, h))
            }
            9 -> {
                val randomX = (w/2..(w - ghostBitMap.width)).random()
                val randomY = (h/2..(h - ghostBitMap.height)).random()
                enemies.add(Ghost(context,randomX,randomY,12, DOWN, w, h))
                val randomX2 = (200..(w - ghostBitMap.width)).random()
                val randomY2 = (200..(h - ghostBitMap.height)).random()
                enemies.add(Ghost(context,randomX2,randomY2,14, RIGHT, w, h))
            }
            10 -> {
                val randomX = (w/2..(w - ghostBitMap.width)).random()
                val randomY = (h/2..(h - ghostBitMap.height)).random()
                enemies.add(Ghost(context,randomX,randomY,12, LEFT, w, h))
                val randomX2 = (200..(w - ghostBitMap.width)).random()
                val randomY2 = (200..(h - ghostBitMap.height)).random()
                enemies.add(Ghost(context,randomX2,randomY2,14, UP, w, h))
            }
        }

        enemiesInitialized = true
    }


    fun newGame() {
        pacx = 0
        pacy = 0 //just some starting coordinates - you can change this.
        //reset the points
        coinsInitialized = false
        enemiesInitialized = false
        bitmap = pacBitMap
        alive = true
        points = 0
        level = 1
        levelTimeLeft = 60
        direction = RIGHT
        coins.clear()
        enemies.clear()
        pointsView.text = "${context.resources.getString(R.string.points)} $points"
        levelView.text = "${context.resources.getString(R.string.level)} $level"
        gameView?.invalidate() //redraw screen
    }

    fun levelUp() {
        pacx = 0
        pacy = 0
        level++
        levelTimeLeft = 60
        bitmap = pacBitMap
        coinsInitialized = false
        enemiesInitialized = false
        direction = RIGHT
        coins.clear()
        enemies.clear()
        levelView.text = "${context.resources.getString(R.string.level)} $level"
        gameView?.invalidate() //redraw screen
    }

    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }

    fun moveRight(pixels: Int) {
        if (pacx + pixels + bitmap.width < w) {
            pacx += pixels
            doCollisionCheck()
        } else pacx = 0
        gameView!!.invalidate()
    }

    fun moveLeft(pixels: Int) {
        if (pacx - pixels > 0) {
            pacx -= pixels
            doCollisionCheck()
        } else pacx = w - pacBitMap.width
        gameView!!.invalidate()
    }

    fun moveUp(pixels: Int) {
        if (pacy - pixels > 0) {
            pacy -= pixels
            doCollisionCheck()
        } else pacy = h - pacBitMap.height
        gameView!!.invalidate()
    }

    fun moveDown(pixels: Int) {
        if (pacy + pixels + bitmap.height < h) {
            pacy += pixels
            doCollisionCheck()
        } else pacy = 0
        gameView!!.invalidate()
    }

    //Function to change direction of movement
    fun changeDirection(newDir: String) {
        val matrix = Matrix()

        when(newDir) {
            "u" -> {
                matrix.postRotate(270F)
                val scaledBitmap = Bitmap.createScaledBitmap(pacBitMap, pacBitMap.width, pacBitMap.height, true)
                bitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
            }
            "d" -> {
                matrix.postRotate(90F)
                val scaledBitmap = Bitmap.createScaledBitmap(pacBitMap, pacBitMap.width, pacBitMap.height, true)
                bitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
            }
            "l" -> {
                matrix.preScale(-1.0f, 1.0f)
                val scaledBitmap = Bitmap.createScaledBitmap(pacBitMap, pacBitMap.width, pacBitMap.height, true)
                bitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
            }
            "r" -> {
                bitmap = pacBitMap
            }
        }

        when(newDir) {
            "r" -> direction = RIGHT
            "l" -> direction = LEFT
            "u" -> direction = UP
            "d" -> direction = DOWN
        }
    }

    fun doCollisionCheck() {
        coins.forEach {
            if (!it.taken) {
                //Calculating distance
                val dist = checkDistanceCoin(pacx, pacy, it.cordX, it.cordY)
                //Checking if the currently iterated coin is hit
                //Will go trough if distance between 2 objects is smaller,
                //then the sum of distances from center to border of the 2 objects.
                if (dist < (bitmap.width/2) + (coinBitMap.width/2)) {
                    it.taken = true
                    points++
                    pointsView.text = "${context.resources.getString(R.string.points)} $points"
                    //Update the count of active coins
                    coinsNotTakenCount = coins.filter { !it.taken }.size
                }
            }

        }

        enemies.forEach {
                //Calculating distance
                val dist = checkDistanceGhost(pacx, pacy, it.cordX, it.cordY)
                //Checking if the currently iterated ghost is hit
                //Needed to modify this a bit compared to the coins, since the ghost doesn't have a 1:1 w-h ratio, so I just took an average of the 2
                if (dist < (bitmap.width /2) + (ghostBitMap.width/2)) {
                    alive = false
                }
        }

        if (!alive) {
            newGame()
        }
    }

    fun checkDistanceCoin(px: Int, py: Int, cx: Int, cy: Int): Double {
        //Formula is adjusted to be looking at the center of both objects, rather than the top-left corner
        //I think it's correct based on my testing and the best thinking i'm capable of, could be mistaken, just wanted to avoid a funky-hitbox-feeling
        //It's supposed to calculate this by adding half the width to the X coords and half the height to the Y coords
        val sumx = ((cx + (coinBitMap.width/2)) - (px + (bitmap.width/2))) * ((cx + (coinBitMap.width/2)) - (px + (bitmap.width/2)))
        val sumy  = ((cy + (coinBitMap.height/2)) - (py + (bitmap.width/2))) * ((cy + (coinBitMap.height/2)) - (py + (bitmap.height/2)))
        return sqrt(sumx.toDouble() + sumy.toDouble())
    }

    fun checkDistanceGhost(px: Int, py: Int, cx: Int, cy: Int): Double {
        // I haven't adjusted this formula for the ghosts specifically but it seems to be good enough, even tho the ratio isn't 1:1 on the ghost
        val sumx = ((cx + (ghostBitMap.width/2)) - (px + (bitmap.width/2))) * ((cx + (ghostBitMap.width/2)) - (px + (bitmap.width/2)))
        val sumy  = ((cy + (ghostBitMap.height/2)) - (py + (bitmap.width/2))) * ((cy + (ghostBitMap.height/2)) - (py + (bitmap.height/2)))
        return sqrt(sumx.toDouble() + sumy.toDouble())
    }
}