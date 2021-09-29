package ir.cafebazaar.referrersdksample

import android.app.Application
import androidx.lifecycle.*
import ir.cafebazaar.referrersdk.ReferrerClient
import ir.cafebazaar.referrersdk.ReferrerDetails
import ir.cafebazaar.referrersdk.ReferrerStateListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val referrerClient: ReferrerClient = ReferrerClient.newBuilder(application).build()
    private val stateListener = object : ReferrerStateListener {
        override fun onReferrerSetupFinished(referrerResponseState: Int) {
            _referrerResponseState.postValue(referrerResponseState)
        }

        override fun onReferrerServiceDisconnected() {
            referrerClient.endConnection()
        }
    }
    private val _referrerResponseState = MutableLiveData<Int>()
    val referrerResponseState: LiveData<Int> = _referrerResponseState
    private val _referrerContent = MutableLiveData<ReferrerDetails>()
    val referrerContent: LiveData<ReferrerDetails> = _referrerContent
    private val _errorDuringGettingReferrerAndConsumeIt = MutableLiveData<String>()
    val errorDuringGettingReferrerAndConsumeIt: LiveData<String> = _errorDuringGettingReferrerAndConsumeIt
    fun onResume() {
        viewModelScope.launch(Dispatchers.IO) {
            referrerClient.startConnection(stateListener)
        }
    }

    fun getAndConsumeReferrer() {
        referrerClient.referrer?.let { referrerDetails ->
            _referrerContent.postValue(referrerDetails)
            referrerClient.consumeReferrer(referrerDetails.installBeginTimestampMilliseconds)
        } ?: kotlin.run {
            _errorDuringGettingReferrerAndConsumeIt.postValue("THERE IS NO REFERRER")
        }
    }
}