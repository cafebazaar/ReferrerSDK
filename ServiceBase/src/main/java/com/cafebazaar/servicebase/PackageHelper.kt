package com.cafebazaar.servicebase

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build

internal fun getBazaarPackageInfo(context: Context): PackageInfo? {
    return try {
        val packageManager = context.packageManager
        packageManager.getPackageInfo(Client.SERVICE_PACKAGE_NAME, 0)
    } catch (ignored: Exception) {
        null
    }
}

internal fun getBazaarVersionCode(context: Context) = getBazaarPackageInfo(context)?.let {
    sdkAwareVersionCode(it)
} ?: 0L

internal fun sdkAwareVersionCode(packageInfo: PackageInfo): Long {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageInfo.longVersionCode
    } else {
        packageInfo.versionCode.toLong()
    }
}