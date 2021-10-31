package com.cafebazaar.servicebase.communicator

interface ClientConnectionCommunicator {

    fun startConnection(): Boolean

    fun stopConnection()
}