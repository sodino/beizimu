package bei.zi.mu.ext

import android.content.Context
import android.widget.EditText
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import bei.zi.mu.App


fun EditText.hideSoftImputFromWindow() {
    val inputMethodManager = App.myApp.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}