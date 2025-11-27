package com.example.myapplications

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        val btnAddSampleNoticia: Button = findViewById(R.id.btnAddSampleNoticia)

        // Mostrar el botón de ejemplo solo en modo debug
        if (0 != (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE)) {
            btnAddSampleNoticia.visibility = View.VISIBLE
        }

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

        btnAddSampleNoticia.setOnClickListener {
            crearNoticiaDeEjemplo()
        }

        cargarNoticias()
    }

    private fun crearNoticiaDeEjemplo() {
        val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val noticiaEjemplo = Noticia(
            titulo = "Noticia de Ejemplo",
            resumen = "Este es el resumen de una noticia de prueba.",
            contenido = "Este es el contenido completo y detallado de la noticia de ejemplo. Sirve para verificar que la funcionalidad de la aplicación es correcta.",
            autor = "Desarrollador",
            fecha = fecha
        )

        db.collection("noticias").add(noticiaEjemplo)
            .addOnSuccessListener {
                Toast.makeText(this, "Noticia de ejemplo creada", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al crear la noticia de ejemplo: ${e.message}", Toast.LENGTH_SHORT).show()
            }
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