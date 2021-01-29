package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NetworkViewModel : ViewModel() {

    val networkAvaliable = MutableLiveData<Boolean>()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        private var avaliableAmount = 0

        override fun onAvailable(network: Network) {
            avaliableAmount++
            updateStatus()
        }

        override fun onLost(network: Network) {
            avaliableAmount--
            updateStatus()
        }

        private fun updateStatus() {
            (avaliableAmount > 0).also { isAvaliable ->
                if (networkAvaliable.value != isAvaliable) {
                    setNetworkStatus(isAvaliable)
                }
            }
        }

    }

    fun registerNetworkListener(context: Context) {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).also { cm ->
            networkAvaliable.value = false
            cm.registerNetworkCallback(
                NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build(),
                networkCallback
            )
        }
    }

    fun unregisterNetworkListener(context: Context) {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).also { cm ->
            cm.unregisterNetworkCallback(networkCallback)
        }
    }

    private fun setNetworkStatus(avaliable: Boolean) {
        viewModelScope.launch { networkAvaliable.value = avaliable }
    }


}