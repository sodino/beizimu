package bei.zi.mu.ext

import bei.zi.mu.App



fun Int.showToast() {
    ((App.myApp.getText(this))as String).showToast()
}


fun Int.dip2px() : Int {
    val density = App.myApp.resources.displayMetrics.density
    return (this * density.toDouble() + 0.5).toInt()
}


fun Int.hexString() : String {
    return java.lang.Integer.toHexString(this)
}