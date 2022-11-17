package com.toxicbakery.library.nsd.rx

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.toxicbakery.library.nsd.rx.discovery.*
import com.toxicbakery.library.nsd.rx.registration.RegistrationConfiguration
import com.toxicbakery.library.nsd.rx.registration.RegistrationEvent
import com.toxicbakery.library.nsd.rx.registration.RegistrationListenerFlow
import com.toxicbakery.library.nsd.rx.registration.ServiceUnregistered
import com.toxicbakery.library.nsd.rx.resolve.ResolveEvent
import com.toxicbakery.library.nsd.rx.resolve.ResolveListenerFlow
import kotlinx.coroutines.flow.*

class NsdManagerFlow {

    private val nsdManagerCompat: INsdManagerCompat

    constructor(context: Context) {
        this.nsdManagerCompat = NsdManagerCompat.fromContext(context)
    }

    constructor(nsdManagerCompat: INsdManagerCompat) {
        this.nsdManagerCompat = nsdManagerCompat
    }

    fun discoverServices(
        discoveryConfiguration: DiscoveryConfiguration
    ): Flow<DiscoveryEvent> =
        discoverServices(discoveryConfiguration) { nsdManagerCompat, sharedFlow ->
            DiscoveryListenerFlow(nsdManagerCompat, sharedFlow)
        }

    fun discoverServices(
        discoveryConfiguration: DiscoveryConfiguration,
        listenerFactory: (INsdManagerCompat, MutableSharedFlow<DiscoveryEvent>) -> NsdManager.DiscoveryListener
    ): Flow<DiscoveryEvent> = MutableSharedFlow<DiscoveryEvent>().let { sharedFlow ->
        val listener = listenerFactory(nsdManagerCompat, sharedFlow)
        fun deregister() = nsdManagerCompat.stopServiceDiscovery(listener)
        nsdManagerCompat.discoverServices(
            serviceType = discoveryConfiguration.type,
            protocolType = discoveryConfiguration.protocolType,
            listener = listener,
        )
        sharedFlow
            .onEach { event ->
                when (event) {
                    is DiscoveryStopped,
                    is DiscoveryServiceLost -> deregister()
                    else -> Unit
                }
            }
            .catch { deregister() }
    }

    fun registerService(discoveryConfiguration: RegistrationConfiguration): Flow<RegistrationEvent> =
        registerService(discoveryConfiguration) { nsdManagerCompat, emitter ->
            RegistrationListenerFlow(nsdManagerCompat, emitter)
        }

    fun registerService(
        registrationConfiguration: RegistrationConfiguration,
        listenerFactory: (INsdManagerCompat, MutableSharedFlow<RegistrationEvent>) -> NsdManager.RegistrationListener
    ): Flow<RegistrationEvent> = MutableSharedFlow<RegistrationEvent>().let { sharedFlow ->
        val listener = listenerFactory(nsdManagerCompat, sharedFlow)
        fun deregister() = nsdManagerCompat.unregisterService(listener)
        val nsdServiceInfo = NsdServiceInfo().apply {
            serviceName = registrationConfiguration.serviceName
            serviceType = registrationConfiguration.serviceType
            port = registrationConfiguration.port
        }
        nsdManagerCompat.registerService(
            nsdServiceInfo,
            registrationConfiguration.protocolType,
            listener
        )
        sharedFlow
            .onEach { event ->
                if (event is ServiceUnregistered) deregister()
            }
            .catch { deregister() }
    }

    fun resolveService(serviceInfo: NsdServiceInfo): Flow<ResolveEvent> =
        resolveService(serviceInfo) { sharedFlow -> ResolveListenerFlow(sharedFlow) }

    fun resolveService(
        serviceInfo: NsdServiceInfo,
        listenerFactory: (MutableSharedFlow<ResolveEvent>) -> NsdManager.ResolveListener
    ): Flow<ResolveEvent> = MutableSharedFlow<ResolveEvent>().let { sharedFlow ->
        nsdManagerCompat.resolveService(serviceInfo, listenerFactory(sharedFlow))
        sharedFlow.asSharedFlow()
    }
}
