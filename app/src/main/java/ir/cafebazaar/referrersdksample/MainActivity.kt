package ir.cafebazaar.referrersdksample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ir.cafebazaar.referrersdk.ReferrerClient
import ir.cafebazaar.referrersdk.ReferrerStateListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        ReferrerClient.newBuilder(applicationContext).build().apply {
            startConnection(object : ReferrerStateListener {
                override fun onReferrerSetupFinished(referrerResponse: Int) {
                    when (referrerResponse) {
                        ReferrerClient.OK -> {
                            referrer?.let {
                                Toast.makeText(
                                    applicationContext,
                                    "REFERRER:$it",
                                    Toast.LENGTH_SHORT
                                ).show()
                                consumeReferrer(it.installBeginTimestampMilliseconds)
                            }
                        }
                        ReferrerClient.DEVELOPER_ERROR -> {
                            Toast.makeText(
                                applicationContext,
                                "REFERRER:DEVELOPER_ERROR",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        ReferrerClient.FEATURE_NOT_SUPPORTED -> {
                            Toast.makeText(
                                applicationContext,
                                "REFERRER:FEATURE_NOT_SUPPORTED",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        ReferrerClient.SERVICE_UNAVAILABLE -> {
                            Toast.makeText(
                                applicationContext,
                                "REFERRER:SERVICE_UNAVAILABLE",
                                Toast.LENGTH_SHORT
                            ).show()
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