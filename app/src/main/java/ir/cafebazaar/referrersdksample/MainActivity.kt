package ir.cafebazaar.referrersdksample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import ir.cafebazaar.referrersdk.ReferrerClient
import ir.cafebazaar.referrersdk.model.ReferrerDetails
import ir.cafebazaar.servicebase.Client
import ir.cafebazaar.servicebase.state.ClientStateListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        showError("")
        ReferrerClient.getClient(applicationContext).apply {
            lifecycleScope.launch(Dispatchers.IO) {
                startConnection(object : ClientStateListener {
                    override fun onSetupFinished(response: Int) {
                        when (response) {
                            Client.OK -> {
                                getReferrer()?.let {
                                    showMessages(it)
                                    consumeReferrer(it.installBeginTimestampMilliseconds)
                                    endConnection()
                                } ?: kotlin.run {
                                    showError("THERE IS NO REFERRER")
                                }
                            }
                            Client.DEVELOPER_ERROR -> {
                                showError("DEVELOPER_ERROR")
                            }
                            Client.SERVICE_UNAVAILABLE -> {
                                showError("SERVICE_UNAVAILABLE")
                            }
                        }
                    }

                    override fun onServiceDisconnected() {
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