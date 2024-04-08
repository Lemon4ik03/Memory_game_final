package com.example.memory_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.content.Intent
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.memory_game.databinding.ActivityChooseModeBinding

class Choose_mode : AppCompatActivity() {

    lateinit var binding: ActivityChooseModeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_mode)
        binding = ActivityChooseModeBinding.inflate(layoutInflater)

        val text_box = findViewById<TextView>(R.id.choose_game_mode)
        val b_pvp = findViewById<Button>(R.id.button_pvp)
        val b_pvc = findViewById<Button>(R.id.button_pvc)
        //val start = findViewById<Button>(R.id.start)


        val input_one_name = One_name()
        val input_names = Input_names()

        b_pvp.setOnClickListener {
            text_box.visibility = View.INVISIBLE
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout2, input_names)
                .commit()
        }
        b_pvc.setOnClickListener {
            text_box.visibility = View.INVISIBLE
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout2, input_one_name)
                .commit()
        }

    }}

         /*val player1 =findViewById<EditText>(R.id.player1)
        val player2 =findViewById<EditText>(R.id.player2)
        start.setOnClickListener{
            if (isEditTextFilled(player1) && isEditTextFilled(player2)) {
                val Intent = Intent(this,MainActivity::class.java)
                    startActivity(Intent)
            }
        }*/






