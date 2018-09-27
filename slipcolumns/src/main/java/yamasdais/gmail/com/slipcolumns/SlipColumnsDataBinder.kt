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

package yamasdais.gmail.com.slipcolumns

import android.view.View

abstract class SlipColumnsDataBinder {
    abstract fun getColumnCount(): Int

    var columnPosition: Int = -1

    abstract fun get(i: Int): Any

    abstract fun createView(): View

    open val prevColumnItem: Any
        get() =
            if (columnPosition == 0) {
                ""
            } else {
                get(columnPosition - 1)
            }

    open val nextColumnItem: Any
        get() =
            if (columnPosition + 1 == getColumnCount()) {
                ""
            } else {
                get(columnPosition + 1)
            }

    open val currentItem: Any
        get() = get(columnPosition)

    open fun prevColumn(): Any =
            if (columnPosition < 1) {
                throw IndexOutOfBoundsException("Cannot move to previous item from: $columnPosition")
            } else {
                get(--columnPosition)
            }

    open fun nextColumn(): Any =
            if (columnPosition + 1 >= getColumnCount()) {
                throw IndexOutOfBoundsException("Cannot exceed ${getColumnCount()} from: $columnPosition")
            } else {
                get(++columnPosition)
            }
}
