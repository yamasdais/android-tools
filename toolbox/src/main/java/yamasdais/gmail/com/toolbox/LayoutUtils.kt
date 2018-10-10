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

import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup.MarginLayoutParams

fun separateMeasureValues(measureValue: Int): Pair<Int, Int> {
    return Pair(MeasureSpec.getMode(measureValue), MeasureSpec.getSize(measureValue))
}

fun measureChildViewVertical(view: View, widthMeasure: Int, heightMeasure: Int): Pair<Int, Int> {
    val lp = view.layoutParams
    var vMargin = 0
    var hMargin = 0
    val vPadding = view.paddingTop + view.paddingBottom
    val hPadding = view.paddingStart + view.paddingEnd
    if (view.visibility != View.GONE && lp is MarginLayoutParams) {
        vMargin += lp.topMargin + lp.bottomMargin
        hMargin += lp.marginStart + lp.marginEnd
    }

    view.measure(
            MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasure) - hMargin,
                    MeasureSpec.getMode(widthMeasure)),
            MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasure) - vMargin,
                    MeasureSpec.getMode(heightMeasure)))

    return Pair(
           if (view.visibility == View.GONE) 0 else { view.measuredWidth + hPadding },
           if (view.visibility == View.GONE) 0 else { view.measuredHeight + vPadding }
    )
}