package ir.cafebazaar.referrersdksample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import ir.cafebazaar.referrersdk.ReferrerClient
import ir.cafebazaar.referrersdk.ReferrerStateListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        ReferrerClient.newBuilder(applicationContext).build().apply {
            lifecycleScope.launch {
                startConnection(object : ReferrerStateListener {
                    override fun onReferrerSetupFinished(referrerResponse: Int) {
                        when (referrerResponse) {
                            ReferrerClient.OK -> {
                                referrer?.let {
                                    showMessage("REFERRER:${it.referrer}")
                                    consumeReferrer(it.installBeginTimestampMilliseconds)
                                } ?: kotlin.run {
                                    showMessage("REFERRER: THERE IS NO REFERRER")
                                }
                            }
                            ReferrerClient.DEVELOPER_ERROR -> {
                                showMessage("REFERRER:DEVELOPER_ERROR")
                            }
                            ReferrerClient.SERVICE_UNAVAILABLE -> {
                                showMessage("REFERRER:SERVICE_UNAVAILABLE")
                            }
                        }
                    }

                    override fun onReferrerServiceDisconnected() {
                        endConnection()
                    }
                })
            }
        }
    }

    private fun showMessage(message: String) {
        with(findViewById<TextView>(R.id.textview)) {
            post {
                text = message
            }
        }
    }
}