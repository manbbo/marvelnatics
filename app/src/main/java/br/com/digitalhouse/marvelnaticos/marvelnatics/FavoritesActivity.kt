package br.com.digitalhouse.marvelnaticos.marvelnatics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.digitalhouse.marvelnaticos.marvelnatics.dao.ComicsDao
import br.com.digitalhouse.marvelnaticos.marvelnatics.database.AppDatabase
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.db.ComicColecaoInfoDB
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.db.ComicDB
import br.com.digitalhouse.marvelnaticos.marvelnatics.services.repo
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main.HomeViewModel
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main.OfflineViewModel
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.coroutines.launch

class FavoritesActivity : AppCompatActivity() {

    val viewModel: OfflineViewModel by viewModels<OfflineViewModel>{
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return OfflineViewModel(repo, this@FavoritesActivity) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        viewModel.popLists()

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
        include3.findViewById<ImageView>(R.id.iv_list_icon)
            .setImageResource(R.drawable.ic_check_circle)

        val include4: View = findViewById(R.id.include4)
        include4.findViewById<TextView>(R.id.tv_list_listName).text = "Meus Favoritos"
        include4.findViewById<ImageView>(R.id.iv_list_icon)
            .setImageResource(R.drawable.ic_baseline_star_24_checked)

        val include5: View = findViewById(R.id.include5)
        include5.findViewById<TextView>(R.id.tv_list_listName).text = "Maiores Notas"
        include5.findViewById<ImageView>(R.id.iv_list_icon)
            .setImageResource(R.drawable.ic_baseline_star_24_checked)


        val recyclerView1: RecyclerView = include1.findViewById(R.id.rv_list_listImages)
        val recyclerView2: RecyclerView = include2.findViewById(R.id.rv_list_listImages)
        val recyclerView3: RecyclerView = include3.findViewById(R.id.rv_list_listImages)
        val recyclerView4: RecyclerView = include4.findViewById(R.id.rv_list_listImages)
        val recyclerView5: RecyclerView = include5.findViewById(R.id.rv_list_listImages)

        viewModel.listComicsTenho.observe(this){
            recyclerView3.adapter = ComicDBAdapter(this, viewModel.listComicsTenho.value!!)
        }

        viewModel.listComicsFavoritos.observe(this){
            recyclerView4.adapter = ComicDBAdapter(this, viewModel.listComicsFavoritos.value!!)
        }

        viewModel.listComicsJaLi.observe(this){
            recyclerView1.adapter = ComicDBAdapter(this, viewModel.listComicsJaLi.value!!)
        }

        viewModel.listComicsQueroLer.observe(this){
            recyclerView2.adapter = ComicDBAdapter(this, viewModel.listComicsQueroLer.value!!)

            //TODO: substituir pelos dados do banco
            recyclerView5.adapter = ComicDBAdapter(this, viewModel.listComicsQueroLer.value!!)
        }

        recyclerView1.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView2.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView3.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView4.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView5.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}