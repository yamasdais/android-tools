package yamasdais.gmail.com.columnshifter

import android.content.Context
import android.graphics.Canvas
import android.view.View
import android.view.View.MeasureSpec
import java.util.*

abstract class ColumnShifterAdapter {
    abstract fun getCount(): Int

    var position: Int = -1
        get() = field
        set(value) {
            field = value
        }

    abstract fun get(i: Int): Any

    abstract fun createView(): View

    open val prevItem: Any
        get() =
            if (position == 0) {
                ""
            } else {
                get(position - 1)
            }

    open val nextItem: Any
        get() =
            if (position + 1 == getCount()) {
                ""
            } else {
                get(position + 1)
            }

    open val currentItem: Any
        get() = get(position)

    open fun prev(): Any =
            if (position < 1) {
                throw IndexOutOfBoundsException("Cannot move to previous item from: ${position}")
            } else {
                get(--position)
            }

    open fun next(): Any =
            if (position + 1 >= getCount()) {
                throw IndexOutOfBoundsException("Cannot exceed ${getCount()} from: ${position}")
            } else {
                get(++position)
            }
}