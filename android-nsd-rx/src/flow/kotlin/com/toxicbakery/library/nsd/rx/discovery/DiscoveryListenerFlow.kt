package com.toxicbakery.library.nsd.rx.discovery

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.toxicbakery.library.nsd.rx.DiscoveryStartFailedException
import com.toxicbakery.library.nsd.rx.DiscoveryStopFailedException
import com.toxicbakery.library.nsd.rx.INsdManagerCompat
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

internal class DiscoveryListenerFlow(
    private val nsdManagerCompat: INsdManagerCompat,
    private val sharedFlow: MutableSharedFlow<DiscoveryEvent>,
) : NsdManager.DiscoveryListener {

    private val binder: DiscoveryBinder by lazy { DiscoveryBinder(nsdManagerCompat, this) }

    override fun onServiceFound(serviceInfo: NsdServiceInfo) {
        MainScope().launch {
            sharedFlow.emit(DiscoveryServiceFound(binder, serviceInfo))
        }
    }

    override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
        MainScope().launch {
            sharedFlow.emitAll(flow { throw DiscoveryStopFailedException(serviceType, errorCode) })
        }
    }

    override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
        MainScope().launch {
            sharedFlow.emitAll(flow { throw DiscoveryStartFailedException(serviceType, errorCode) })
        }
    }

    override fun onDiscoveryStarted(serviceType: String) {
        MainScope().launch {
            sharedFlow.emit(DiscoveryStarted(serviceType))
        }
    }

    override fun onDiscoveryStopped(serviceType: String) {
        MainScope().launch {
            sharedFlow.emit(DiscoveryStopped(serviceType))
        }
    }

    override fun onServiceLost(serviceInfo: NsdServiceInfo) {
        MainScope().launch {
            sharedFlow.emit(DiscoveryServiceLost(binder, serviceInfo))
        }
    }
}