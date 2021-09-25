package ir.cafebazaar.servicebase.state

interface ClientStateListener {
    fun onSetupFinished(response: Int)
    fun onServiceDisconnected()
}