package com.toxicbakery.library.nsd.rx;

import android.net.nsd.NsdManager;
import android.support.annotation.IntDef;

import kotlin.annotation.Retention;

@Retention()
@IntDef({
        NsdManager.PROTOCOL_DNS_SD
})
public @interface ProtocolType {
}
