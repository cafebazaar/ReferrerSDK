package com.farsitel.bazaar.referrerprovider;

interface ReferrerProviderService {
    /**
    * Returns the referrer data for given package name
    * @param packageName The package name of the user application
    * @return Bundle Contains the referrer data as below:
    * A string value with key equal install_referrer
    * A long value with key equal referrer_click_timestamp_milliseconds
    * A long value with key equal install_begin_timestamp_milliseconds
    * A string value with key equal app_version
    */
    Bundle getReferrer(String packageName);
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
    * @param packageName The package name of the user application.
    * @param installTimeMilliSeconds The install time (in milliseconds) as retrieved from {@code getReferrer()}.
    *
    * @deprecated Since Bazaar 26.4.0. This method is now a no-op and should no longer be used.
    *             Referrer consumption is now handled internally.
    */
    void consumeReferrer(String packageName, long installTimeMilliSeconds);
}