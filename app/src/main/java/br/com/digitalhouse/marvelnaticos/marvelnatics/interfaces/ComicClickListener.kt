package br.com.digitalhouse.marvelnaticos.marvelnatics.interfaces

import android.view.View

interface ComicClickListener {
    fun onComicClickListener(position: Int) : View.OnClickListener
}