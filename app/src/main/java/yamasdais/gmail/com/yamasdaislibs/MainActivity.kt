package yamasdais.gmail.com.yamasdaislibs

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import yamasdais.gmail.com.columnshifter.ColumnShifterAdapter
import yamasdais.gmail.com.toolbox.getResourceOtherLocale
import yamasdais.gmail.com.toolbox.getStringOtherLocale
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {

    fun getMusicalString(musicalLocale: Locale): (Int) -> String {
        val res = getResourceOtherLocale(this, musicalLocale)
        return getStringOtherLocale(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = this
        try {
            columnShifter.addPropertyChangeListener(listener = object: PropertyChangeListener {
                override fun propertyChange(evt: PropertyChangeEvent?) {
                    evt?.let {
                        if (evt.propertyName == "item") {
                            //disp_text.text = evt.newValue as? String ?: "<null>"
                            disp_text.text = columnShifter.item as? String ?: "<null>"
                        }
                    }
                }

            })
            columnShifter.adapter = object : ColumnShifterAdapter() {
                val format = arrayOf("%s\u266D\u266D", "%s\u266D", "%s", "%s\u266F", "%s\u266F\u266F")
                val accidental = arrayOf("\u266D\u266D", "\u266D", "♮", "\u266F", "\u266F\u266F")

                override fun getCount(): Int = format.size

                override fun get(i: Int): Any = if (i < 0) { "" } else { String.format(format[i], columnShifter.data) }

                override val prevItem: Any
                    get() =
                        if (position == 0) {
                            ""
                        } else {
                            accidental[position-1]
                        }
                override val nextItem: Any
                    get() =
                        if (position + 1 == getCount()) {
                            ""
                        } else {
                            accidental[position+1]
                        }


                override fun createView(): View {
                    val textView = TextView(context)
                    textView.gravity = (Gravity.TOP or Gravity.CENTER_HORIZONTAL)
                    textView.textSize = 36F
                    textView.gravity = Gravity.CENTER

                    return textView
                }
            }

            val musResAcc = getMusicalString(Locale.JAPAN)
            columnShifter.data = musResAcc(R.string.notename_c)
        } catch (e: Exception) {
           e.printStackTrace()
        }

    }
}
