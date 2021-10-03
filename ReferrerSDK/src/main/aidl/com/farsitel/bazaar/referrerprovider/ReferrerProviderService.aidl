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
    * Consumes the referrer data for given package name. Users have to provide
    * the right install time in order to consume the referrer data.
    * @param packageName The package name of the user application
    * @param installTimeMilliSeconds The install time which user has received from
    * getReferrer()
    */
    void consumeReferrer(String packageName, long installTimeMilliSeconds);
}