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

package yamasdais.gmail.com.yamasdaislibs

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import yamasdais.gmail.com.slipcolumns.SlipColumnsDataBinder
import yamasdais.gmail.com.toolbox.getResourceOtherLocale
import yamasdais.gmail.com.toolbox.getStringOtherLocale
import java.beans.PropertyChangeListener
import java.util.*

class MainActivity : AppCompatActivity() {

    private fun getMusicalString(musicalLocale: Locale): (Int) -> String {
        val res = getResourceOtherLocale(this, musicalLocale)
        return getStringOtherLocale(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = this
        try {
            slipColumnsList.addPropertyChangeListener(listener = PropertyChangeListener { evt ->
                evt?.let {
                    if (evt.propertyName == "item") {
                        //disp_text.text = evt.newValue as? String ?: "<null>"
                        disp_text.text = slipColumnsList.item as? String ?: "<null>"
                    }
                }
            })
            slipColumnsList.locale = Locale("ja")
            slipColumnsList.adapter = object : SlipColumnsDataBinder() {
                val format = arrayOf("%s\u266D\u266D", "%s\u266D", "%s", "%s\u266F", "%s\u266F\u266F")
                val accidental = arrayOf("\u266D\u266D", "\u266D", "♮", "\u266F", "\u266F\u266F")

                override fun getColumnCount(): Int = format.size

                override fun get(i: Int): Any = if (i < 0) { "" } else { String.format(format[i], slipColumnsList.data) }

                override val prevColumnItem: Any
                    get() =
                        if (columnPosition == 0) {
                            ""
                        } else {
                            accidental[columnPosition-1]
                        }
                override val nextColumnItem: Any
                    get() =
                        if (columnPosition + 1 == getColumnCount()) {
                            ""
                        } else {
                            accidental[columnPosition+1]
                        }


                override fun createView(): View {
                    val textView = TextView(context)
                    textView.gravity = (Gravity.TOP or Gravity.CENTER_HORIZONTAL)
                    textView.gravity = Gravity.CENTER

                    return textView
                }
            }

            val musResAcc = getMusicalString(slipColumnsList.locale)
            slipColumnsList.data = musResAcc(R.string.notename_c)

            langCountryIconView.locale = Locale("ja", "JP")
        } catch (e: Exception) {
           e.printStackTrace()
        }

    }
}
