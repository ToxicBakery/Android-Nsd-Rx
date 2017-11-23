package com.toxicbakery.application.nsd.rx

import android.app.Activity
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.toxicbakery.library.nsd.rx.NsdManagerRx
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryConfiguration
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryEvent
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryServiceFound
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryServiceLost
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private val toggleButton: Button by bind(R.id.toggle)
    private val statusTextView: TextView by bind(R.id.status)
    private val recyclerView: RecyclerView by bind(R.id.recycler_view)

    private val nsdManagerRx: NsdManagerRx by lazy { NsdManagerRx(this) }
    private val adapter: DiscoveryAdapter by lazy { DiscoveryAdapter() }
    private var subscription: Disposable = Disposables.disposed()

    fun <T : View> Activity.bind(@IdRes id: Int) = lazy { findViewById<T>(id) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        toggleButton.setOnClickListener { toggle() }
        updateUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopDiscovery()
    }

    fun updateUI() {
        statusTextView.setText(
                if (subscription.isDisposed) R.string.activity_main_status_discovery_off
                else R.string.activity_main_status_discovery_on)
    }

    fun toggle() {
        if (subscription.isDisposed) startDiscovery()
        else stopDiscovery()
    }

    fun startDiscovery() {
        subscription = nsdManagerRx.discoverServices(DiscoveryConfiguration("_services._dns-sd._udp"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { event: DiscoveryEvent ->
                            Log.d(TAG, "Event $event")
                            when (event) {
                                is DiscoveryServiceFound -> adapter.addItem(event.service.toDiscoveryRecord())
                                is DiscoveryServiceLost -> adapter.removeItem(event.service.toDiscoveryRecord())
                            }
                        },
                        { Log.e(TAG, "Error starting discovery.", it) })

        updateUI()
    }

    fun stopDiscovery() {
        subscription.dispose()
        updateUI()
        adapter.clear()
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}