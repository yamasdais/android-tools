/*
 * MIT License
 *
 * Copyright (c) 2018. Yamasdais@gmail.com.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

