package com.toxicbakery.library.nsd.rx

import android.net.nsd.NsdServiceInfo
import com.toxicbakery.library.nsd.rx.discovery.*
import io.reactivex.ObservableEmitter
import org.junit.Before
import org.junit.Test

class DiscoveryListenerRxTest {

    private lateinit var nsdManagerCompat: INsdManagerCompat
    private lateinit var emitter: ObservableEmitter<DiscoveryEvent>
    private lateinit var binder: DiscoveryBinder
    private lateinit var listener: DiscoveryListenerRx

    @Before
    fun setup() {
        nsdManagerCompat = mock()
        emitter = mock()
        listener = DiscoveryListenerRx(nsdManagerCompat, emitter)
        binder = DiscoveryBinder(nsdManagerCompat, listener)
    }

    @Test
    fun onServiceFound() {
        NsdServiceInfo().let {
            listener.onServiceFound(it)
            verify(emitter).onNext(DiscoveryServiceFound(binder, it))
        }
    }

    @Test
    fun onStopDiscoveryFailed() {
        listener.onStopDiscoveryFailed(SERVICE_TYPE, ERROR_CODE)
        verify(emitter).onError(DiscoveryStopFailedException(SERVICE_TYPE, ERROR_CODE))
    }

    @Test
    fun onStartDiscoveryFailed() {
        listener.onStartDiscoveryFailed(SERVICE_TYPE, ERROR_CODE)
        verify(emitter).onError(DiscoveryStartFailedException(SERVICE_TYPE, ERROR_CODE))
    }

    @Test
    fun onDiscoveryStarted() {
        listener.onDiscoveryStarted(SERVICE_TYPE)
        verify(emitter).onNext(DiscoveryStarted(SERVICE_TYPE))
    }

    @Test
    fun onDiscoveryStopped() {
        listener.onDiscoveryStopped(SERVICE_TYPE)
        verify(emitter).onNext(DiscoveryStopped(SERVICE_TYPE))
        verify(emitter).onComplete()
    }

    @Test
    fun onServiceLost() {
        NsdServiceInfo().let {
            listener.onServiceLost(it)
            verify(emitter).onNext(DiscoveryServiceLost(binder, it))
        }
    }

    companion object {
        private const val SERVICE_TYPE = "serviceType"
        private const val ERROR_CODE = 1
    }

}