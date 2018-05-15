package bei.zi.mu.ext

import bei.zi.mu.App

fun Int.dip2px() : Int {
    val density = App.myApp.resources.displayMetrics.density
    return (this * density.toDouble() + 0.5).toInt()
}