package com.toxicbakery.library.nsd.rx

import android.net.nsd.NsdServiceInfo
import android.support.test.InstrumentationRegistry
import android.support.test.filters.SmallTest
import android.support.test.runner.AndroidJUnit4
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryConfiguration
import com.toxicbakery.library.nsd.rx.registration.RegistrationConfiguration
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import java.net.ServerSocket

@RunWith(AndroidJUnit4::class)
@SmallTest
class NsdManagerRxTest {

    private lateinit var nsdManagerCompat: INsdManagerCompat
    private lateinit var nsdManagerRx: NsdManagerRx
    private lateinit var registrationConfiguration: RegistrationConfiguration
    private lateinit var serverSocket: ServerSocket
    private val nsdConfiguration = DiscoveryConfiguration("_http._tcp.")

    @Before
    fun setup() {
        serverSocket = ServerSocket(0)
        registrationConfiguration = RegistrationConfiguration(port = serverSocket.localPort)
        nsdManagerCompat = mock()
        nsdManagerRx = NsdManagerRx(nsdManagerCompat)
    }

    @After
    fun teardown() {
        serverSocket.close()
    }

    @Test
    fun secondConstructor() {
        NsdManagerRx(InstrumentationRegistry.getTargetContext())
    }

    @Test
    fun discoverServices() =
        nsdManagerRx.discoverServices(nsdConfiguration)
            .subscribe()
            .dispose()

    @Test
    fun discoverServicesWithListener() {
        nsdManagerRx.discoverServices(nsdConfiguration) { _, _ -> mock() }
            .subscribe()
            .dispose()
    }

    @Test
    fun registerService() {
        nsdManagerRx.registerService(registrationConfiguration)
            .subscribe()
            .dispose()
    }

    @Test
    fun registerServiceWithListener() {
        nsdManagerRx.registerService(registrationConfiguration) { _, _ -> mock() }
            .subscribe()
            .dispose()
    }

    @Test
    fun resolveService() {
        NsdServiceInfo().let {
            nsdManagerRx.resolveService(it)
                .subscribe()
                .dispose()
        }
    }

    @Test
    fun resolveServiceWithListener() {
        NsdServiceInfo().let {
            nsdManagerRx.resolveService(it) { mock() }
                .subscribe()
                .dispose()
        }
    }

}
