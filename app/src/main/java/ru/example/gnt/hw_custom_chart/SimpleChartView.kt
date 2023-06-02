package ru.example.gnt.hw_custom_chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class SimpleChartView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var arcTextSize = 0F
    private var arcTextPosition: TextPosition? = null
    private var arcTextColor = 0

    private var circleRadius = 0F

    private val startAngle = -90f

    private var centerX = 0F
    private var centerY = 0F

    var values: List<Pair<Int, Float>> = emptyList()
        set(value) {
            field = value
            if (values.sumOf { it.second.toDouble() }.toFloat() != 100F) {
                throw InvalidColorArgumentsException("Sum of arguments should be equal to 100")
            }
            invalidate()
        }

    private var paint: Paint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    private val arcShapeList = mutableListOf<ArcShape>()


    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.SimpleChartView)
            circleRadius = typedArray.getDimension(
                R.styleable.SimpleChartView_circleRadius,
                DEFAULT_CIRCLE_RADIUS
            )
            arcTextColor =
                typedArray.getColor(R.styleable.SimpleChartView_arcTextColor, DEFAULT_TEXT_COLOR)
            arcTextSize = typedArray.getDimension(
                R.styleable.SimpleChartView_arcTextSize,
                DEFAULT_TEXT_SIZE
            )
            arcTextPosition =
                when (typedArray.getInteger(R.styleable.SimpleChartView_arcTextPosition, 0)) {
                    1 -> TextPosition.EDGE
                    2 -> TextPosition.MIDDLE
                    3 -> TextPosition.END
                    else -> TextPosition.MIDDLE
                }
            typedArray.recycle()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        initDimensions()
        createArcShapes()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        arcShapeList.forEach { shapeWithText ->
            with(shapeWithText) {
                paint.color = color
                canvas?.drawArc(
                    left,
                    top,
                    right,
                    bottom,
                    currentAngle,
                    sweepAngle,
                    true,
                    paint
                )
                paint.apply {
                    color = arcTextColor
                    textSize = arcTextSize
                }
                canvas?.drawText(
                    percentageText,
                    textX,
                    textY,
                    paint
                )
            }
        }
    }

    private fun initDimensions() {
        circleRadius = if (circleRadius == 0F) (min(width, height) / 2).toFloat() else circleRadius
        arcTextSize = if (arcTextSize == 0F) DEFAULT_TEXT_SIZE else arcTextSize
        arcTextColor = if (arcTextColor == 0) DEFAULT_TEXT_COLOR else arcTextColor

        centerX = width / 2f
        centerY = height / 2f
    }


    private fun createArcShapes() {
        paint.style = Paint.Style.FILL

        var currentAngle = startAngle

        values.forEach { pair ->

            val sweepAngle = pair.second * 3.6f
            val textPosition: Float = arcTextPosition?.position ?: DEFAULT_TEXT_POSITION

            val textX =
                centerX + (circleRadius / textPosition) * cos(Math.toRadians((currentAngle + sweepAngle / 2f).toDouble())).toFloat()
            val textY =
                centerY + (circleRadius / textPosition) * sin(Math.toRadians((currentAngle + sweepAngle / 2f).toDouble())).toFloat() + (arcTextSize / 2f)

            arcShapeList.add(
                ArcShape(
                    left = centerX - circleRadius,
                    top = centerY - circleRadius,
                    right = centerX + circleRadius,
                    bottom = centerY + circleRadius,
                    currentAngle = currentAngle,
                    sweepAngle = sweepAngle,
                    color = pair.first,
                    textX = textX,
                    textY = textY,
                    percentageText = "${pair.second.toInt()}%"
                )
            )
            currentAngle += sweepAngle
        }
    }

    private inner class ArcShape(
        val left: Float,
        val right: Float,
        val top: Float,
        val bottom: Float,
        val currentAngle: Float,
        val sweepAngle: Float,
        val color: Int,
        val textX: Float,
        val textY: Float,
        val percentageText: String
    )

    private inner class InvalidColorArgumentsException(override val message: String?) :
        Exception(message)

    private enum class TextPosition(
        val position: Float
    ) {
        EDGE(1.15F),
        MIDDLE(DEFAULT_TEXT_POSITION),
        END(3F)
    }

    companion object {
        private const val DEFAULT_CIRCLE_RADIUS = 0F
        private const val DEFAULT_TEXT_SIZE = 40F
        private const val DEFAULT_TEXT_COLOR = Color.BLACK
        private const val DEFAULT_TEXT_POSITION = 1.7F
    }
}

