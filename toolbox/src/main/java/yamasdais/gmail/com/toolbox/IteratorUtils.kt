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

fun <T> Pair<T, T>.asIterable() = object: Iterable<T> {
    override fun iterator(): Iterator<T> {
        return object: Iterator<T> {
            var wasEnd: Boolean? = null
            override fun hasNext(): Boolean =
                    wasEnd == true

            override fun next(): T =
                when (wasEnd) {
                    null -> { wasEnd = false; first }
                    false -> { wasEnd = true; second }
                    true -> throw NoSuchElementException()
                }
        }
    }
}

fun <T> Iterator<T>.toPair() =
        Pair(next(), next())

inline fun <T, S> Iterator<T>.toPair(func: (T) -> S) =
        Pair(func(next()), func(next()))
