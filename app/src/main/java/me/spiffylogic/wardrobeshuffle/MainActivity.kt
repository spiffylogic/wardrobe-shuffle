package me.spiffylogic.wardrobeshuffle

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manage_button.setOnClickListener {
            startActivity(Intent(this, ManageActivity::class.java))
        }

        shuffle_button.setOnClickListener({ _ ->
            //Snackbar.make(view, "Tell me what to wear", Snackbar.LENGTH_LONG).show()
            startActivity(Intent(this, ShuffleActivity::class.java))
        })
    }
}
