package com.toxicbakery.library.nsd.rx

import android.net.nsd.NsdServiceInfo
import com.toxicbakery.library.nsd.rx.resolve.ResolveEvent
import com.toxicbakery.library.nsd.rx.resolve.ResolveListenerRx
import com.toxicbakery.library.nsd.rx.resolve.ServiceResolved
import io.reactivex.ObservableEmitter
import org.junit.Before
import org.junit.Test

class ResolveListenerRxTest {

    private lateinit var emitter: ObservableEmitter<ResolveEvent>
    private lateinit var listener: ResolveListenerRx

    @Before
    fun setup() {
        emitter = mock()
        listener = ResolveListenerRx(emitter)
    }

    @Test
    fun onResolveFailed() {
        NsdServiceInfo().let {
            listener.onResolveFailed(it, ERROR_CODE)
            verify(emitter).onError(ResolveFailedException(it, ERROR_CODE))
        }
    }

    @Test
    fun onServiceResolved() {
        NsdServiceInfo().let {
            listener.onServiceResolved(it)
            verify(emitter).onNext(ServiceResolved(it))
        }
    }

    companion object {
        private const val ERROR_CODE = 1
    }

}