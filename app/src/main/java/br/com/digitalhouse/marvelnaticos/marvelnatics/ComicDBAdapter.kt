package br.com.digitalhouse.marvelnaticos.marvelnatics

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.db.ComicDB
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.comic.ComicFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ComicDBAdapter(private val context: Context, val lists: List<ComicDB?>) : RecyclerView.Adapter<ComicDBAdapter.ComicDBViewHolder>() {


    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ComicDBAdapter.ComicDBViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_histories, parent, false)
        return ComicDBViewHolder(itemView)
    }

    inner class ComicDBViewHolder(listView: View) : RecyclerView.ViewHolder(listView) {
        val view = itemView
        val iv_item_comic: ImageView = itemView.findViewById(R.id.iv_item_comic)
        val iv_item_star1: ImageView = itemView.findViewById(R.id.iv_item_star1)
        val iv_item_star2: ImageView = itemView.findViewById(R.id.iv_item_star2)
        val iv_item_star3: ImageView = itemView.findViewById(R.id.iv_item_star3)
        val iv_item_star4: ImageView = itemView.findViewById(R.id.iv_item_star4)
        val iv_item_star5: ImageView = itemView.findViewById(R.id.iv_item_star5)
    }

    override fun onBindViewHolder(holder: ComicDBViewHolder, position: Int) {
        val currentItem = lists[position]

        Glide
                .with(context)
                .load(currentItem!!.imagemCapaUrl)
                .placeholder(spinner)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.iv_item_comic)

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
//        if(currentItem.rate >= 1) { holder.iv_item_star1.setImageResource(R.drawable.ic_baseline_star_24_checked)}
//        if(currentItem.rate >= 2) { holder.iv_item_star2.setImageResource(R.drawable.ic_baseline_star_24_checked)}
//        if(currentItem.rate >= 3) { holder.iv_item_star3.setImageResource(R.drawable.ic_baseline_star_24_checked)}
//        if(currentItem.rate >= 4) { holder.iv_item_star4.setImageResource(R.drawable.ic_baseline_star_24_checked)}
//        if(currentItem.rate == 5) { holder.iv_item_star5.setImageResource(R.drawable.ic_baseline_star_24_checked)}
    }

    override fun getItemCount(): Int = lists.size

    private val spinner = CircularProgressDrawable(context).apply {
        strokeCap = Paint.Cap.ROUND
        centerRadius = 40f
        strokeWidth = 15f
        start()
    }
}