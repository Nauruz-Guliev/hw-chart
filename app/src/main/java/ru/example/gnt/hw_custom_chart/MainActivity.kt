package ru.example.gnt.hw_custom_chart

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<SimpleChartView>(R.id.chart_view).values =
            listOf(
                Pair(Color.RED, 40f),
                Pair(Color.GREEN, 20f),
                Pair(Color.BLUE, 30f),
                Pair(Color.YELLOW, 10f),
            )

    }
}
