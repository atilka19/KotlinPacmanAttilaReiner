package org.pondar.pacmankotlin

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    //reference to the game class.
    private var game: Game? = null
    private var gameTimer: Timer = Timer()
    private var levelTimer: Timer = Timer()
    var running = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //makes sure it always runs in portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)

        game = Game(this,pointsView,levelView)
        gameTimer.schedule(object : TimerTask() {
            override fun run() {
                gameTimerMethod()
            }
        }, 0, 50) // 0 = start point, val2 = ms between ticks
        levelTimer.schedule(object : TimerTask() {
            override fun run() {
                levelTimerMethod()
            }
        }, 0, 1000) // 0 = start point, val2 = ms between ticks
        //intialize the game view class and game class
        game?.setGameView(gameView)
        gameView.setGame(game)
        game?.newGame()

        running = true
        gameView.setOnTouchListener(object : OnSwipeTouchListener(this) {

            override fun onSwipeTop() {
                game?.changeDirection("u")
            }

            override fun onSwipeBottom() {
                game?.changeDirection("d")
            }

            override fun onSwipeLeft() {
                game?.changeDirection("l")
            }

            override fun onSwipeRight() {
                game?.changeDirection("r")
            }
        })

        cont.setOnClickListener {
            cont.visibility = View.INVISIBLE
            reset.visibility = View.INVISIBLE
            pause.visibility = View.VISIBLE
            running = true
        }

        pause.setOnClickListener {
            cont.visibility = View.VISIBLE
            reset.visibility = View.VISIBLE
            pause.visibility = View.INVISIBLE
            running = false;
        }

        reset.setOnClickListener {
            game?.newGame()
            timerView.text = "${this.resources.getString(R.string.time)} ${game?.levelTimeLeft.toString()}"
        }
    }

    private fun gameTimerMethod() {
        this.runOnUiThread(gameTimerTick)
    }

    private val gameTimerTick = Runnable {
        //This method runs in the same thread as the UI.
        // so we can draw
        if (running) {
            when(game?.direction) {
                1 -> game?.moveRight(8)
                2 -> game?.moveLeft(8)
                3 -> game?.moveUp(8)
                4 -> game?.moveDown(8)
            }
            game?.enemies?.forEach {
                it.move()
            }
        }
    }

    private fun levelTimerMethod() {
        if (running) {
            this.runOnUiThread(levelTimerTick)
        }
    }

    private val levelTimerTick = Runnable {
        //Level timer actions, updating text checking if it hit 0
        game?.levelTimeLeft = game?.levelTimeLeft!!.minus(1)
        timerView.text = "${this.resources.getString(R.string.time)} ${game?.levelTimeLeft.toString()}"
        if (game?.levelTimeLeft == 0) {
            game?.newGame()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_settings) {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_LONG).show()
            return true
        } else if (id == R.id.action_newGame) {
            Toast.makeText(this, "New Game clicked", Toast.LENGTH_LONG).show()
            game?.newGame()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
