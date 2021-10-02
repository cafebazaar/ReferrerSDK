package ir.cafebazaar.servicebase

import android.os.Looper
import com.cafebazaar.servicebase.communicator.ClientConnectionCommunicator
import com.cafebazaar.servicebase.communicator.ClientReceiverCommunicator
import com.cafebazaar.servicebase.receiver.ClientReceiver
import com.cafebazaar.servicebase.state.ClientState
import com.cafebazaar.servicebase.state.ClientStateListener

abstract class Client: ClientState() {
    protected abstract val supportedClientVersion: Long
    protected abstract fun getConnectionsList(clientStateListener: ClientStateListener): List<ClientConnectionCommunicator>?
    @Volatile private var clientState = DISCONNECTED
    @Volatile protected var clientConnection: ClientConnectionCommunicator? = null
    private val isReady: Boolean
        get() = (clientState == CONNECTED).and(clientConnection != null)

    fun startConnection(clientStateListener: ClientStateListener) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw IllegalThreadStateException(OFF_MAIN_THREAD_EXCEPTION)
        }
        if (isReady.not()) {
            when {
                clientState == CONNECTING -> {
                    clientStateListener.onSetupFinished(DEVELOPER_ERROR)
                }
                clientState != CLOSED -> {
                    clientState = CONNECTING
                    getConnectionsList(clientStateListener)?.forEach { connection ->
                        if (connection is ClientReceiverCommunicator) {
                            ClientReceiver.addObserver(connection)
                        }
                        if (connection.startConnection()) {
                            clientConnection = connection
                            return
                        }
                    }
                    clientState = DISCONNECTED
                    clientStateListener.onSetupFinished(SERVICE_UNAVAILABLE)
                }
                else -> {
                    clientStateListener.onSetupFinished(DEVELOPER_ERROR)
                }
            }
        } else {
            clientStateListener.onSetupFinished(OK)
        }
    }

    fun endConnection() {
        clientState = CLOSED
        clientConnection?.let { connection ->
            if (connection is ClientReceiverCommunicator) {
                ClientReceiver.removeObserver(connection)
            }
            connection.stopConnection()
        }
    }

    protected fun runIfReady(block: () -> Any?): Any? {
        if (isReady.not()) {
            throw IllegalStateException(SERVICE_IS_NOT_STARTED_EXCEPTION)
        } else {
            return block.invoke()
        }
    }

    override fun updateClientState(state: Int) {
        clientState = state
    }

    companion object {
        private const val OFF_MAIN_THREAD_EXCEPTION = "This function has to call off the main thread."
        private const val SERVICE_IS_NOT_STARTED_EXCEPTION =
            "Service not connected. Please start a connection before using the service."
        const val OK = 0
        const val SERVICE_UNAVAILABLE = 1
        const val DEVELOPER_ERROR = 2
    }
}