package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.colecao

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.ComicSearchAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Comic

class ColecaoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colecao)

        val rv:RecyclerView = findViewById(R.id.rv_organizarColecao_resultado)
        val backBtn: ImageButton = findViewById(R.id.ib_colecao_backBtn)
        rv.adapter = ComicSearchAdapter(this, mutableListOf(
            Comic(),
            Comic(),
            Comic(),
            Comic(),
            Comic()
        ))

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