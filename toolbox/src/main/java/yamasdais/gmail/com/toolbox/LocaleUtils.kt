package yamasdais.gmail.com.toolbox

import java.util.*
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources

fun getResourceOtherLocale(context: Context, targetLocale: Locale): Resources {
    val dLocale = Locale.getDefault()
    return if (dLocale.equals(targetLocale)) {
        context.resources
    } else {
        context.createConfigurationContext(Configuration(context.resources.configuration).apply {
            setLocale(targetLocale)
        }).resources
    }
}

fun getStringOtherLocale(resource: Resources)
        : (Int) -> String = {
    id: Int -> resource.getString(id)
}
