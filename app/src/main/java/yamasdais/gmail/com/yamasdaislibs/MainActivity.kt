package yamasdais.gmail.com.yamasdaislibs

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import yamasdais.gmail.com.columnshifter.ColumnShifterAdapter
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = this
        try {
            columnShifter.data = "C"
            columnShifter.adapter = object : ColumnShifterAdapter() {
                val format = arrayOf("%s\u266D\u266D", "%s\u266D", "%s", "%s\u266F", "%s\u266F\u266F")
                val accidental = arrayOf("\u266D\u266D", "\u266D", "♮", "\u266F", "\u266F\u266F")

                override fun getCount(): Int = format.size

                override fun get(i: Int): Any = String.format(format[i], columnShifter.data)

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
                    val text_view = TextView(context)
                    text_view.gravity = (Gravity.TOP or Gravity.CENTER_HORIZONTAL)
                    text_view.textSize = 36F
                    text_view.gravity = Gravity.CENTER

                    return text_view
                }

            }
        } catch (e: Exception) {
           e.printStackTrace()
        }

    }
}
