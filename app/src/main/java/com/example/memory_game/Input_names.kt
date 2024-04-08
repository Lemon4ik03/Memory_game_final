package com.example.memory_game

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

class Input_names : Fragment() {

    lateinit var player1: String
    lateinit var player2: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_input_names, container, false)
        val start =view.findViewById<Button>(R.id.starts)

        start.setOnClickListener{
            val editText1 : EditText = view.findViewById(R.id.player1)
            val editText2 : EditText = view.findViewById(R.id.player2)
            if (editText1.text.toString().trim().isNotEmpty() and editText2.text.toString().trim().isNotEmpty() ) {
                player1 = editText1.text.toString().trim()
                player2 = editText2.text.toString().trim()
                val intent = Intent(requireContext(), PvPMode::class.java)
                intent.putExtra("player1", player1)
                intent.putExtra("player2", player2)
                startActivity(intent)
            }
        }
        return view
    }

}











