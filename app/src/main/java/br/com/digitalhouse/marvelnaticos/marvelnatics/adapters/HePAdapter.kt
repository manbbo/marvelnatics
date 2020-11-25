package br.com.digitalhouse.marvelnaticos.marvelnatics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Comic

class HePAdapter(private val context: Context, private val listHeP: MutableList<Comic>) :
    PagerAdapter() {
    override fun getCount(): Int = listHeP.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return `object` == view
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val root = LayoutInflater.from(context)
            .inflate(R.layout.item_comiclandscape, container, false)
        container.addView(root)
        return root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}