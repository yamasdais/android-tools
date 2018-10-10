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

package yamasdais.gmail.com.localeselector

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import yamasdais.gmail.com.toolbox.measureChildViewVertical
import yamasdais.gmail.com.toolbox.separateMeasureValues
import java.util.*

/**
 * TODO: document your custom view class.
 */

fun ViewManager.langCountryIconView(theme: Int = 0) = langCountryIconView(theme) {}
inline fun ViewManager.langCountryIconView(theme: Int = 0, init: LangCountryIconView.() -> Unit)
        = ankoView({ LangCountryIconView(it) }, theme, init)

class LangCountryIconView : LinearLayout {
    private var imageWpH: Double = 0.0
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val wPadding = paddingStart + paddingEnd
        val hPadding = paddingTop + paddingBottom
        val (widthMode, widthSize) = separateMeasureValues(widthMeasureSpec)
        val (langWidth, langHeight) = measureChildViewVertical(languageView!!,
                MeasureSpec.makeMeasureSpec(widthSize - wPadding, widthMode), heightMeasureSpec)
        val (contWidth, contHeight) = measureChildViewVertical(countryView!!,
                MeasureSpec.makeMeasureSpec(widthSize - wPadding, widthMode), heightMeasureSpec)
        val textWidth = Math.max(langWidth, contWidth)
        val textHeight = langHeight + contHeight
        val w = when(MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(widthMeasureSpec)
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED
            -> Math.max(textWidth, MeasureSpec.getSize(widthMeasureSpec))
            else -> { -1 }
        }
        val imageHeightIntrinsic = (w/imageWpH).toInt()
        val h = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY ->
                MeasureSpec.getSize(heightMeasureSpec) - textHeight
            MeasureSpec.AT_MOST ->
                Math.min(imageHeightIntrinsic, MeasureSpec.getSize(heightMeasureSpec) - textHeight)
            MeasureSpec.UNSPECIFIED ->
                imageHeightIntrinsic
            else -> { -1 }
        } - hPadding
        measureChildViewVertical(flagImage!!,
                MeasureSpec.makeMeasureSpec(w - wPadding, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(Math.max(10, h - hPadding), MeasureSpec.EXACTLY))
        setMeasuredDimension(w, h + textHeight + hPadding)
    }
    fun onMeasure1(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val texts = sequenceOf(languageView, countryView).filterNotNull()
        val margins = texts.map {
            val lp = it.layoutParams
            if (it.visibility != View.GONE && lp is MarginLayoutParams) {
                Pair(lp.marginStart + lp.marginEnd, lp.topMargin + lp.bottomMargin)
            } else {
                Pair(0, 0)
            }
        }
        val padding = Pair(paddingStart + paddingEnd, paddingTop + paddingBottom)
        texts.zip(margins).forEach {
            val (text, margin) = it
            text.measure(
                    MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) - margin.first - padding.first,
                            MeasureSpec.getMode(widthMeasureSpec)),
                    MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) - margin.second,
                            MeasureSpec.getMode(heightMeasureSpec)))
        }
        val textWidth = texts.filterNot {
            it.visibility == View.GONE
        }.map {
            it.measuredWidth + it.paddingStart + it.paddingEnd
        } .max() ?: 1
        val textHeight = texts.filterNot {
            it.visibility == View.GONE
        }.sumBy {
            it.measuredHeight + it.paddingTop + it.paddingBottom
        }
        val w = when(MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(widthMeasureSpec)
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED
            -> Math.max(textWidth, MeasureSpec.getSize(widthMeasureSpec))
            else -> { -1 }
        }
        val imageHeightIntrinsic = (w/imageWpH).toInt()
        val flagMargin = flagImage?.let {
            val lp = it.layoutParams
            if (lp is MarginLayoutParams) {
                Pair(lp.marginStart + lp.marginEnd, lp.topMargin + lp.bottomMargin)
            } else {
                Pair(0, 0)
            }
        } ?: Pair(0, 0)
        val h = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY ->
                MeasureSpec.getSize(heightMeasureSpec) - textHeight
            MeasureSpec.AT_MOST ->
                Math.min(imageHeightIntrinsic, MeasureSpec.getSize(heightMeasureSpec) - textHeight)
            MeasureSpec.UNSPECIFIED ->
                imageHeightIntrinsic
            else -> { -1 }
        } - padding.second
        flagImage?.measure(
                MeasureSpec.makeMeasureSpec(Math.max(10, w - padding.first - flagMargin.first), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(
                        Math.max(10, h - flagMargin.second), MeasureSpec.EXACTLY))
        setMeasuredDimension(w, h + textHeight + padding.second)
    }

    var locale: Locale? = null
        set(value) {
            if (field != value) {
                field = value
                onSetLocale()
            }
        }

    private var languageView: TextView? = null
    private var countryView: TextView? = null
    private var flagImage: ImageView? = null

    constructor(context: Context) : super(context) {
        init(null, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int, defStyleRes: Int)
            :super(context, attrs, defStyle, defStyleRes) {
        init(attrs, defStyle, defStyleRes)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int, defStyleRes: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.LangCountryIconView, defStyle, defStyleRes)
        layout(attrs, a)
        a.recycle()
    }

    private fun onSetLocale() {
        locale?.let {
            languageView?.text = it.displayLanguage ?: "(language)"
            countryView?.text = it.displayCountry ?: "(country)"
            setImage()
        }
    }

    private fun setImage() {
        val image = R.drawable.ic_jp
        flagImage?.let {
            it.setImageResource(image)
            imageWpH = it.drawable.intrinsicWidth.toDouble() / it.drawable.intrinsicHeight.toDouble()
            it.backgroundColor = Color.LTGRAY
        }
    }

    private fun layout(attrs: AttributeSet?, typedArray: TypedArray) {
        AnkoContext.createDelegate(this).apply {
            orientation = VERTICAL
            /*
            flagImage = imageView {
                scaleType = ImageView.ScaleType.FIT_CENTER
                layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
            }
            */
            flagImage = createFlagImage(typedArray)
            /*
            languageView = themedTextView {
                layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
            }
            */
            languageView = createTextView(typedArray.getResourceId(R.styleable.LangCountryIconView_languageTextLayout,
                    R.layout.lang_country_flag_language_default))
            /*
            countryView = textView {
                backgroundColor = Color.GREEN
                layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
            }
            */
            countryView = createTextView(typedArray.getResourceId(R.styleable.LangCountryIconView_countryTextLayout,
                    R.layout.lang_country_flag_country_default))
            layoutParams = LinearLayout.LayoutParams(context, attrs)
        }
    }

    private fun createFlagImage(typedArray: TypedArray): ImageView {
        val lo = typedArray.getResourceId(R.styleable.LangCountryIconView_flagImageLayout,
                R.layout.lang_country_flag_image_default)
        return if (lo != 0) {
            include(lo) {

            }
        } else {
            imageView {
                scaleType = ImageView.ScaleType.FIT_CENTER
                layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
            }
        }
    }

    private fun createTextView(lo: Int): TextView {
        return if (lo != 0) {
            include(lo) {
            }
        } else {
            textView {
                layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
            }
        }
    }
}
