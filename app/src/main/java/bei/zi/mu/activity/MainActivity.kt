package bei.zi.mu.activity

import android.os.Bundle
import bei.zi.mu.R
import bei.zi.mu.TitlebarActivity

class MainActivity : TitlebarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

}
