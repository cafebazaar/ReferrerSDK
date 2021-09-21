package ir.cafebazaar.referrersdksample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import ir.cafebazaar.referrersdk.ReferrerClient
import ir.cafebazaar.referrersdk.ReferrerDetails
import ir.cafebazaar.referrersdk.ReferrerStateListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        showError("")
        ReferrerClient.newBuilder(applicationContext).build().apply {
            lifecycleScope.launch(Dispatchers.IO) {
                startConnection(object : ReferrerStateListener {
                    override fun onReferrerSetupFinished(referrerResponse: Int) {
                        when (referrerResponse) {
                            ReferrerClient.OK -> {
                                referrer?.let {
                                    showMessages(it)
                                    consumeReferrer(it.installBeginTimestampMilliseconds)
                                } ?: kotlin.run {
                                    showError("THERE IS NO REFERRER")
                                }
                            }
                            ReferrerClient.DEVELOPER_ERROR -> {
                                showError("DEVELOPER_ERROR")
                            }
                            ReferrerClient.SERVICE_UNAVAILABLE -> {
                                showError("SERVICE_UNAVAILABLE")
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

    private fun showMessages(referrerDetails: ReferrerDetails) {
        with(findViewById<TextView>(R.id.clicktime)) {
            post {
                text = referrerDetails.referrerClickTimestampMilliseconds.millisecondsToTime()
            }
        }
        with(findViewById<TextView>(R.id.installtime)) {
            post {
                text = referrerDetails.installBeginTimestampMilliseconds.millisecondsToTime()
            }
        }
        with(findViewById<TextView>(R.id.version)) {
            post {
                text = referrerDetails.appVersion.toString()
            }
        }
        with(findViewById<TextView>(R.id.content)) {
            post {
                text = referrerDetails.referrer.toString()
            }
        }
    }

    private fun showError(message: String) {
        with(findViewById<TextView>(R.id.error)) {
            post {
                text = message
            }
        }
    }

    private fun Long.millisecondsToTime(): String {
        return Date(this).toString()
    }
}