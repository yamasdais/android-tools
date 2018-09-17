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
