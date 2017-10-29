package me.spiffylogic.wardrobeshuffle

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.support.design.widget.Snackbar
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val manageButton = findViewById<Button>(R.id.manage_button)
        manageButton.setOnClickListener {
            Snackbar.make(it, "Go to manage wardrobe (inventory)", Snackbar.LENGTH_LONG).show()
        }

        val shuffleButton = findViewById<Button>(R.id.shuffle_button)
        shuffleButton.setOnClickListener({ view ->
            Snackbar.make(view, "Tell me what to wear", Snackbar.LENGTH_LONG).show()
        })
    }




}
