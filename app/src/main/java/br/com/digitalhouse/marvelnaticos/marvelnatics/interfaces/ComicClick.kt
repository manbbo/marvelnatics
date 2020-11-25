package br.com.digitalhouse.marvelnaticos.marvelnatics.interfaces

import android.view.View

interface ComicClick {

    fun onComicClick(position: Int): View.OnClickListener
}