package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.colecao

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView.OnEditorActionListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.ComicCollectionAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.services.repo
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main.OfflineViewModel


class ColecaoActivity : AppCompatActivity() {

    val viewModel: OfflineViewModel by viewModels<OfflineViewModel>{
        object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return OfflineViewModel(repo, this@ColecaoActivity) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colecao)
        viewModel.getAllComics()

        val rv:RecyclerView = findViewById(R.id.rv_organizarColecao_resultado)
        val backBtn: ImageButton = findViewById(R.id.ib_colecao_backBtn)
        val rvColecao : RecyclerView = findViewById(R.id.rv_organizarColecao_resultado)
        val txtPesquisar : EditText =  findViewById(R.id.et_busca_colecao)

        viewModel.listComics.observe(this){
            viewModel.getAllInfos()
        }

        viewModel.listInfos.observe(this){
            rvColecao.adapter = ComicCollectionAdapter(this, viewModel, viewModel.listComics.value!!, viewModel.listInfos.value!!)
        }

        viewModel.listInfosSearch.observe(this){
            rvColecao.adapter = ComicCollectionAdapter(this, viewModel, viewModel.listComicsSearch.value!!, viewModel.listInfosSearch.value!!)
        }

        txtPesquisar.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.filterListByTitle(txtPesquisar.text.toString())
                return@OnEditorActionListener true
            }
            false
        })

//        rv.adapter = ComicSearchAdapter(this, mutableListOf(
//            //Comic(),
//            //Comic(),
//            //Comic(),
//            //Comic(),
//            //Comic()
//        ), this)

        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)

        backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.static_animation, R.anim.slide_out_bottom)
    }
}