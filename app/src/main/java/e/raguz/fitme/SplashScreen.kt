package e.raguz.fitme

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import e.raguz.fitme.framework.*
import kotlinx.android.synthetic.main.activity_splash_screen.*

private const val DELAY : Long = 3000
private const val DATA_IMPORTED = "e.raguz.fitMe.data_imported"

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        startAnimation()
        redirect()
    }

    private fun startAnimation() {
        ivSplashScreen.applyAnimation(R.anim.blink);
    }

    private fun redirect() {

        if (isOnline()) {
            Handler(Looper.getMainLooper()).postDelayed(
                { startActivity<MainActivity>() },
                DELAY
            )
        } else {
           Toast.makeText(this,
               getString(R.string.connect_message),
               Toast.LENGTH_SHORT).show()
            finish()
        }

    }
}