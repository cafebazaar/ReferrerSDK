Android ReferrerSDK for [Cafe Bazaar](https://cafebazaar.ir/?l=en) App Store.
## Getting Started
To start working with ReferrerSDK, you need to add its dependency into your `build.gradle` file:
### Dependency
```groovy
dependencies {
    implementation "com.github.cafebazaar:referrersdk:[latest_version]"
}
```

Then you need to add jitpack as your maven repository in `build.gradle`  file:

```groovy
repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
```

### Referrer details
- `referrer`: the referrer string.
- `referrerClickTimestampMilliseconds` / `installBeginTimestampMilliseconds`: device times. The install begin time is recorded when the download of the first install actually starts and isn't changed by updates.
- `referrerClickTimestampServerMilliseconds` / `installBeginTimestampServerMilliseconds`: server times with whole-second precision, independent of the device clock. Prefer them for ordering and anti-fraud checks.
- Every timestamp is epoch milliseconds (UTC), and `-1` (`ReferrerDetails.TIMESTAMP_NOT_AVAILABLE`) means it isn't available, e.g. on older Bazaar versions.

### How to use
For more information regarding the usage of ReferrerSDK, please check out the [wiki](https://github.com/cafebazaar/referrersdk/wiki) page.
### Sample
There is a fully functional sample application that demonstrates the usage of ReferrerSDK, all you have to do is cloning the project and running the [app](https://github.com/cafebazaar/ReferrerSDK/tree/master/app) module.
