package com.toxicbakery.library.nsd.rx

import org.mockito.Mockito

inline fun <reified T> mock() = Mockito.mock(T::class.java)

fun <T> verify(mock: T) = Mockito.verify(mock)