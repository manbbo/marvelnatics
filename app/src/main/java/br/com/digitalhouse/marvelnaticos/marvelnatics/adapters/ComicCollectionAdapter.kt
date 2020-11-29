package br.com.digitalhouse.marvelnaticos.marvelnatics.adapters


import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Comic

class ComicCollectionAdapter(private val context: Context, private val listComics : MutableList<Comic>):RecyclerView.Adapter<ComicCollectionAdapter.ComicCollectionViewHolder>(){

    class ComicCollectionViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicCollectionViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.item_colecao, parent, false)

        val ctx : Context = parent.context

        val btFavorito : ImageView = root.findViewById(R.id.bt_favorito_colecao)
        val btQueroler : ImageView = root.findViewById(R.id.bt_queroler_colecao)
        val btJali : ImageView = root.findViewById(R.id.bt_jali_colecao)
        val btTenho : ImageView = root.findViewById(R.id.bt_tenho_colecao)

        // Botoes de ação
        var countFav = false
        btFavorito.setOnClickListener {
            if (!countFav) {

                val wrappedDrawable: Drawable = btFavorito.background
                DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(ctx, R.color.favoritebt))
                btFavorito?.background = wrappedDrawable
                countFav = true
            }
            else {
                val wrappedDrawable: Drawable = btFavorito.background
                DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(ctx, R.color.black))
                btFavorito?.background = wrappedDrawable
                countFav = false
            }

            Toast.makeText(ctx, "Você clicou em 'FAVORITOS'", Toast.LENGTH_SHORT).show()
        }

        var countQler = false
        btQueroler.setOnClickListener {
            if (!countQler) {
                val wrappedDrawable: Drawable = btQueroler.background
                DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(ctx, R.color.querolerbt))
                btQueroler?.background = wrappedDrawable
                countQler =true
            }
            else {
                val wrappedDrawable: Drawable = btQueroler.background
                DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(ctx, R.color.black))
                btQueroler?.background = wrappedDrawable
                countQler = false
            }
            Toast.makeText(ctx, "Você clicou em 'QUERO LER'", Toast.LENGTH_SHORT).show()
        }

        var countJali = false
        btJali.setOnClickListener {
            if (!countJali) {
                val wrappedDrawable: Drawable = btJali.background
                DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(ctx, R.color.jalibt))
                btJali?.background = wrappedDrawable
                countJali = true
            }
            else {
                val wrappedDrawable: Drawable = btJali.background
                DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(ctx, R.color.black))
                btJali?.background = wrappedDrawable
                countJali = false
            }

            Toast.makeText(ctx, "Você clicou em 'Ja li'", Toast.LENGTH_SHORT).show()
        }

        var countTenho = false
        btTenho.setOnClickListener {
            if (!countTenho) {
                val wrappedDrawable: Drawable = btTenho.background
                DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(ctx, R.color.tenhobt))
                btTenho?.background = wrappedDrawable
                countTenho = true
            }
            else {
                val wrappedDrawable: Drawable = btTenho.background
                DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(ctx, R.color.black))
                btTenho?.background = wrappedDrawable
                countTenho = false
            }

            Toast.makeText(ctx, "Você clicou em 'TENHO'", Toast.LENGTH_SHORT).show()
        }
        //////

        return ComicCollectionViewHolder(root)
    }

    override fun onBindViewHolder(holder: ComicCollectionViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = listComics.size
}