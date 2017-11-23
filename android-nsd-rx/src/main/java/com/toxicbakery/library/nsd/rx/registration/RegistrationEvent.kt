package com.toxicbakery.library.nsd.rx.registration

import android.net.nsd.NsdServiceInfo

sealed class RegistrationEvent
data class ServiceRegistered(
        val registrationBinder: RegistrationBinder,
        val nsdServiceInfo: NsdServiceInfo) : RegistrationEvent()

data class ServiceUnregistered(
        val registrationBinder: RegistrationBinder,
        val nsdServiceInfo: NsdServiceInfo) : RegistrationEvent()