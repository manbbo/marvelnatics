package br.com.digitalhouse.marvelnaticos.marvelnatics.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.interfaces.HePClickListener
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Comic
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class HePAdapter(
    private val context: Context,
    private val listener: HePClickListener
) : PagerAdapter() {

    private val spinner = CircularProgressDrawable(context).apply {
        strokeCap = Paint.Cap.ROUND
        centerRadius = 40f
        strokeWidth = 15f
        start()
    }

    var listHeP = mutableListOf<Comic>()

    override fun getCount(): Int = listHeP.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return `object` == view
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.item_comiclandscape, container, false)

        /*
        Pega a referencia do cardview presente no item_comiclandscape.xml para setar a
        Ação de click.
         */

        val card: CardView = view.findViewById(R.id.cv_hep_view)

        // Define a ação de click
//        card.setOnClickListener(listener.onHePClickListener(position)) // TODO ESTA BUGANDO O SCROLL


        (container as ViewPager).addView(view)


        // TODO COLOCAR UMA IMAGEM MEHOR
        Glide
            .with(context)
            .load(listHeP[position].let { comic ->
                "${comic.thumbnail.path}.${comic.thumbnail.extension}".replace("http", "https")
            })
            .placeholder(spinner)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view.findViewById<ImageView>(R.id.iv_story))

        view.findViewById<TextView>(R.id.tv_comic_name).text = listHeP[position].title // TODO TITULO

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}