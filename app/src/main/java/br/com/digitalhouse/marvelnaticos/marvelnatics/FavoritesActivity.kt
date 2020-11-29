package br.com.digitalhouse.marvelnaticos.marvelnatics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_favorites.*

class FavoritesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        setSupportActionBar(findViewById(R.id.favToolbar))

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        val include1: View = findViewById(R.id.include1)
        include1.findViewById<TextView>(R.id.tv_list_listName).text = "Lidos"
        include1.findViewById<ImageView>(R.id.iv_list_icon).setImageResource(R.drawable.ic_book)

        val include2: View = findViewById(R.id.include2)
        include2.findViewById<TextView>(R.id.tv_list_listName).text = "Quero Ler"
        include2.findViewById<ImageView>(R.id.iv_list_icon).setImageResource(R.drawable.ic_favorite)


        val include3: View = findViewById(R.id.include3)
        include3.findViewById<TextView>(R.id.tv_list_listName).text = "Minha Coleção"
        include3.findViewById<ImageView>(R.id.iv_list_icon).setImageResource(R.drawable.ic_check_circle)

        val include4: View = findViewById(R.id.include4)
        include4.findViewById<TextView>(R.id.tv_list_listName).text = "Meus Favoritos"
        include4.findViewById<ImageView>(R.id.iv_list_icon).setImageResource(R.drawable.ic_baseline_star_24_checked)

        val include5: View = findViewById(R.id.include5)
        include5.findViewById<TextView>(R.id.tv_list_listName).text = "Maiores Notas"
        include5.findViewById<ImageView>(R.id.iv_list_icon).setImageResource(R.drawable.ic_baseline_star_24_checked)


        val recycleView1: RecyclerView = include1.findViewById(R.id.rv_list_listImages)
        val recycleView2: RecyclerView = include2.findViewById(R.id.rv_list_listImages)
        val recycleView3: RecyclerView = include3.findViewById(R.id.rv_list_listImages)
        val recycleView4: RecyclerView = include4.findViewById(R.id.rv_list_listImages)
        val recycleView5: RecyclerView = include5.findViewById(R.id.rv_list_listImages)

        recycleView1.adapter = ItemHistoryAdapter(listLidos())
        recycleView2.adapter = ItemHistoryAdapter(listQueroLer())
        recycleView3.adapter = ItemHistoryAdapter(listMinhaColecao())
        recycleView4.adapter = ItemHistoryAdapter(listMeusFavoritos())
        recycleView5.adapter = ItemHistoryAdapter(listMaioresNotas())

        recycleView1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycleView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycleView3.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycleView4.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycleView5.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}

private fun listLidos(): List<ItemHistory> = listOf(
        ItemHistory(
                1,
                "Spider man",
                "Esse quadrinho é mto legal...",
                5
        ),
        ItemHistory(
                2,
                "Iron man",
                "Esse quadrinho é mto legal...",
                4
        ),
        ItemHistory(
                3,
                "Hulk",
                "Esse quadrinho é mto legal...",
                3
        ),
        ItemHistory(
                16,
                "Capitão América",
                "Esse quadrinho é mto legal...",
                3
        )
)

fun listQueroLer(): List<ItemHistory> = listOf(
        ItemHistory(
                4,
                "Spider man",
                "Esse quadrinho é mto legal...",
                0
        ),
        ItemHistory(
                5,
                "Iron man",
                "Esse quadrinho é mto legal...",
                0
        ),
        ItemHistory(
                6,
                "Hulk",
                "Esse quadrinho é mto legal...",
                0
        ),
        ItemHistory(
                17,
                "Capitão América",
                "Esse quadrinho é mto legal...",
                3
        )
)

fun listMinhaColecao(): List<ItemHistory> = listOf(
        ItemHistory(
                7,
                "Spider man",
                "Esse quadrinho é mto legal...",
                5
        ),
        ItemHistory(
                8,
                "Iron man",
                "Esse quadrinho é mto legal...",
                2
        ),
        ItemHistory(
                9,
                "Hulk",
                "Esse quadrinho é mto legal...",
                1
        ),
        ItemHistory(
                18,
                "Capitão América",
                "Esse quadrinho é mto legal...",
                3
        )
)

fun listMeusFavoritos(): List<ItemHistory> = listOf(
        ItemHistory(
                10,
                "Spider man",
                "Esse quadrinho é mto legal...",
                5
        ),
        ItemHistory(
                11,
                "Iron man",
                "Esse quadrinho é mto legal...",
                5
        ),
        ItemHistory(
                12,
                "Hulk",
                "Esse quadrinho é mto legal...",
                5
        ),
        ItemHistory(
                19,
                "Capitão América",
                "Esse quadrinho é mto legal...",
                3
        )
)

fun listMaioresNotas(): List<ItemHistory> = listOf(
        ItemHistory(
                13,
                "Spider man",
                "Esse quadrinho é mto legal...",
                5
        ),
        ItemHistory(
                14,
                "Iron man",
                "Esse quadrinho é mto legal...",
                5
        ),
        ItemHistory(
                15,
                "Hulk",
                "Esse quadrinho é mto legal...",
                5
        ),
        ItemHistory(
                20,
                "Capitão América",
                "Esse quadrinho é mto legal...",
                3
        )
)