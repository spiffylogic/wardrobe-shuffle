package me.spiffylogic.wardrobeshuffle

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val manageButton = findViewById<Button>(R.id.manage_button)
        manageButton.setOnClickListener {
            startActivity(Intent(this, ManageActivity::class.java))
        }

        val shuffleButton = findViewById<Button>(R.id.shuffle_button)
        shuffleButton.setOnClickListener({ _ ->
            //Snackbar.make(view, "Tell me what to wear", Snackbar.LENGTH_LONG).show()
            startActivity(Intent(this, ShuffleActivity::class.java))
        })
    }
}
