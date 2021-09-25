package ir.cafebazaar.servicebase.communicators

import ir.cafebazaar.servicebase.state.ClientState
import ir.cafebazaar.servicebase.state.ClientStateListener

interface ClientConnectionCommunicator {
    var clientState: ClientState
    var clientStateListener: ClientStateListener
    fun startConnection(): Boolean
    fun stopConnection()
}