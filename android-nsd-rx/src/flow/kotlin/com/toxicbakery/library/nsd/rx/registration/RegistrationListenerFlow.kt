package com.toxicbakery.library.nsd.rx.registration

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.toxicbakery.library.nsd.rx.INsdManagerCompat
import com.toxicbakery.library.nsd.rx.RegistrationFailedException
import com.toxicbakery.library.nsd.rx.UnregistrationFailedException
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

internal class RegistrationListenerFlow(
    private val nsdManagerCompat: INsdManagerCompat,
    private val sharedFlow: MutableSharedFlow<RegistrationEvent>,
) : NsdManager.RegistrationListener {

    private val binder: RegistrationBinder by lazy { RegistrationBinder(nsdManagerCompat, this) }

    override fun onUnregistrationFailed(nsdServiceInfo: NsdServiceInfo, errorCode: Int) {
        MainScope().launch {
            sharedFlow.emitAll(flow {
                throw UnregistrationFailedException(nsdServiceInfo, errorCode)
            })
        }
    }

    override fun onServiceUnregistered(nsdServiceInfo: NsdServiceInfo) {
        MainScope().launch {
            ServiceUnregistered(binder, nsdServiceInfo)
        }
    }

    override fun onRegistrationFailed(nsdServiceInfo: NsdServiceInfo, errorCode: Int) {
        MainScope().launch {
            sharedFlow.emitAll(flow {
                throw RegistrationFailedException(
                    nsdServiceInfo,
                    errorCode
                )
            })
        }
    }

    override fun onServiceRegistered(nsdServiceInfo: NsdServiceInfo) {
        MainScope().launch {
            sharedFlow.emit(ServiceRegistered(binder, nsdServiceInfo))
        }
    }
}
