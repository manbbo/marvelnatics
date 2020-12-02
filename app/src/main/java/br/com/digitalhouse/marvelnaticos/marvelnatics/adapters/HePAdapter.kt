package br.com.digitalhouse.marvelnaticos.marvelnatics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.interfaces.HePClickListener
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Comic

class HePAdapter(
    private val context: Context,
    private val listHeP: MutableList<Comic>,
    private val listener: HePClickListener
) :
    PagerAdapter() {
    override fun getCount(): Int = listHeP.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return `object` == view
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.item_comiclandscape, container, false)

        /*
        Pega a referencia do cardview presente no item_comiclandscape.xml para setar a
        Ação de click.
         */
        val card: CardView = view.findViewById(R.id.cv_hep_view)

        // Define a ação de click
        card.setOnClickListener(listener.onHePClickListener(position))

        (container as ViewPager).addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}