package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.colecao

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
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
    lateinit var rvColecao: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colecao)
        viewModel.getAllComics()

        val backBtn: ImageButton = findViewById(R.id.ib_colecao_backBtn)
        rvColecao = findViewById(R.id.rv_organizarColecao_resultado)
        val txtPesquisar : EditText =  findViewById(R.id.et_busca_colecao)
        val message : TextView = findViewById(R.id.loading)

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
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                return@OnEditorActionListener true
            }
            false
        })

        viewModel.listComics.observe(this, Observer { listComics ->
            if (listComics.isEmpty())  {
                    Log.i("ViewModel", "onCreate: CRIOU")
                rvColecao.visibility = View.INVISIBLE
                    message.visibility = View.VISIBLE
                } else {
                Log.i("ViewModel", "onCreate: NAO CRIOU")
                rvColecao.visibility = View.VISIBLE
                message.visibility = View.INVISIBLE
            }
                })

        rvColecao.layoutManager = LinearLayoutManager(this)
        rvColecao.setHasFixedSize(true)

        backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.static_animation, R.anim.slide_out_bottom)
    }
}