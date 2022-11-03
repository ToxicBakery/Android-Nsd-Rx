package com.toxicbakery.library.nsd.rx

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryConfiguration
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryEvent
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryListenerRx
import com.toxicbakery.library.nsd.rx.registration.RegistrationConfiguration
import com.toxicbakery.library.nsd.rx.registration.RegistrationEvent
import com.toxicbakery.library.nsd.rx.registration.RegistrationListenerRx
import com.toxicbakery.library.nsd.rx.resolve.ResolveEvent
import com.toxicbakery.library.nsd.rx.resolve.ResolveListenerRx
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

class NsdManagerRx {

    private val nsdManagerCompat: INsdManagerCompat

    constructor(context: Context) {
        this.nsdManagerCompat = NsdManagerCompat.fromContext(context)
    }

    constructor(nsdManagerCompat: INsdManagerCompat) {
        this.nsdManagerCompat = nsdManagerCompat
    }

    fun discoverServices(discoveryConfiguration: DiscoveryConfiguration): Observable<DiscoveryEvent> =
            discoverServices(discoveryConfiguration) { nsdManagerCompat, emitter ->
                DiscoveryListenerRx(nsdManagerCompat, emitter)
            }

    fun discoverServices(
            discoveryConfiguration: DiscoveryConfiguration,
            listenerFactory: (INsdManagerCompat, ObservableEmitter<DiscoveryEvent>) -> NsdManager.DiscoveryListener
    ): Observable<DiscoveryEvent> = Observable.create { emitter: ObservableEmitter<DiscoveryEvent> ->
        val listener = listenerFactory(nsdManagerCompat, emitter)
        emitter.setCancellable { nsdManagerCompat.stopServiceDiscovery(listener) }
        nsdManagerCompat.discoverServices(
                serviceType = discoveryConfiguration.type,
                protocolType = discoveryConfiguration.protocolType,
                listener = listener
        )
    }

    fun registerService(discoveryConfiguration: RegistrationConfiguration): Observable<RegistrationEvent> =
            registerService(discoveryConfiguration) { nsdManagerCompat, emitter ->
                RegistrationListenerRx(nsdManagerCompat, emitter)
            }

    fun registerService(
            registrationConfiguration: RegistrationConfiguration,
            listenerFactory: (INsdManagerCompat, ObservableEmitter<RegistrationEvent>) -> NsdManager.RegistrationListener
    ): Observable<RegistrationEvent> = Observable.create { emitter: ObservableEmitter<RegistrationEvent> ->
        val listener = listenerFactory(nsdManagerCompat, emitter)
        emitter.setCancellable { nsdManagerCompat.unregisterService(listener) }
        val nsdServiceInfo = NsdServiceInfo().apply {
            serviceName = registrationConfiguration.serviceName
            serviceType = registrationConfiguration.serviceType
            port = registrationConfiguration.port
        }
        nsdManagerCompat.registerService(nsdServiceInfo, registrationConfiguration.protocolType, listener)
    }

    fun resolveService(serviceInfo: NsdServiceInfo): Observable<ResolveEvent> =
            resolveService(serviceInfo) { emitter -> ResolveListenerRx(emitter) }

    fun resolveService(
            serviceInfo: NsdServiceInfo,
            listenerFactory: (ObservableEmitter<ResolveEvent>) -> NsdManager.ResolveListener
    ): Observable<ResolveEvent> = Observable.create { emitter: ObservableEmitter<ResolveEvent> ->
                nsdManagerCompat.resolveService(serviceInfo, listenerFactory(emitter))
            }

}
