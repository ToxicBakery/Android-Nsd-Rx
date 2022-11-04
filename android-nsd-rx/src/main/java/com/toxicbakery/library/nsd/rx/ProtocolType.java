package com.toxicbakery.library.nsd.rx;

import android.net.nsd.NsdManager;
import androidx.annotation.IntDef;

import kotlin.annotation.Retention;

@Retention()
@IntDef({
        NsdManager.PROTOCOL_DNS_SD
})
public @interface ProtocolType {
}
