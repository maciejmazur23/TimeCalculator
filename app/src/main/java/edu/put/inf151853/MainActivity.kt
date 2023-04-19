package edu.put.inf151853

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun runDateCalculator(v: View){
        val i = Intent(this, DateActivity::class.java)
        startActivity(i)
    }

    fun runTimeCalculator(v: View){
        val i = Intent(this, TimeActivity::class.java)
        startActivity(i)
    }
}