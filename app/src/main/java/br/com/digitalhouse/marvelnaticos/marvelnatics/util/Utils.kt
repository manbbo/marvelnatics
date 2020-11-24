package br.com.digitalhouse.marvelnaticos.marvelnatics.util

import android.content.Context

class Utils {
    companion object {
        fun calculateSpan(context: Context, width: Int): Int {
            val displayMetrics = context.resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels / displayMetrics.density
            return (dpWidth / width).toInt()
        }

        fun calculateMargin(context: Context, spanCount: Int, width: Int): Int {
            val displayMetrics = context.resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels / displayMetrics.density
            val widthLeft = dpWidth - (width * spanCount)
            return (widthLeft/(spanCount*2)).toInt()
        }
    }
}