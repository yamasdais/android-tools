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
