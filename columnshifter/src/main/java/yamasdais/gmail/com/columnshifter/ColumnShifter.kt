package yamasdais.gmail.com.columnshifter

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.column_shifter.view.*
import yamasdais.gmail.com.toolbox.*
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.util.*

// TODO attr によるプロパティ設定
// TODO 文字列から最小幅を計算する処理の実装(button)
open class ColumnShifter @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
        ): LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    private val listeners: PropertyChangeSupport
    private var initialPosition: Int = 0
    private var prevInAnimation: Int = android.R.anim.slide_in_left
    private var prevOutAnimation: Int = android.R.anim.slide_out_right
    private var nextInAnimation: Int = R.anim.slide_in_right
    private var nextOutAnimation: Int = R.anim.slide_out_left
    private var textMetricsStandard: Int = R.string.defaultFontMetricsStandard
    private var buttonMetricsStandard: Int = R.string.defaultButtonMetricsStandard
    var textFontSize: Float = 120f
    private var buttonFontSize: Float = 80f

    private val onGestureListener = object: GestureDetector.OnGestureListener {
        override fun onShowPress(e: MotionEvent?) {
            //Log.d("onShowPress", "Event: $e")
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            //Log.d("onSingleTapUp", "Event: $e")
            return false
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            //Log.d("onScroll","Event1:$e1, Event2:$e2, X: $distanceX, Y: $distanceY")
            return false
        }

        override fun onLongPress(e: MotionEvent?) {
            //Log.d("onLongPress", "Event: $e")
        }

        override fun onDown(e: MotionEvent?): Boolean {
            //Log.d("onDown", "Event: $e")
            return false
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            val xyRatio = velocityX / Math.abs(velocityY)
            //Log.d("onFling", "X: $velocityX, Y: $velocityY, X/Y: $xyratio")
            if (Math.abs(xyRatio) > 1.0) {
                if (xyRatio > 0) {
                    movePrevious()
                } else {
                    moveNext()
                }
                return true
            }
            return false
        }
    }
    private val gestureDetector: GestureDetector = GestureDetector(context, onGestureListener)

    /*
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event) ?: super.onTouchEvent(event)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        //Log.d("dispatchTouchEvent", "Event: $ev")
        //gestureDetector.onTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
        //super.dispatchTouchEvent(ev)
        //return gestureDetector.onTouchEvent(ev)
    }
    */

    var locale: Locale = Locale.getDefault()
    set(value) {
        if (value != field) {
            Log.d("locale", "Changed to: $field")
            field = value
            val res = getResourceOtherLocale(context, locale)
            val getMessage = getStringOtherLocale(res)
            val spCoeff = res.displayMetrics.scaledDensity
            val paint = Paint().apply {
                textLocale = locale
                textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,textFontSize / spCoeff, res.displayMetrics)
            }
            val textMetrics = calculateFontRect(
                    paint, getMessage(textMetricsStandard))
                    .asIterable().iterator().toPair {
                Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, it, res.displayMetrics))
            }
            paint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, buttonFontSize / spCoeff, res.displayMetrics)
            val buttonMetrics = calculateFontRect(
                    paint, getMessage(buttonMetricsStandard))
                    .asIterable().iterator().toPair {
                        Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, it, res.displayMetrics))
                    }
            switcher.minimumWidth = textMetrics.first
            switcher.minimumHeight = textMetrics.second
            prev_button.minimumWidth = buttonMetrics.first
            prev_button.minWidth = buttonMetrics.first
            prev_button.minimumHeight = buttonMetrics.second
            next_button.minimumWidth = buttonMetrics.first
            next_button.minWidth = buttonMetrics.first
            next_button.minimumHeight = buttonMetrics.second
        }
    }

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

        listeners = PropertyChangeSupport(this)

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
                if (a.hasValue(R.styleable.ColumnShifter_textMetricsStandard)) {
                    textMetricsStandard = a.getResourceId(R.styleable.ColumnShifter_textMetricsStandard, textMetricsStandard)
                }
                if (a.hasValue(R.styleable.ColumnShifter_textFontSize)) {
                    textFontSize = a.getDimensionPixelSize(R.styleable.ColumnShifter_textFontSize, textFontSize.toInt()).toFloat()
                }
                if (a.hasValue(R.styleable.ColumnShifter_buttonMetricsStandard)) {
                    buttonMetricsStandard = a.getResourceId(R.styleable.ColumnShifter_buttonMetricsStandard, buttonMetricsStandard)
                }
                if (a.hasValue(R.styleable.ColumnShifter_buttonFontSize)) {
                    buttonFontSize = a.getDimensionPixelSize(R.styleable.ColumnShifter_buttonFontSize, buttonFontSize.toInt()).toFloat()
                }
                a.recycle()
            }

        }
        switcher.setOnTouchListener {
            v, event -> gestureDetector.onTouchEvent(event)
        }
        switcher.setOnClickListener {  }

        prev_button.setOnClickListener {
            movePrevious()
        }

        next_button.setOnClickListener {
            moveNext()
        }
        isDirNext = !isDirNext
    }

    open var data: Any? = null
        set(value) {
            if (field != value) {
                val checker = checkDataUpdate()
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

    private fun updateButtonState() {
        prev_button.isEnabled = (adapter?.position ?: 0) > 0
        next_button.isEnabled = (adapter?.position ?: 0) < (adapter?.getCount() ?: 0) - 1
    }

    private fun checkDataUpdate(): () -> Unit {
        val oldValue = adapter?.currentItem
        return {
            val newValue = adapter?.currentItem
            if (oldValue != newValue) {
                this.listeners.firePropertyChange("item", oldValue, newValue)
            }
        }
    }
    fun movePrevious() {
        adapter?.let {
            if (it.position > 0) {
                isDirNext = false
                val checker = checkDataUpdate()
                switcher.setText(it.prev().toString())
                prev_button.text = it.prevItem.toString()
                next_button.text = it.nextItem.toString()
                checker()
            }

        }
        updateButtonState()
    }

    fun moveNext() {
        adapter?.let {
            if (it.position < it.getCount() - 1) {
                isDirNext = true
                val checker = checkDataUpdate()
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
                switcher.setFactory {
                    (value.createView() as TextView).apply {
                        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, textFontSize)
                    }
                }
                position = initialPosition
            }

    var position: Int
        get() = adapter?.position ?: -1
        set(value) {
            adapter?.let {
                val checker = checkDataUpdate()
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