package ru.example.gnt.hw_custom_chart

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.example.gnt.hw_custom_chart.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.chartView.values =
            listOf(
                Pair(Color.RED, 40f),
                Pair(Color.GREEN, 20f),
                Pair(Color.BLUE, 30f),
                Pair(Color.YELLOW, 10f),
            )

    }
}
