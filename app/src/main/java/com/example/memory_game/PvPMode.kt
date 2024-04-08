package com.example.memory_game

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import java.io.File
import org.apache.commons.io.FileUtils
import java.io.FileInputStream
import java.io.IOException


class PvPMode : AppCompatActivity() {

    private lateinit var buttons: List<ImageButton>

    var card1: Int = 0
    var card2: Int = 0
    var opened :Int =0;
    var buttonClicked = BooleanArray(0)
    var currentPlayer : Int = 1
    var pointsTogether: Int =0
    //punktu glābšanai

    var player1_points = 0;
    var player2_points = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // foto pievienošana massīvā (list)
        val cards = mutableListOf(
            R.drawable.lolipop, R.drawable.cupcake, R.drawable.donut, R.drawable.eclair,
            R.drawable.gingerbread, R.drawable.kitkat, R.drawable.marshmallow, R.drawable.pie
        )
        val pl1_p = findViewById<TextView>(R.id.player2_p)
        val pl2_p = findViewById<TextView>(R.id.player1_p)

        // dati no iepriekšēja activity, fragmenta
        val player1 = intent.getStringExtra("player1")
        val player2 = intent.getStringExtra("player2")
        val pl1_box = findViewById<TextView>(R.id.player1_score)
        pl1_box.text =player1 + "'s points: "
        val pl2_box = findViewById<TextView>(R.id.player2_score)
        pl2_box.text =player2 + "'s points: "

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

                if (End()){
                    val endbox = findViewById<FrameLayout>(R.id.win_box)
                    val endtext = findViewById<TextView>(R.id.win)
                    endbox.visibility = View.VISIBLE
                    endtext.visibility = View.VISIBLE
                    if (player1_points > player2_points){
                        endtext.text = player1 +" Win !!!"}
                    else if (player1_points ==player2_points){
                        endtext.text = "Draw !!!"
                    } else
                    endtext.text = player2 +" Win !!!"
                }
                nextStep(pl1_p,pl2_p)


                // kartiņas animācijai
            val oa1 = ObjectAnimator.ofFloat(button,"scaleX",1f,0f)
            val oa2 = ObjectAnimator.ofFloat(button,"scaleX",0f,1f)
            oa1.interpolator = DecelerateInterpolator()
            oa2.interpolator = AccelerateDecelerateInterpolator()

            if (!buttonClicked[index]){
                if (opened < 2) {
                    oa1.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)

                            if (button.drawable.constantState == resources.getDrawable(R.drawable.android_cropped).constantState) {
                                button.setImageResource(cards[index])
                                opened++
                            } //else button.setImageResource(R.drawable.android_cropped)
                            oa2.start()
                            oa2.setDuration(500)

                        }
                    })
                    oa1.start()
                    oa1.setDuration(500)

                    buttonClicked[index] = true
                    if (card1 == 0){
                    card1 = index}
                    else if (card2 == 0){
                        card2 =index
                    }

                }
            }

        }}



    }

// ja atverti 2 kartinas, tad vai aizvert vinus vai atstat (ka ari anulet vertibas card1, card2)
    fun nextStep(pl1_p :TextView,pl2_p:TextView){
        if (opened == 2){
            opened = 0
            if (!Match() ){
                buttonClicked[card1] = false
                buttonClicked[card2] = false
                close(buttons[card1])
                close(buttons[card2])
                card1 =0
                card2 =0
                if (currentPlayer == 1) {
                    currentPlayer = 2
                } else if (currentPlayer == 2){
                    currentPlayer = 1
                }
            }
            else {
                card1 =0
                card2 =0
                if (currentPlayer == 1){
                    player1_points++
                    pl1_p.text = player1_points.toString()
                }
                else if (currentPlayer ==2){
                    player2_points++
                    pl2_p.text = player2_points.toString()
                }
                }
            }
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
    // Сравниваем файлы

 // vai kartinas vienādas
    fun Match () :Boolean{
     val areEqual = areImagesEqual(buttons[card1], buttons[card2])

         //val areFilesEqual = file.readBytes().contentEquals(file2.readBytes())
        //Log.i("MainActivity",buttons[card1].drawable.constantState.toString() +" un "+ buttons[card2].drawable.constantState.toString())
        //if (buttons[card1].drawable.constantState == buttons[card2].drawable.constantState){
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
                oa2.setDuration(500)

            }
        })
        oa1.start()
        oa1.setDuration(500)


        Log.i("MainActivity", "closed")
    }

    }










