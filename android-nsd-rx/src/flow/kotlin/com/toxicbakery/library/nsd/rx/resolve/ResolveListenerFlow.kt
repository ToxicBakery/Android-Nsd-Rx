package com.toxicbakery.library.nsd.rx.resolve

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.toxicbakery.library.nsd.rx.ResolveFailedException
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class ResolveListenerFlow(
    private val sharedFlow: MutableSharedFlow<ResolveEvent>
) : NsdManager.ResolveListener {
    override fun onResolveFailed(nsdServiceInfo: NsdServiceInfo, errorCode: Int) {
        MainScope().launch {
            sharedFlow.emitAll(flow { throw ResolveFailedException(nsdServiceInfo, errorCode) })
        }
    }

    override fun onServiceResolved(nsdServiceInfo: NsdServiceInfo) {
        MainScope().launch {
            sharedFlow.emit(ServiceResolved(nsdServiceInfo))
        }
    }
}
