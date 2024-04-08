package com.example.memory_game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.os.Handler
import android.widget.TextView

class Welcome_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)

        val startGame = findViewById<Button>(R.id.button2)
        val tap = findViewById<TextView>(R.id.tap)
        val handler = Handler()
        handler.postDelayed({tap.visibility=View.VISIBLE},1500)
        handler.postDelayed({startGame.visibility= View.VISIBLE},1500)
        startGame.setOnClickListener{
            val Intent = Intent(this,Choose_mode::class.java)
            startActivity(Intent)
        }
    }


}