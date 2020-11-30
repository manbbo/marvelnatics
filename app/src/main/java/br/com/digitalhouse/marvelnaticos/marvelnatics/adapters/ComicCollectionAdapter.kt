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
                btFavorito?.setColorFilter(ContextCompat.getColor(ctx, R.color.favoritebt), android.graphics.PorterDuff.Mode.SRC_IN)
                countFav = true
            }
            else {

                btFavorito?.setColorFilter(ContextCompat.getColor(ctx, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN)
                countFav = false
            }

            Toast.makeText(ctx, "Você clicou em 'FAVORITOS'", Toast.LENGTH_SHORT).show()
        }

        var countQler = false
        btQueroler.setOnClickListener {
            if (!countQler) {
                btQueroler?.setColorFilter(ContextCompat.getColor(ctx, R.color.querolerbt), android.graphics.PorterDuff.Mode.SRC_IN)
                countQler =true
            }
            else {
                 btQueroler?.setColorFilter(ContextCompat.getColor(ctx, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN)
                countQler = false
            }
            Toast.makeText(ctx, "Você clicou em 'QUERO LER'", Toast.LENGTH_SHORT).show()
        }

        var countJali = false
        btJali.setOnClickListener {
            if (!countJali) {
                btJali?.setColorFilter(ContextCompat.getColor(ctx, R.color.jalibt), android.graphics.PorterDuff.Mode.SRC_IN)
                countJali = true
            }
            else {
               btJali?.setColorFilter(ContextCompat.getColor(ctx, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN)
                countJali = false
            }

            Toast.makeText(ctx, "Você clicou em 'Ja li'", Toast.LENGTH_SHORT).show()
        }

        var countTenho = false
        btTenho.setOnClickListener {
            if (!countTenho) {
               btTenho?.setColorFilter(ContextCompat.getColor(ctx, R.color.tenhobt), android.graphics.PorterDuff.Mode.SRC_IN)
                countTenho = true
            }
            else {
                btTenho?.setColorFilter(ContextCompat.getColor(ctx, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN)
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