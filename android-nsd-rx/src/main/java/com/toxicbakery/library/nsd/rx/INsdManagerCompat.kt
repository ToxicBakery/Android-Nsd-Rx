package com.toxicbakery.library.nsd.rx

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo

interface INsdManagerCompat {

    fun registerService(serviceInfo: NsdServiceInfo, protocolType: Int, listener: NsdManager.RegistrationListener)

    fun unregisterService(listener: NsdManager.RegistrationListener)

    fun discoverServices(serviceType: String, protocolType: Int, listener: NsdManager.DiscoveryListener)

    fun stopServiceDiscovery(listener: NsdManager.DiscoveryListener)

    fun resolveService(serviceInfo: NsdServiceInfo, listener: NsdManager.ResolveListener)

}