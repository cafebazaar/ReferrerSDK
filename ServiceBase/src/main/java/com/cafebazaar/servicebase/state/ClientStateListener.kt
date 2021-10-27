package com.cafebazaar.servicebase.state

interface ClientStateListener {

    fun onReady()

    fun onError(clientError: ClientError)
}