package br.com.digitalhouse.marvelnaticos.marvelnatics.util

import android.content.Context
import android.graphics.PorterDuff
import android.widget.ImageView
import androidx.core.content.ContextCompat
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import java.math.BigInteger
import java.security.MessageDigest

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
            return (widthLeft / (spanCount * 2)).toInt()
        }

        fun hashFormat(privateKey: String, pkey: String, dateTime: String): String {
            val str = "${dateTime}${privateKey}${pkey}"
            return md5hash(str)
        }

        fun md5hash(str: String): String {
            val md = MessageDigest.getInstance("MD5")
            val bigInt = BigInteger(1, md.digest(str.toByteArray(Charsets.UTF_8)))
            return String.format("%032x", bigInt)
        }


        fun colorStars(views: Array<ImageView>, amount: Int, context: Context) {
            views.forEachIndexed { i, v ->
                if (i < amount) {
                    v.setColorFilter(ContextCompat.getColor(context, R.color.favoritebt), PorterDuff.Mode.SRC_IN)
                } else {
                    v.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_IN)
                }
            }
        }
    }
}