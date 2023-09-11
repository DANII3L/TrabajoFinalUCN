package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity2 : AppCompatActivity() {
    private lateinit var txtBienvenida : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val extras = intent.extras
        if (extras != null) {
            txtBienvenida = findViewById(R.id.txtBienvenida)
            txtBienvenida.setText( "Bienvenido ${extras.getString("nombre")}!")
        }

    }
}