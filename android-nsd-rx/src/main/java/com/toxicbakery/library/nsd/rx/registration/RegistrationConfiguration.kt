package com.toxicbakery.library.nsd.rx.registration

import android.net.nsd.NsdManager
import com.toxicbakery.library.nsd.rx.ProtocolType

data class RegistrationConfiguration(
        val serviceName: String = "default",
        val serviceType: String = "_http._tcp.",
        val port: Int,

        @ProtocolType
        val protocolType: Int = NsdManager.PROTOCOL_DNS_SD
)