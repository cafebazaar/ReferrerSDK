package ir.cafebazaar.referrersdksample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import ir.cafebazaar.referrersdk.ReferrerClient
import ir.cafebazaar.referrersdk.ReferrerDetails
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var errorTextView: TextView
    private lateinit var referrerContentTextView: TextView
    private lateinit var versionTextView: TextView
    private lateinit var installTimeTextView: TextView
    private lateinit var clickTimeTextView: TextView
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        with(mainViewModel) {
            referrerResponseState.observe(this@MainActivity) { referrerResponseState ->
                handleReferrerResponse(referrerResponseState)
            }
            referrerContent.observe(this@MainActivity) { referrerDetails ->
                showMessages(referrerDetails)
            }
            errorDuringGettingReferrerAndConsumeIt.observe(this@MainActivity) { error ->
                showError(error)
            }
        }
    }

    private fun initUI() {
        clickTimeTextView = findViewById(R.id.clicktime)
        installTimeTextView = findViewById(R.id.installtime)
        versionTextView = findViewById(R.id.version)
        referrerContentTextView = findViewById(R.id.content)
        errorTextView = findViewById(R.id.error)
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.onResume()
        hideError()
    }

    private fun hideError() {
        showError("")
    }

    private fun handleReferrerResponse(referrerResponse: Int) {
        when (referrerResponse) {
            ReferrerClient.OK -> {
                mainViewModel.getAndConsumeReferrer()
            }
            ReferrerClient.DEVELOPER_ERROR -> {
                showError("DEVELOPER_ERROR")
            }
            ReferrerClient.SERVICE_UNAVAILABLE -> {
                showError("SERVICE_UNAVAILABLE")
            }
        }
    }

    private fun showMessages(referrerDetails: ReferrerDetails) {
        clickTimeTextView.text = referrerDetails.referrerClickTimestampMilliseconds.millisecondsToTime()
        installTimeTextView.text = referrerDetails.installBeginTimestampMilliseconds.millisecondsToTime()
        versionTextView.text = referrerDetails.appVersion.toString()
        referrerContentTextView.text = referrerDetails.referrer.toString()
    }

    private fun showError(message: String) {
        errorTextView.text = message
    }

    private fun Long.millisecondsToTime(): String {
        return Date(this).toString()
    }
}