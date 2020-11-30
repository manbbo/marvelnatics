package br.com.digitalhouse.marvelnaticos.marvelnatics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.interfaces.ComicClickListener
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Comic
import br.com.digitalhouse.marvelnaticos.marvelnatics.util.Utils

class ComicsAdapter(
    private val context: Context,
    val listComics: MutableList<Comic>,
    private val listener: ComicClickListener
) :
    RecyclerView.Adapter<ComicsAdapter.ComicViewHolder>() {

    inner class ComicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivComic: ImageView = itemView.findViewById(R.id.iv_item_comic)
        val star1: ImageView = itemView.findViewById(R.id.iv_item_star1)
        val star2: ImageView = itemView.findViewById(R.id.iv_item_star2)
        val star3: ImageView = itemView.findViewById(R.id.iv_item_star3)
        val star4: ImageView = itemView.findViewById(R.id.iv_item_star4)
        val star5: ImageView = itemView.findViewById(R.id.iv_item_star5)

        init {
            itemView.setOnClickListener(listener.onComicClickListener(adapterPosition))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.item_histories, parent, false)
        return ComicViewHolder(root)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        val layoutParams = holder.ivComic.layoutParams as ViewGroup.MarginLayoutParams
        val margin = Utils.calculateMargin(context, Utils.calculateSpan(context, 114), 114)
    }

    override fun getItemCount(): Int = listComics.size
}