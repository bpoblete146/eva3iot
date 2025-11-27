package com.example.myapplications

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddNoticia: FloatingActionButton
    private lateinit var noticiaAdapter: NoticiaAdapter
    private val noticias = mutableListOf<Noticia>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        db = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.recyclerView)
        fabAddNoticia = findViewById(R.id.fabAddNoticia)

        recyclerView.layoutManager = LinearLayoutManager(this)
        noticiaAdapter = NoticiaAdapter(noticias) { noticia ->
            val intent = Intent(this, VerNoticiaActivity::class.java).apply {
                putExtra("noticia_id", noticia.id)
            }
            startActivity(intent)
        }
        recyclerView.adapter = noticiaAdapter

        fabAddNoticia.setOnClickListener {
            startActivity(Intent(this, AddNoticiaActivity::class.java))
        }

        cargarNoticias()
    }

    private fun cargarNoticias() {
        db.collection("noticias").orderBy("fecha", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(this, "Error al cargar noticias: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    noticias.clear()
                    noticias.addAll(snapshots.toObjects(Noticia::class.java))
                    noticiaAdapter.notifyDataSetChanged()
                }
            }
    }
}