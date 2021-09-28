package com.farsitel.bazaar.referrerprovider;

// Declare any non-default types here with import statements

interface ReferrerProviderService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    Bundle getReferrer(String packageName);
    void consumeReferrer(String packageName, long installTimeMilliSeconds);
}