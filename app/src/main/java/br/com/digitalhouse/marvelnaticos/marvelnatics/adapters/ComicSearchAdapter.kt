package br.com.digitalhouse.marvelnaticos.marvelnatics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Comic

class ComicSearchAdapter(private val context: Context, private val listComics : MutableList<Comic>):RecyclerView.Adapter<ComicSearchAdapter.ComicSearchViewHolder>(){

    class ComicSearchViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicSearchViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.item_colecao, parent, false)
        return ComicSearchViewHolder(root)
    }

    override fun onBindViewHolder(holder: ComicSearchViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = listComics.size
}