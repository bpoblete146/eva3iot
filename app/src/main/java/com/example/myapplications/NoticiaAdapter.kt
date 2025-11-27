package com.example.myapplications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoticiaAdapter(private val noticias: List<Noticia>, private val onItemClick: (Noticia) -> Unit) : RecyclerView.Adapter<NoticiaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_noticia, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val noticia = noticias[position]
        holder.tvTitulo.text = noticia.titulo
        holder.tvResumen.text = noticia.resumen
        holder.itemView.setOnClickListener { onItemClick(noticia) }
    }

    override fun getItemCount() = noticias.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTitulo)
        val tvResumen: TextView = itemView.findViewById(R.id.tvResumen)
    }
}