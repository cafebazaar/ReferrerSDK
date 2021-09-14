package ir.cafebazaar.referrersdk

import android.content.Context
import android.content.pm.PackageInfo

internal fun Context.getPackageInfo(packageName: String): PackageInfo? = try {
    val packageManager = packageManager
    packageManager.getPackageInfo(packageName, 0)
} catch (ignored: Exception) {
    null
}

@Suppress("DEPRECATION")
internal fun PackageInfo.sdkAwareVersionCode(): Long {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
        longVersionCode
    } else {
        versionCode.toLong()
    }
}