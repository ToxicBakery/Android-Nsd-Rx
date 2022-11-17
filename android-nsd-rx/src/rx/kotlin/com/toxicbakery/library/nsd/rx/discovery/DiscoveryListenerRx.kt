package com.toxicbakery.library.nsd.rx.discovery

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.toxicbakery.library.nsd.rx.DiscoveryStartFailedException
import com.toxicbakery.library.nsd.rx.DiscoveryStopFailedException
import com.toxicbakery.library.nsd.rx.INsdManagerCompat
import io.reactivex.rxjava3.core.ObservableEmitter

internal data class DiscoveryListenerRx(
        private val nsdManagerCompat: INsdManagerCompat,
        private val emitter: ObservableEmitter<DiscoveryEvent>
) : NsdManager.DiscoveryListener {

    private val binder: DiscoveryBinder by lazy { DiscoveryBinder(nsdManagerCompat, this) }

    override fun onServiceFound(serviceInfo: NsdServiceInfo) =
            emitter.onNext(DiscoveryServiceFound(binder, serviceInfo))

    override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) =
            emitter.onError(DiscoveryStopFailedException(serviceType, errorCode))

    override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) =
            emitter.onError(DiscoveryStartFailedException(serviceType, errorCode))

    override fun onDiscoveryStarted(serviceType: String) =
            emitter.onNext(DiscoveryStarted(serviceType))

    override fun onDiscoveryStopped(serviceType: String) {
        emitter.onNext(DiscoveryStopped(serviceType))
        emitter.onComplete()
    }

    override fun onServiceLost(service: NsdServiceInfo) =
            emitter.onNext(DiscoveryServiceLost(binder, service))

}
