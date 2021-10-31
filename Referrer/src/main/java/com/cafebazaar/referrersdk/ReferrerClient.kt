package com.cafebazaar.referrersdk

import android.app.Application
import com.cafebazaar.servicebase.state.ClientStateListener
import com.cafebazaar.referrersdk.model.ReferrerDetails

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
     * Consumes the referrer content in order to avoid getting repetitive referrer content
     */
    fun consumeReferrer(installTime: Long)

    companion object {
        fun getClient(application: Application): ReferrerClient {
            return ReferrerClientImpl(application)
        }
    }
}