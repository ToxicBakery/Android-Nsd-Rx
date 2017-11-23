package com.toxicbakery.library.nsd.rx

import android.net.nsd.NsdServiceInfo

data class DiscoveryStopFailedException(val serviceType: String, val errorCode: Int)
    : Exception("Discovery stop failed for $serviceType with error $errorCode")

data class DiscoveryStartFailedException(val serviceType: String, val errorCode: Int)
    : Exception("Discovery start failed for $serviceType with error $errorCode")

data class RegistrationFailedException(val nsdServiceInfo: NsdServiceInfo, val errorCode: Int)
    : Exception("Registration failed for $nsdServiceInfo with error $errorCode")

data class UnregistrationFailedException(val nsdServiceInfo: NsdServiceInfo, val errorCode: Int)
    : Exception("Unregistration failed for $nsdServiceInfo with error $errorCode")

data class ResolveFailedException(val nsdServiceInfo: NsdServiceInfo, val errorCode: Int)
    : Exception("Resolve failed for $nsdServiceInfo with error $errorCode")