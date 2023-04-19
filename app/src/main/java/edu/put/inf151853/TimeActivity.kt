package edu.put.inf151853

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class TimeActivity : AppCompatActivity() {
    private lateinit var firstHours: EditText
    private lateinit var firstMinutes: EditText
    private lateinit var firstSeconds: EditText
    private lateinit var secondHours: EditText
    private lateinit var secondMinutes: EditText
    private lateinit var secondSeconds: EditText
    private var firstHOrNull: Int? = 0
    private var firstMOrNull: Int? = 0
    private var firstSOrNull: Int? = 0
    private var secondHOrNull: Int? = 0
    private var secondMOrNull: Int? = 0
    private var secondSOrNull: Int? = 0
    private var firstH = 0
    private var firstM = 0
    private var firstS = 0
    private var secondH = 0
    private var secondM = 0
    private var secondS = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)

        this.firstHours = findViewById(R.id.firstHours)
        this.firstMinutes = findViewById(R.id.firstMinutes)
        this.firstSeconds = findViewById(R.id.firstSeconds)
        this.secondHours = findViewById(R.id.secondHours)
        this.secondMinutes = findViewById(R.id.secondMinutes)
        this.secondSeconds = findViewById(R.id.secondSeconds)
    }

    fun plus(v: View) {
        initHMS()
        calculateHours { a, b -> a + b }
        setHours(firstH.toString(), firstM.toString(), firstS.toString())
    }

    fun minus(v: View) {
        initHMS()
        calculateHours { a, b -> a - b }
        setHours(firstH.toString(), firstM.toString(), firstS.toString())
    }

    fun ac(v: View) {
        setHours("", "", "")
    }

    private fun calculateHours(lambda: (Int, Int) -> Int) {
        val firstSum = firstH * 3600 + firstM * 60 + firstS
        val secondSum = secondH * 3600 + secondM * 60 + secondS

        val sum = lambda(firstSum, secondSum)

        firstH = sum / 3600
        firstM = (sum - firstH * 3600) / 60
        firstS = sum - firstH * 3600 - firstM * 60
    }

    private fun initHMS() {
        firstHOrNull = this.firstHours.text.toString().toIntOrNull()
        firstMOrNull = this.firstMinutes.text.toString().toIntOrNull()
        firstSOrNull = this.firstSeconds.text.toString().toIntOrNull()
        secondHOrNull = this.secondHours.text.toString().toIntOrNull()
        secondMOrNull = this.secondMinutes.text.toString().toIntOrNull()
        secondSOrNull = this.secondSeconds.text.toString().toIntOrNull()
        firstH = 0
        firstM = 0
        firstS = 0
        secondH = 0
        secondM = 0
        secondS = 0
        if (firstHOrNull != null) firstH = firstHOrNull as Int
        if (firstMOrNull != null) firstM = firstMOrNull as Int
        if (firstSOrNull != null) firstS = firstSOrNull as Int
        if (secondHOrNull != null) secondH = secondHOrNull as Int
        if (secondMOrNull != null) secondM = secondMOrNull as Int
        if (secondSOrNull != null) secondS = secondSOrNull as Int
    }

    private fun setHours(fH: String, fM: String, fS: String) {
        firstHours.setText(fH)
        firstMinutes.setText(fM)
        firstSeconds.setText(fS)
        secondHours.setText("")
        secondMinutes.setText("")
        secondSeconds.setText("")
    }
}