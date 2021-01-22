package br.com.digitalhouse.marvelnaticos.marvelnatics.adapters


import android.content.Context
import android.graphics.Paint
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import br.com.digitalhouse.marvelnaticos.marvelnatics.ComicDBAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.db.ComicDB
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.comic.ComicFragment
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main.OfflineViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.util.*
import kotlin.collections.ArrayList

class ComicCollectionAdapter(private val context: Context, val viewModel: OfflineViewModel, private val listComics : MutableList<ComicDB?>, private val listInfos : MutableList<List<String>>):RecyclerView.Adapter<ComicCollectionAdapter.ComicCollectionViewHolder>(){

    private var comicId: Int = 0
    private lateinit var title: String
    private lateinit var originalText: String
    private lateinit var dataP: String
    private lateinit var urlImg: String
    private lateinit var cover: String
    private lateinit var creators: String
    private lateinit var drawers: String
    private var countFav = BooleanArray(listComics.size){false}
    private var countQler = BooleanArray(listComics.size){false}
    private var countJali = BooleanArray(listComics.size){false}
    private var countTenho = BooleanArray(listComics.size){false}
    private lateinit var ctx : Context


    class ComicCollectionViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val view = itemView
        val iv_item_comic: ImageView = itemView.findViewById(R.id.img_itemColecao_imagem)
        val tv_item_comic: TextView = itemView.findViewById(R.id.tv_itemColecao_nome)
        val btFavorito : ImageView = itemView.findViewById(R.id.bt_favorito_colecao)
        val btQueroler : ImageView = itemView.findViewById(R.id.bt_queroler_colecao)
        val btJali : ImageView = itemView.findViewById(R.id.bt_jali_colecao)
        val btTenho : ImageView = itemView.findViewById(R.id.bt_tenho_colecao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicCollectionViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.item_colecao, parent, false)

        ctx = parent.context

        return ComicCollectionViewHolder(root)
    }

    override fun onBindViewHolder(holder: ComicCollectionViewHolder, position: Int) {
        val currentItem = listComics[holder.adapterPosition]

        Glide
                .with(context)
                .load(currentItem!!.imagemCapaUrl)
                .placeholder(spinner)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.iv_item_comic)

        holder.tv_item_comic.text = currentItem.titulo

        val currentListInfos = listInfos[holder.adapterPosition]
        currentListInfos.forEach {
            if (it == "T") {
                holder.btTenho?.setColorFilter(ContextCompat.getColor(ctx, R.color.tenhobt), PorterDuff.Mode.SRC_IN)
                countTenho[holder.adapterPosition] = true
            } else if (it == "J") {
                holder.btJali?.setColorFilter(ContextCompat.getColor(ctx, R.color.jalibt), PorterDuff.Mode.SRC_IN)
                countJali[holder.adapterPosition] = true
            } else if (it == "Q") {
                holder.btQueroler?.setColorFilter(ContextCompat.getColor(ctx, R.color.querolerbt), PorterDuff.Mode.SRC_IN)
                countQler[holder.adapterPosition] = true
            } else {
                holder.btFavorito?.setColorFilter(ContextCompat.getColor(ctx, R.color.favoritebt), PorterDuff.Mode.SRC_IN)
                countFav[holder.adapterPosition] = true
            }
        }

        // Botoes de ação
        holder.btFavorito.setOnClickListener {
            Log.i("POSITION", holder.adapterPosition.toString())
            setItemValues(holder.adapterPosition)
            if (!countFav[holder.adapterPosition]) {
                holder.btFavorito?.setColorFilter(ContextCompat.getColor(ctx, R.color.favoritebt), PorterDuff.Mode.SRC_IN)
                viewModel.insertComicInList(comicId, title, originalText, dataP, drawers, cover, creators, urlImg, "F")
            } else {
                holder.btFavorito?.setColorFilter(ContextCompat.getColor(ctx, R.color.black), PorterDuff.Mode.SRC_IN)
                viewModel.removeComicFromList(comicId, title, originalText, dataP, drawers, cover, creators, urlImg, "F")
            }

            countFav[holder.adapterPosition] = !countFav[holder.adapterPosition]

            Toast.makeText(ctx, "Você clicou em 'FAVORITOS'", Toast.LENGTH_SHORT).show()
        }

        holder.btQueroler.setOnClickListener {
            Log.i("POSITION", holder.adapterPosition.toString())
            setItemValues(holder.adapterPosition)
            if (!countQler[holder.adapterPosition]) {
                holder.btQueroler?.setColorFilter(ContextCompat.getColor(ctx, R.color.querolerbt), PorterDuff.Mode.SRC_IN)
                viewModel.insertComicInList(comicId, title, originalText, dataP, drawers, cover, creators, urlImg, "Q")
            } else {
                holder.btQueroler?.setColorFilter(ContextCompat.getColor(ctx, R.color.black), PorterDuff.Mode.SRC_IN)
                viewModel.removeComicFromList(comicId, title, originalText, dataP, drawers, cover, creators, urlImg, "Q")
            }

            countQler[holder.adapterPosition] = !countQler[holder.adapterPosition]

            Toast.makeText(ctx, "Você clicou em 'QUERO LER'", Toast.LENGTH_SHORT).show()
        }

        holder.btJali.setOnClickListener {
            Log.i("POSITION", holder.adapterPosition.toString())
            setItemValues(holder.adapterPosition)
            if (!countJali[holder.adapterPosition]) {
                holder.btJali?.setColorFilter(ContextCompat.getColor(ctx, R.color.jalibt), PorterDuff.Mode.SRC_IN)
                viewModel.insertComicInList(comicId, title, originalText, dataP, drawers, cover, creators, urlImg, "J")
            }else {
                holder.btJali?.setColorFilter(ContextCompat.getColor(ctx, R.color.black), PorterDuff.Mode.SRC_IN)
                viewModel.removeComicFromList(comicId, title, originalText, dataP, drawers, cover, creators, urlImg, "J")
            }

            countJali[holder.adapterPosition] = !countJali[holder.adapterPosition]

            Toast.makeText(ctx, "Você clicou em 'Ja li'", Toast.LENGTH_SHORT).show()
        }

        holder.btTenho.setOnClickListener {
            Log.i("POSITION", holder.adapterPosition.toString())
            setItemValues(holder.adapterPosition)
            if (!countTenho[holder.adapterPosition]) {
                holder.btTenho?.setColorFilter(ContextCompat.getColor(ctx, R.color.tenhobt), PorterDuff.Mode.SRC_IN)
                viewModel.insertComicInList(comicId, title, originalText, dataP, drawers, cover, creators, urlImg, "T")
            }
            else{
                holder.btTenho?.setColorFilter(ContextCompat.getColor(ctx, R.color.black), PorterDuff.Mode.SRC_IN)
                viewModel.removeComicFromList(comicId, title, originalText, dataP, drawers, cover, creators, urlImg, "T")
            }

            countTenho[holder.adapterPosition] = !countTenho[holder.adapterPosition]

            Toast.makeText(ctx, "Você clicou em 'TENHO'", Toast.LENGTH_SHORT).show()
        }

        holder.view.setOnClickListener {
            if (context is AppCompatActivity){
                val t = context.supportFragmentManager.beginTransaction()
                val frag: DialogFragment = ComicFragment.newInstance()

                var bundle = Bundle()
                bundle.putString("title", currentItem!!.titulo)
                bundle.putInt("id", currentItem!!.apiID)
                bundle.putString("urlImage", currentItem!!.imagemCapaUrl)
                bundle.putString("desc", currentItem!!.descricao)
                bundle.putString("date", currentItem!!.dataPublicacao)
                bundle.putString("creators", currentItem!!.criadores)
                bundle.putString("drawers", currentItem!!.desenhistas)
                bundle.putString("cover", currentItem!!.artistasCapa)

                frag.arguments = bundle

                frag.show(t, "Comic")
            }
        }
    }

    override fun getItemCount(): Int = listComics.size

    override fun getItemViewType(position: Int): Int = position

    fun setItemValues(position: Int){
        var currentItem = listComics[position]!!
        comicId = currentItem.apiID
        title = currentItem.titulo
        originalText = currentItem.descricao
        dataP = currentItem.dataPublicacao
        urlImg = currentItem.imagemCapaUrl
        cover = currentItem.artistasCapa
        creators = currentItem.criadores
        drawers = currentItem.desenhistas
    }

    private val spinner = CircularProgressDrawable(context).apply {
        strokeCap = Paint.Cap.ROUND
        centerRadius = 40f
        strokeWidth = 15f
        start()
    }
}