Android ReferrerSDK for [Cafe Bazaar](https://cafebazaar.ir/?l=en) App Store.
## Getting Started
To start working with ReferrerSDK, you need to add its dependency into your `build.gradle` file:
### Dependency
```groovy
dependencies {
    implementation "com.github.cafebazaar.referrersdk:referrersdk:[latest_version]"
}
```

Then you need to add jitpack as your maven repository in `build.gradle`  file:

```groovy
repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
```

### How to use
For more information regarding the usage of ReferrerSDK, please check out the [wiki](https://github.com/cafebazaar/referrersdk/wiki) page.
### Sample
There is a fully functional sample application that demonstrates the usage of ReferrerSDK, all you have to do is cloning the project and running the [app](https://github.com/cafebazaar/ReferrerSDK/tree/master/app) module.
