package ir.cafebazaar.servicebase.state

abstract class ClientState {
    abstract fun updateClientState(state: Int)
    companion object {
        const val DISCONNECTED = 0
        internal const val CONNECTING = 1
        const val CONNECTED = 2
        internal const val CLOSED = 3
    }
}