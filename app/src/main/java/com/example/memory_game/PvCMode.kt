package com.example.memory_game

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.lang.Thread.sleep
import java.util.Timer
import kotlin.concurrent.schedule

class PvCMode : AppCompatActivity() {

    private lateinit var buttons: List<ImageButton>

   var firstTurn:Boolean=false
    var card1: Int = -1
    var card2: Int = -1
    var opened :Int =0;
    var buttonClicked = BooleanArray(0)
    var currentPlayer : Int = 1
    var pointsTogether: Int =0
    //lateinit var help:MutableList<Int>
    //punktu glābšanai

    var player1_points = 0
    var player2_points = 0
    lateinit var cards : MutableList<Int>

    var known= hashMapOf<Int,MutableList<Int>>()
    lateinit var not_opened: MutableList<Int>
    //lateinit var insideList: MutableList<Int>
    var pressed: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // foto pievienošana massīvā (list)
        cards = mutableListOf(
            R.drawable.lolipop, R.drawable.cupcake, R.drawable.donut, R.drawable.eclair,
            R.drawable.gingerbread, R.drawable.kitkat, R.drawable.marshmallow, R.drawable.pie
        )
        not_opened = (0 until 16).toMutableList()
        /*for(i in not_opened){
            Log.i("MainActivity", i.toString())
        }*/
        val pl1_p = findViewById<TextView>(R.id.player2_p)
        val pl2_p = findViewById<TextView>(R.id.player1_p)

        // dati no iepriekšēja activity, fragmenta
        val player1 = intent.getStringExtra("player1")
        var player2 = "Computer"
        val pl1_box = findViewById<TextView>(R.id.player1_score)
        pl1_box.text = player1 + "'s points: "
        val pl2_box = findViewById<TextView>(R.id.player2_score)
        pl2_box.text = player2 + "'s points: "

        cards.addAll(cards)
        cards.shuffle()  // list randomiskai kārtošanai

        // list ar kartīņām
        buttons = listOf(
            findViewById(R.id.imageButton1), findViewById(R.id.imageButton19),
            findViewById(R.id.imageButton2), findViewById(R.id.imageButton3),
            findViewById(R.id.imageButton4), findViewById(R.id.imageButton5),
            findViewById(R.id.imageButton6), findViewById(R.id.imageButton7),
            findViewById(R.id.imageButton8), findViewById(R.id.imageButton13),
            findViewById(R.id.imageButton12), findViewById(R.id.imageButton14),
            findViewById(R.id.imageButton15), findViewById(R.id.imageButton16),
            findViewById(R.id.imageButton17), findViewById(R.id.imageButton18)
        )
        buttonClicked = BooleanArray(buttons.size)


        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {

                if (End()) {
                    val endbox = findViewById<FrameLayout>(R.id.win_box)
                    val endtext = findViewById<TextView>(R.id.win)
                    endbox.visibility = View.VISIBLE
                    endtext.visibility = View.VISIBLE
                    if (player1_points > player2_points) {
                        endtext.text = player1 + " Win !!!"
                    } else if (player1_points == player2_points) {
                        endtext.text = "Draw !!!"
                    } else
                        endtext.text = player2 + " Win !!!"
                }


                if (!buttonClicked[index]) {
                    Log.i("MainActivity", "Clicked button: " + currentPlayer.toString())

                    not_opened.remove(index)
                    buttonClicked[index] = true
                    open(button, index)
                    //runBlocking {
                    //  Log.i("MainActivity", "delaystart")
                    //delay(3000)
                    //Log.i("MainActivity", "delayEnd") }

                    if (currentPlayer == 1) {
                        if (card1 == -1) {
                            card1 = index
                            Log.i("MainActivity", "Card 1:= " + card1.toString())
                        } else if (card2 == -1) {
                            card2 = index
                            Log.i("MainActivity", "Card 2:= " + card2.toString())
                        }
                    }

                    if (currentPlayer==2 && card2 ==-1) {
                        Log.i("MainActivity", "Second computer turn ")
                        //pressed = 1
                        buttons[computerTurn2()].performClick() //2
                    }


                    if (card1 >= 0 && card2 >= 0) {

                        Log.i("MainActivity", "Divas atvertas " + currentPlayer)
                        if (currentPlayer == 1) {
                            if (Match()) {
                                Log.i("MainActivity", "Match speletajam")
                                points(pl1_p, pl2_p)
                                card1 = -1
                                card2 = -1
                            } else {
                                Log.i("MainActivity", "Nav match spel")

                                close(buttons[card1])
                                close(buttons[card2])
                                buttonClicked[card1] = false
                                buttonClicked[card2] = false
                                add_known(card1)
                                add_known(card2)
                                card1 = -1
                                card2 = -1
                                currentPlayer = 2

                                buttons[computerTurn1()].performClick() // 1


                            }
                        } else if (currentPlayer == 2) {
                            if (Match()) {
                                Log.i("MainActivity", "Computer match")
                                points(pl1_p, pl2_p)
                                card1 = -1
                                card2 = -1
                                currentPlayer = 2
                                buttons[computerTurn1()].performClick() //1
                            } else {
                                Log.i("MainActivity", "Computer nav match")
                                close(buttons[card1])
                                close(buttons[card2])
                                buttonClicked[card1] = false
                                buttonClicked[card2] = false
                                add_known(card1)
                                add_known(card2)
                                card1 = -1
                                card2 = -1
                                if (pressed == 2) {
                                    Log.i("MainActivity", "Datora gajieni beidzas, currentPL = 1")
                                    currentPlayer = 1
                                }

                            }
                        }
                    }
                }


            }
        }
    }



    fun add_known (index: Int){
    if (known.containsKey(cards[index])) {
        if (known[cards[index]]?.first() != index) {
            known[cards[index]]?.add(index)
        }
    } else {
        known[cards[index]] = mutableListOf(index)
}}

        // ja atverti 2 kartinas, tad vai aizvert vinus vai atstat (ka ari anulet vertibas card1, card2)
        fun points(pl1_p: TextView, pl2_p: TextView) {
                    if (currentPlayer == 1) {
                        player1_points++
                        pl1_p.text = player1_points.toString()
                    } else if (currentPlayer == 2) {
                        player2_points++
                        pl2_p.text = player2_points.toString()

            }
        }

    fun open (button : ImageButton, index: Int){
        Log.i("MainActivity", "open"+index)

    // kartiņas animācijai
    val oa12 = ObjectAnimator.ofFloat(button,"scaleX",1f,0f)
    val oa23 = ObjectAnimator.ofFloat(button,"scaleX",0f,1f)
    oa12.interpolator = DecelerateInterpolator()
    oa23.interpolator = AccelerateDecelerateInterpolator()

            oa12.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    button.setImageResource(cards[index])
                        //opened++
                    oa23.start()
                    oa23.setDuration(300)
                }
            })
            oa12.start()
            oa12.setDuration(300)

        //Log.i("MainActivity", "end open"+index)
    }

    fun varButKnown (): Boolean {
        for (i in known.values) {
            if (i.size == 2) {
                for (j in i) {
                    Log.i("MainActivity", "Elementi " + j.toString())
                }
                card1 = i[0]
                //card2 = i[1]
                Log.i(
                    "MainActivity",
                    "Kartites  no known " + card1.toString() + " un " + card2.toString()
                )
                //pressed = 1
                firstTurn = true
                break

            }
        }
        if (card1!=-1){
        return true}
        else {
            return false
        }
    }
        // sākas Datora gājiens
        fun computerTurn1 (): Int {

            Log.i("MainActivity", "Computer Turn 1 ")

            // jaizvelas pirmo kartiti

            //parbauda var but jau ir vienadas kartites in known Hash

            if (varButKnown()) {
                return card1
            } else {
                Log.i(
                    "MainActivity",
                    "pressed =0, random".toString()
                )// no kartitem kuri nav atverti nevienu reizi
                if (not_opened.isNotEmpty()) {
                    val randomIndex = not_opened.random()
                    card1 = randomIndex
                    Log.i(
                        "MainActivity",
                        "not_opened Kartites " + card1.toString() + " un " + card2.toString()
                    )
                    pressed = 1
                    firstTurn = true
                    return card1
                } else {
                    Log.i("MainActivity", "errorPair")
                    return 1000
                }

            }
        }

    fun computerTurn2 (): Int {
                    //pressed=1
                    Log.i("MainActivity", "pressed =1 " + card2.toString())
                    // jaizvelas otro kartiti from known ja pirma bija from known

                        Log.i("MainActivity", " Otra card2 == 0".toString())// jaizvelas otro kartiti from known//atkal gan randomiski from not_opened, gan from known
                        val image: Int = cards[card1]
                        val indexList = known.get(image)

                        if (known.get(image)?.size==2) {
                            val indexT = indexList?.get(1)
                            if (indexT != card1) {
                               card2 = indexT!!
                                //pressed = 2
                                //currentPlayer =1
                                firstTurn = false
                                Log.i(
                                    "MainActivity",
                                    " Otra k from known, : Kartites " + card1.toString() + " un " + card2.toString()
                                )
                                return card2}
                            else{
                                val randomIndex = not_opened.random()
                                card2 = randomIndex
                                Log.i(
                                    "MainActivity",
                                    "Rnadomiski ari otru " + card1.toString() + " un " + card2.toString()
                                )
                                pressed = 2
                                // currentPlayer =1
                                firstTurn = false
                                return card2
                                //pressed = 2
                                //t_opened.remove(card2)
                                //known.remove(cards[card2])
                                //Match()
                                //open(buttons.get(card2), card2)
                            }
                        } //random
                            else  //radomiski
                                 //if (not_opened.isNotEmpty())
                                {
                                    val randomIndex = not_opened.random()
                                    card2 = randomIndex
                                    Log.i(
                                        "MainActivity",
                                        "Rnadomiski ari otru " + card1.toString() + " un " + card2.toString()
                                    )
                                    pressed = 2
                                    // currentPlayer =1
                                    firstTurn = false
                                    return card2
                                    //pressed = 2
                                    //t_opened.remove(card2)
                                    //known.remove(cards[card2])
                                    //Match()
                                    //open(buttons.get(card2), card2)

                                }
                                       // return 1000

                            }








    // kopejo punktu parbaude, lai beigt speli
    fun End(): Boolean{
        pointsTogether = player1_points + player2_points
        if (pointsTogether == 8) return true
        else return false
    }
    fun areImagesEqual(imageButton1: ImageButton, imageButton2: ImageButton): Boolean {
        val bitmap1 = (imageButton1.drawable as BitmapDrawable).bitmap
        val bitmap2 = (imageButton2.drawable as BitmapDrawable).bitmap

        return bitmap1.sameAs(bitmap2)
    }
    // vai kartinas vienādas
    fun Match () :Boolean{
        //runBlocking {
          //  delay(5000)
        //}
        Log.i("MainActivity", "Match cards "+card1 +" un "+card2)
        val areEqual = areImagesEqual(buttons[card1], buttons[card2])

        if    (areEqual) {
            return true
        }
        else {
            return false}
    }
    // aizvert ne match kartinas
    fun close(button : ImageButton){

        val oa1 = ObjectAnimator.ofFloat(button,"scaleX",1f,0f)
        val oa2 = ObjectAnimator.ofFloat(button,"scaleX",0f,1f)
        oa1.interpolator = DecelerateInterpolator()
        oa2.interpolator = AccelerateDecelerateInterpolator()
        oa1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                button.setImageResource(R.drawable.android_cropped)
                oa2.start()
                oa2.setDuration(300)

            }
        })
        oa1.start()
        oa1.setDuration(300)
        Log.i("MainActivity", "closed")
    }
        }


