package com.cafebazaar.servicebase.state

interface ClientStateListener {
    fun onSetupFinished(response: Int)
    fun onServiceDisconnected()
}