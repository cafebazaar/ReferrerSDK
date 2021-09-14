package ir.cafebazaar.referrersdk

abstract class ClientState {
    abstract fun updateState(state: Int)
    companion object {
        internal const val DISCONNECTED = 0
        internal const val CONNECTING = 1
        internal const val CONNECTED = 2
        internal const val CLOSED = 3
    }
}