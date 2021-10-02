package com.cafebazaar.servicebase.communicator

import com.cafebazaar.servicebase.state.ClientState
import com.cafebazaar.servicebase.state.ClientStateListener


interface ClientConnectionCommunicator {
    var clientState: ClientState
    var clientStateListener: ClientStateListener
    fun startConnection(): Boolean
    fun stopConnection()
}