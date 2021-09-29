package ir.cafebazaar.referrersdksample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.cafebazaar.referrersdk.ReferrerClient
import ir.cafebazaar.referrersdk.ReferrerStateListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _referrerResponseState = MutableLiveData<Int>()
    val referrerResponseState: LiveData<Int> = _referrerResponseState
    fun onCreate(referrerClient: ReferrerClient) {
        with(referrerClient) {
            viewModelScope.launch(Dispatchers.IO) {
                startConnection(object : ReferrerStateListener {
                    override fun onReferrerSetupFinished(referrerResponseState: Int) {
                        _referrerResponseState.postValue(referrerResponseState)
                    }

                    override fun onReferrerServiceDisconnected() {
                        endConnection()
                    }
                })
            }
        }
    }
}