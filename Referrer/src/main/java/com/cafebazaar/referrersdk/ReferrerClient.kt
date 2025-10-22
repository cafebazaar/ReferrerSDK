package com.cafebazaar.referrersdk

import android.app.Application
import com.cafebazaar.referrersdk.model.ReferrerDetails
import com.cafebazaar.servicebase.state.ClientStateListener

interface ReferrerClient {
    /**
     * Establishes the connection between SDK and Bazaar client.
     * @param clientStateListener A listener which user can use it to get the state of the connection.
     * @throws Exception You have to call this function off the main thread
     * otherwise it throws an exception.
     */
    fun startConnection(clientStateListener: ClientStateListener)

    /**
     * Closes the connection
     */
    fun endConnection()

    /**
     * Returns the referrer details which consists of
     * Referrer Content
     * Click Time in Milli Seconds
     * Install Time in Milli Seconds
     * Version of App
     */
    fun getReferrerDetails(): ReferrerDetails?

    /**
     * Consumes the referrer data for the given package name.
     * <p>
     * Users were previously required to provide the correct install time (retrieved from
     * {@code getReferrer()}) in order to consume the referrer data.
     * </p>
     *
     * <p><strong>Deprecated since Bazaar 26.4.0:</strong> This method is no longer supported.
     * Referrer consumption has been made internal and automatic. As of this version, calling this
     * method has no effect. Referrer data will now be consumed automatically within 90 days
     * of installation or upon app uninstall/reinstall.</p>
     *
     * @param installTime The install time (in milliseconds) as retrieved from {@code getReferrer()}.
     *
     * @deprecated Since Bazaar 26.4.0. This method is now a no-op and should no longer be used.
     *             Referrer consumption is now handled internally.
     */
    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "This functions is no-op from 26.4.0 of Bazaar and consume logic has become internal."
    )
    fun consumeReferrer(installTime: Long)

    companion object {
        fun getClient(application: Application): ReferrerClient {
            return ReferrerClientImpl(application)
        }
    }
}