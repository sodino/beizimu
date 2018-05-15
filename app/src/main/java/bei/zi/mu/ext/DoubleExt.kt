package bei.zi.mu.ext

import bei.zi.mu.App

fun Double.dip2px() : Double {
    val density = App.myApp.resources.displayMetrics.density
    return (this * density.toDouble() + 0.5)
}