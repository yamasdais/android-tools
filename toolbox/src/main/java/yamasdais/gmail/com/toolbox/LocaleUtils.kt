package yamasdais.gmail.com.toolbox

import java.util.*
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Paint

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

fun getStringOtherLocale(resource: Resources) = {
    id: Int -> resource.getString(id)
}

inline fun calculateFontRect(paint: Paint, resId: Int, retriever: (Int) -> String?)
        = Pair(paint.measureText(retriever(resId) ?: "H"), paint.getFontMetrics(null))
