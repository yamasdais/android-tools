package yamasdais.gmail.com.toolbox

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Paint
import java.util.*

fun getResourceOtherLocale(context: Context, targetLocale: Locale): Resources {
    val dLocale = Locale.getDefault()
    return if (dLocale == targetLocale) {
        context.resources
    } else {
        context.createConfigurationContext(Configuration(context.resources.configuration).apply {
            setLocale(targetLocale)
        }).resources
    }
}

fun getStringOtherLocale(resource: Resources): (Int)->String = {
    id: Int -> resource.getString(id)
}

fun calculateFontRect(paint: Paint, message: String?): Pair<Float, Float>
        = Pair(paint.measureText(message ?: "H"), paint.getFontMetrics(null))

data class AttributeReader<T>(
        val accessor: TypedArray.(Int, T)->T,
        val resourceId: Int,
        val defValue: T,
        val runIfFound: (T)->Unit) {
    fun readAttribute(ta: TypedArray) {
        if (ta.hasValue(resourceId)) {
            runIfFound(ta.accessor(resourceId, defValue))
        }
    }
}

fun <T> readAttribute(ta: TypedArray, reader: AttributeReader<T>) {
    reader.readAttribute(ta)
}

