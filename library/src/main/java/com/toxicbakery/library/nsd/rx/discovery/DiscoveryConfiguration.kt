package com.toxicbakery.library.nsd.rx.discovery

import android.net.nsd.NsdManager
import com.toxicbakery.library.nsd.rx.ProtocolType

data class DiscoveryConfiguration(
        val type: String,
        @ProtocolType val protocolType: Int = NsdManager.PROTOCOL_DNS_SD
)