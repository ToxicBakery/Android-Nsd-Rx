package com.toxicbakery.library.nsd.rx

import android.net.nsd.NsdServiceInfo
import android.support.test.filters.SmallTest
import android.support.test.runner.AndroidJUnit4
import com.toxicbakery.library.nsd.rx.registration.*
import io.reactivex.ObservableEmitter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

/**
 * Test has to run under Instrumentation for NsdServiceInfo.toString() functionality
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
class RegistrationListenerRxTest {

    private lateinit var nsdManagerCompat: INsdManagerCompat
    private lateinit var emitter: ObservableEmitter<RegistrationEvent>
    private lateinit var listener: RegistrationListenerRx

    private val binder: RegistrationBinder
        get() = RegistrationBinder(nsdManagerCompat, listener)

    @Before
    fun setUp() {
        nsdManagerCompat = mock()
        emitter = mock()
        listener = RegistrationListenerRx(nsdManagerCompat, emitter)
    }

    @Test
    fun onUnregistrationFailed() {
        NsdServiceInfo().let {
            listener.onUnregistrationFailed(it, ERROR_CODE)
            verify(emitter).onError(UnregistrationFailedException(it, ERROR_CODE))
        }
    }

    @Test
    fun onServiceUnregistered() {
        NsdServiceInfo().let {
            listener.onServiceUnregistered(it)
            verify(emitter).onNext(ServiceUnregistered(binder, it))
            verify(emitter).onComplete()
        }
    }

    @Test
    fun onRegistrationFailed() {
        NsdServiceInfo().let {
            listener.onRegistrationFailed(it, ERROR_CODE)
            verify(emitter).onError(RegistrationFailedException(it, ERROR_CODE))
        }
    }

    @Test
    fun onServiceRegistered() {
        NsdServiceInfo().let {
            listener.onServiceRegistered(it)
            verify(emitter).onNext(ServiceRegistered(binder, it))
        }
    }

    companion object {
        private const val ERROR_CODE = 1
    }

}
