package com.example.memory_game

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

class One_name : Fragment() {

    lateinit var player: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_one_name, container, false)
        val start =view.findViewById<Button>(R.id.starts)

        start.setOnClickListener{
            val editText : EditText= view.findViewById(R.id.playerOne)

            if (editText.text.toString().trim().isNotEmpty()) {
                player = editText.text.toString().trim()

                val intent = Intent(requireContext(), PvCMode::class.java)
                intent.putExtra("player", player)
                startActivity(intent)
            }
        }
        return view
    }

    }
