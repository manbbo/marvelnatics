package br.com.digitalhouse.marvelnaticos.marvelnatics.adapters

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Comic
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.comic.ComicFragment
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main.MainActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ComicSearchAdapter(private val context: Context, private val listComics: ArrayList<Comic?>, var ctx: MainActivity):RecyclerView.Adapter<ComicSearchAdapter.CustomViewHolder>(){

    inner class ComicSearchViewHolder(itemView: View):CustomViewHolder(itemView){
        var tvTitulo = view.findViewById<TextView>(R.id.tv_buscaResultado_titulo)
        var imvComic = view.findViewById<ImageView>(R.id.img_buscaResultado_imagem)
        var desc = view.findViewById<TextView>(R.id.tv_buscaResultado_descricao)
        var dataComic = view.findViewById<TextView>(R.id.tv_buscaResultado_ano)
    }

    inner class LoadingViewHolder(itemView: View):CustomViewHolder(itemView)

    open inner class CustomViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var view = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        if (viewType == 1) {
            val root = LayoutInflater.from(parent.context).inflate(R.layout.item_busca_resultado, parent, false)
            return ComicSearchViewHolder(root)
        }else{
            val root = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            return LoadingViewHolder(root)
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        if (holder is LoadingViewHolder) return
        if (holder is ComicSearchViewHolder){
            var item = listComics[position]

            holder.tvTitulo.text = item!!.title
            holder.desc.text = item!!.description
            holder.dataComic.text = item!!.dates[0].date.subSequence(0,4)
            var urlImg : String = item!!.thumbnail.path.replace("http", "https")+"."+item!!.thumbnail.extension

            Glide
                    .with(context)
                    .load(urlImg)
                    .placeholder(spinner)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.imvComic)

            holder.view.setOnClickListener {
                val t = ctx.supportFragmentManager.beginTransaction()
                val frag: DialogFragment = ComicFragment.newInstance()

                var bundle = Bundle()
                bundle.putString("title", item!!.title)
                bundle.putString("urlImage", urlImg)
                bundle.putString("desc", item!!.description)
                bundle.putString("date", item!!.dates[0].date)

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
        }
    }

    override fun getItemCount(): Int = listComics.size

    private val spinner = CircularProgressDrawable(context).apply {
        strokeCap = Paint.Cap.ROUND
        centerRadius = 40f
        strokeWidth = 15f
        start()
    }

    override fun getItemViewType(position: Int): Int {
        if (listComics[position] != null) return 1; //VIEW_TYPE_ITEM
        else return 2; //VIEW_TYPE_LOADING
    }

    fun addNullData(){
        listComics.add(null)
    }

    fun removeNullData(){
        listComics.removeAt(listComics.size - 1)
    }
}