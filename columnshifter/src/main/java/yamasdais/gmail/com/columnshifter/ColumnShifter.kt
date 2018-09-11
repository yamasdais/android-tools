package yamasdais.gmail.com.columnshifter

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ViewSwitcher
import kotlinx.android.synthetic.main.column_shifter.view.*
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport

// TODO attr によるプロパティ設定
// TODO 文字列から最小幅を計算する処理の実装
open class ColumnShifter @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
        ): LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val listeners = PropertyChangeSupport(this);
    private var initialPosition: Int = 0
    private var prevInAnimation: Int = android.R.anim.slide_in_left
    private var prevOutAnimation: Int = android.R.anim.slide_out_right
    private var nextInAnimation: Int = R.anim.slide_in_right
    private var nextOutAnimation: Int = R.anim.slide_out_left

    private var isDirNext: Boolean = false
        set(value) {
            if (value != field) {
                field = value
                switcher.setInAnimation(context,
                        if (value) { nextInAnimation } else { prevInAnimation })
                switcher.setOutAnimation(context,
                        if (value) { nextOutAnimation } else { prevOutAnimation })
            }
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.column_shifter, this)

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.ColumnShifter)
            a?.let {
                if (a.hasValue(R.styleable.ColumnShifter_initialColumn)) {
                    initialPosition = a.getInteger(R.styleable.ColumnShifter_initialColumn, 0)
                }
                if (a.hasValue(R.styleable.ColumnShifter_prevInAnimation)) {
                    prevInAnimation = a.getResourceId(R.styleable.ColumnShifter_prevInAnimation, prevInAnimation)
                }
                if (a.hasValue(R.styleable.ColumnShifter_prevOutAnimation)) {
                    prevOutAnimation = a.getResourceId(R.styleable.ColumnShifter_prevOutAnimation, prevOutAnimation)
                }
                if (a.hasValue(R.styleable.ColumnShifter_nextInAnimation)) {
                    nextInAnimation = a.getResourceId(R.styleable.ColumnShifter_nextInAnimation, nextInAnimation)
                }
                if (a.hasValue(R.styleable.ColumnShifter_nextOutAnimation)) {
                    nextOutAnimation = a.getResourceId(R.styleable.ColumnShifter_nextOutAnimation, nextOutAnimation)
                }
                a.recycle()
            }

        }

        prev_button.setOnClickListener {
            movePrevious()
        }

        next_button.setOnClickListener {
            moveNext()
        }
        isDirNext = !isDirNext
    }

    open var data: Any? = null
        get () = field
        set(value) {
            if (field != value) {
                val checker = checkDateUpdate()
                field = value
                adapter?.let {
                    switcher.setCurrentText(it.currentItem.toString())
                    prev_button.text = it.prevItem.toString()
                    next_button.text = it.nextItem.toString()
                }
                checker()
            }
        }

    val item: Any?
        get() = adapter?.currentItem

    fun updateButtonState() {
        prev_button.isEnabled = (adapter?.position ?: 0) > 0
        next_button.isEnabled = (adapter?.position ?: 0) < (adapter?.getCount() ?: 0) - 1
    }

    fun checkDateUpdate(): () -> Unit {
        val oldValue = adapter?.currentItem
        return {
            var newValue = adapter?.currentItem
            if (oldValue != newValue) {
                this.listeners.firePropertyChange("item", oldValue, newValue)
            }
        }
    }
    fun movePrevious() {
        isDirNext = false
        adapter?.let {
            if (it.position > 0) {
                val checker = checkDateUpdate()
                switcher.setText(it.prev().toString())
                prev_button.text = it.prevItem.toString()
                next_button.text = it.nextItem.toString()
                checker()
            }

        }
        updateButtonState()
    }

    fun moveNext() {
        isDirNext = true
        adapter?.let {
            if (it.position < it.getCount() - 1) {
                val checker = checkDateUpdate()
                switcher.setText(it.next().toString())
                prev_button.text = it.prevItem.toString()
                next_button.text = it.nextItem.toString()
                checker()
            }
        }
        updateButtonState()
    }

    open var adapter: ColumnShifterAdapter? = null
            set(value) {
                field = value!!
                switcher.setFactory(object: ViewSwitcher.ViewFactory {
                    override fun makeView(): View = value.createView()
                })
                position = initialPosition
            }

    var position: Int
        get() = adapter?.position ?: -1
        set(value) {
            adapter?.let {
                val checker = checkDateUpdate()
                it.position = value
                switcher.setCurrentText(it.currentItem.toString())
                prev_button.text = it.prevItem.toString()
                next_button.text = it.nextItem.toString()
                checker()
            }
            updateButtonState()
        }

    fun addPropertyChangeListener(listener: PropertyChangeListener) =
            listeners.addPropertyChangeListener(listener)

    fun removePropertyChangeListener(listener: PropertyChangeListener) =
            listeners.removePropertyChangeListener(listener)
}