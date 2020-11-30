package br.com.digitalhouse.marvelnaticos.marvelnatics.interfaces

import android.view.View

// Interface para definar o click no card das Historias em Destaques
interface HePClickListener {
    fun onHePClickListener(position: Int) : View.OnClickListener
}