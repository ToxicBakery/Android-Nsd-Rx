package com.toxicbakery.library.nsd.rx;

import android.net.nsd.NsdManager;

import androidx.annotation.IntDef;

import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;

@Retention(value = AnnotationRetention.SOURCE)
@IntDef({NsdManager.PROTOCOL_DNS_SD})
public @interface ProtocolType {
}
