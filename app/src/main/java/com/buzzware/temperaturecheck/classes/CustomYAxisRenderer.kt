import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import com.buzzware.temperaturecheck.R
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.renderer.YAxisRenderer
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler
class CustomYAxisRenderer(
    viewPortHandler: ViewPortHandler,
    yAxis: YAxis,
    trans: Transformer,
    private val context: Context
) : YAxisRenderer(viewPortHandler, yAxis, trans) {

    private val iconMap = mapOf(
        1f to R.drawable.emoji_1,  // 😊 Very Happy
        2f to R.drawable.emoji_2,  // 🙂 Happy
        3f to R.drawable.emoji_3,  // 😐 Neutral
        4f to R.drawable.emoji_4,  // ☹️ Sad
        5f to R.drawable.emoji_5   // 😢 Very Sad
    )

    override fun drawYLabels(c: Canvas, fixedPosition: Float, positions: FloatArray, offset: Float) {
        val paint = Paint().apply {
            textAlign = Paint.Align.CENTER
        }

        for (i in positions.indices step 2) {
            val y = positions[i + 1] // Y-coordinate
            val labelValue = positions[i + 1].toInt().toFloat() // Convert to integer float (e.g., 1.0, 2.0, etc.)

            iconMap[labelValue]?.let { iconResId ->
                val bitmap = BitmapFactory.decodeResource(context.resources, iconResId)
                val iconSize = 80 // Increase size if necessary
                val scaledBitmap = Bitmap.createScaledBitmap(bitmap, iconSize, iconSize, false)

                // Adjust position for better visibility
                val xOffset = fixedPosition - iconSize - 20 // Shift icons slightly to the left
                val yOffset = y - (iconSize / 2) // Center vertically

                c.drawBitmap(scaledBitmap, xOffset, yOffset, paint)
            }
        }
    }
}

