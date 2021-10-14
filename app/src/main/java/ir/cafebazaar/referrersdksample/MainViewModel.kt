package ir.cafebazaar.referrersdksample

import android.app.Application
import androidx.lifecycle.*
import com.cafebazaar.servicebase.state.ClientError
import com.cafebazaar.servicebase.state.ClientStateListener
import com.cafebazaar.referrersdk.ReferrerClient
import com.cafebazaar.referrersdk.model.ReferrerDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val referrerClient: ReferrerClient = ReferrerClient.getClient(application)
    private val stateListener = object : ClientStateListener {

        override fun onReady() {
            getAndConsumeReferrer()
        }

        override fun onError(clientError: ClientError) {
            handleReferrerError(clientError)
        }
    }
    private val _referrerContent = MutableLiveData<ReferrerDetails>()
    val referrerContent: LiveData<ReferrerDetails> = _referrerContent

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun onResume() {
        viewModelScope.launch(Dispatchers.IO) {
            referrerClient.startConnection(stateListener)
        }
    }

    private fun handleReferrerError(referrerError: ClientError) {
        when (referrerError) {
            is ClientError.ServiceUnAvailable -> {
                _errorMessage.postValue(referrerError.message)
            }
            is ClientError.DeveloperError -> {
                _errorMessage.postValue(referrerError.message)
            }
            is ClientError.RunTime ->  {
                _errorMessage.postValue(referrerError.message)
            }
        }
    }

    private fun getAndConsumeReferrer() {
        referrerClient.getReferrerDetails()?.let { referrerDetails ->
            _referrerContent.postValue(referrerDetails)
            referrerClient.consumeReferrer(referrerDetails.installBeginTimestampMilliseconds)
            referrerClient.endConnection()
        } ?: run {
            _errorMessage.postValue("THERE IS NO REFERRER")
        }
    }
}