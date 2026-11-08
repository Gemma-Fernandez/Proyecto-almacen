package com.example.almacen.app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private val productosList = mutableListOf<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Inicializar el RecyclerView y su LayoutManager
        recyclerView = findViewById(R.id.recyclerProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 2. Asignar un adaptador vacío desde el inicio
        adapter = ProductoAdapter(productosList) { productoSeleccionado ->
            val intent = Intent(this@MainActivity, ProductoDetailActivity::class.java).apply {
                putExtra("EXTRA_PRODUCTO", productoSeleccionado)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        // 3. Cargar los datos desde Retrofit
        cargarProductos()
    }

    private fun cargarProductos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiService = RetrofitClient.apiService
                val nuevosProductos = apiService.getProductos()

                withContext(Dispatchers.Main) {
                    Log.d("API_TEST", "Productos recibidos: ${nuevosProductos.size}")
                    productosList.clear()
                    productosList.addAll(nuevosProductos)
                    adapter.notifyDataSetChanged()

                    val adapter = ProductoAdapter(productosList) { productoSeleccionado ->
                        val intent = Intent(this@MainActivity, ProductoDetailActivity::class.java).apply {
                            putExtra("EXTRA_PRODUCTO", productoSeleccionado)
                        }
                        startActivity(intent)
                    }
                    recyclerView.adapter = adapter
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("API_TEST", "Error al cargar: ${e.localizedMessage}")
                }
            }
        }
    }
}