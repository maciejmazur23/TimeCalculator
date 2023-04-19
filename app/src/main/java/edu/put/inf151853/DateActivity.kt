package edu.put.inf151853

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.floor

class DateActivity : AppCompatActivity() {
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy")
    private var numberDaysBetweenDates = 0L
    private var firstNumberOfDays = LocalDate.now().dayOfMonth
    private var firstNumberOfMonths = LocalDate.now().monthValue
    private var firstNumberOfYears = LocalDate.now().year
    private var secondNumberOfDays = LocalDate.now().dayOfMonth
    private var secondNumberOfMonths = LocalDate.now().monthValue
    private var secondNumberOfYears = LocalDate.now().year
    private lateinit var businessDaysBetweenDates: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date)

        val firstDate: DatePicker = findViewById(R.id.firstDatePicker)
        val secondDate: DatePicker = findViewById(R.id.secondDatePicker)
        val numberOfDays: TextView = findViewById(R.id.numberOfDays)
        this.businessDaysBetweenDates = findViewById(R.id.businessDaysBetweenDates)

        setData(firstDate.dayOfMonth, firstDate.month, firstDate.year, 0)
        setData(secondDate.dayOfMonth, secondDate.month, secondDate.year, 1)

        firstDate.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            setData(dayOfMonth, monthOfYear, year, 0)
            setDaysBetweenDates()
            setText(numberOfDays)
        }

        secondDate.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            setData(dayOfMonth, monthOfYear, year, 1)
            setDaysBetweenDates()
            setText(numberOfDays)
        }
    }

    private fun setDaysBetweenDates() {
        val from = LocalDate.parse(
            String.format("${this.firstNumberOfDays}/${this.firstNumberOfMonths}/${this.firstNumberOfYears}"),
            dateFormatter
        )
        val to = LocalDate.parse(
            String.format("${this.secondNumberOfDays}/${this.secondNumberOfMonths}/${this.secondNumberOfYears}"),
            dateFormatter
        )
        val between = ChronoUnit.DAYS.between(from, to)
        this.numberDaysBetweenDates = between
    }

    private fun setData(day: Int, month: Int, year: Int, choice: Int) {
        if (choice == 0) {
            this.firstNumberOfDays = day
            this.firstNumberOfMonths = month + 1
            this.firstNumberOfYears = year
        } else {
            this.secondNumberOfDays = day
            this.secondNumberOfMonths = month + 1
            this.secondNumberOfYears = year
        }
        calculateWorkingDays()
    }

    fun plus(v: View) {
        val number: EditText = findViewById(R.id.numberOfDays)
        val days = number.text.toString().toInt()
        this.numberDaysBetweenDates = days.toLong()
        setDate(days)
    }

    @SuppressLint("SetTextI18n")
    private fun calculateWorkingDays() {
        var numberOfWorkDays = 0

        var start = LocalDate.parse(
            String.format(
                "${this.firstNumberOfDays}/${this.firstNumberOfMonths}/${this.firstNumberOfYears}"
            ), this.dateFormatter
        )
        val end = LocalDate.parse(
            String.format(
                "${this.secondNumberOfDays}/${this.secondNumberOfMonths}/${this.secondNumberOfYears}"
            ), this.dateFormatter
        )

        val calendar = Calendar.getInstance()
        while (end > start) {
            start = start.plusDays(1)

            val instant = start.atStartOfDay(ZoneOffset.ofHours(1)).toInstant()
            calendar.time = Date.from(instant)
            val get = calendar.get(Calendar.DAY_OF_WEEK)

            if (!(get == 1 || get == 7 || otherWorkDay(start))) {
                numberOfWorkDays++
            }
        }
        this.businessDaysBetweenDates.text = "Dni roboczych pomiędzy datami: $numberOfWorkDays"
    }

    private fun otherWorkDay(start: LocalDate): Boolean {
        val january1 = LocalDate.of(start.year, 1, 1)
        val january6 = LocalDate.of(start.year, 1, 6)
        val may1 = LocalDate.of(start.year, 1, 6)
        val may3 = LocalDate.of(start.year, 1, 6)
        val august15 = LocalDate.of(start.year, 1, 6)
        val november1 = LocalDate.of(start.year, 1, 6)
        val november11 = LocalDate.of(start.year, 1, 6)
        val december25 = LocalDate.of(start.year, 1, 6)
        val december26 = LocalDate.of(start.year, 1, 6)
        val easter = calculateEster(start)

        if (easter != null) {
            return if (start == january1 || start == january6 || start == may1 || start == may3 ||
                start == august15 || start == november1 || start == november11 ||
                start == december25 || start == december26
            ) true else start == (easter.plusDays(1)) ||
                    start == (easter.plusDays(60))
        } else throw RuntimeException("The date of Easter cannot be counted!")
    }

    private fun calculateEster(start: LocalDate): LocalDate? {
        val a = start.year % 19
        val b = floor((start.year / 100).toDouble())
        val c = start.year % 100
        val d = floor(b / 4)
        val e = b % 4
        val f = floor((b + 8) / 25)
        val g = floor(((b - f) + 1) / 3)
        val h = (19 * a + b - d - g + 15) % 30
        val i = floor((c / 4).toDouble())
        val k = c % 4
        val l = (32 + 2 * e + 2 * i - h - k) % 7
        val m = floor((a + 11 * h + 22 * l) / 451)
        val p = (h + l - 7 * m + 114) % 31
        val day = p + 1
        val month = floor(h + l - 7 * m + 114) / 31

        return LocalDate.of(start.year, month.toInt(), day.toInt())
    }

    private fun setDate(days: Int) {
        val from = LocalDate.parse(
            String.format(
                "${this.firstNumberOfDays}/${this.firstNumberOfMonths}/${this.firstNumberOfYears}"
            ), this.dateFormatter
        )
        val newDate = from.plusDays(days.toLong())
        val picker: DatePicker = findViewById(R.id.secondDatePicker)

        this.secondNumberOfYears = newDate.year
        this.secondNumberOfMonths = newDate.monthValue - 1
        this.secondNumberOfDays = newDate.dayOfMonth

        picker.updateDate(
            this.secondNumberOfYears,
            this.secondNumberOfMonths,
            this.secondNumberOfDays
        )
        calculateWorkingDays()
    }

    private fun setText(daysBetweenDates: TextView) {
        daysBetweenDates.text = this.numberDaysBetweenDates.toString()
    }
}