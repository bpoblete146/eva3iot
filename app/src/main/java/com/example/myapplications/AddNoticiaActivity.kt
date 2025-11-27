package com.example.myapplications

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNoticiaActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private var noticiaId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_noticia)

        db = FirebaseFirestore.getInstance()
        noticiaId = intent.getStringExtra("noticia_id")

        val etTitulo: EditText = findViewById(R.id.etTitulo)
        val etResumen: EditText = findViewById(R.id.etResumen)
        val etContenido: EditText = findViewById(R.id.etContenido)
        val etAutor: EditText = findViewById(R.id.etAutor)
        val btnGuardar: Button = findViewById(R.id.btnGuardar)

        if (noticiaId != null) {
            title = "Editar Noticia"
            btnGuardar.text = "Actualizar"
            db.collection("noticias").document(noticiaId!!).get().addOnSuccessListener { document ->
                if (document != null) {
                    val noticia = document.toObject(Noticia::class.java)
                    etTitulo.setText(noticia?.titulo)
                    etResumen.setText(noticia?.resumen)
                    etContenido.setText(noticia?.contenido)
                    etAutor.setText(noticia?.autor)
                }
            }
        } else {
            title = "Agregar Noticia"
            btnGuardar.text = "Guardar"
        }

        btnGuardar.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val resumen = etResumen.text.toString()
            val contenido = etContenido.text.toString()
            val autor = etAutor.text.toString()

            if (titulo.isNotEmpty() && resumen.isNotEmpty() && contenido.isNotEmpty() && autor.isNotEmpty()) {
                if (noticiaId != null) {
                    // Actualizar noticia existente
                    val noticiaActualizada = mapOf(
                        "titulo" to titulo,
                        "resumen" to resumen,
                        "contenido" to contenido,
                        "autor" to autor
                    )
                    db.collection("noticias").document(noticiaId!!).update(noticiaActualizada)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Noticia actualizada", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al actualizar: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Crear nueva noticia
                    val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                    val noticia = Noticia(null, titulo, resumen, contenido, autor, fecha)
                    db.collection("noticias").add(noticia)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Noticia guardada", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}