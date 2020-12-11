package br.com.digitalhouse.marvelnaticos.marvelnatics.adapters

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.interfaces.HePClickListener
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Comic
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.comic.ComicFragment
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main.MainActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class HePAdapter(
    private val context: Context,
    private val listHeP: MutableList<Comic>,
    var ctx: MainActivity
) : PagerAdapter() {

    private val spinner = CircularProgressDrawable(context).apply {
        strokeCap = Paint.Cap.ROUND
        centerRadius = 40f
        strokeWidth = 15f
        start()
    }

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
        card.setOnClickListener {
            var item = listHeP[position]
            var urlImg : String = item.thumbnail.path.replace("http", "https")+"."+item.thumbnail.extension
            val t = ctx.supportFragmentManager.beginTransaction()
            val frag: DialogFragment = ComicFragment.newInstance()

            var bundle = Bundle()
            bundle.putString("title", item.title)
            bundle.putString("urlImage", urlImg)
            bundle.putString("desc", item.description)
            bundle.putString("date", item.dates[0].date)

            var strCreator = ""
            var strCover = ""
            var strDraw = ""
            for (it in item.creators.items){
                if (it.role == "writer") strCreator += it.name + ", "
                else if (it.role.contains("cover"))  strCover += it.name + ", "
                else if (it.role.contains("colorist") || it.role.contains("inker") )  strDraw += it.name + ", "
            }
            var l = strCreator.length
            if (l > 1) strCreator = strCreator.substring(0, l - 2) + "."

            l = strCover.length
            if (l > 1) strCover = strCover.substring(0, l - 2) + "."

            l = strDraw.length
            if (l > 1) strDraw = strDraw.substring(0, l - 2) + "."


            bundle.putString("creators", strCreator)
            bundle.putString("drawers", strDraw)
            bundle.putString("cover", strCover)

            frag.arguments = bundle

            frag.show(t, "Comic")
        }


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

        view.findViewById<TextView>(R.id.tv_comic_name).text = listHeP[position].title

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}