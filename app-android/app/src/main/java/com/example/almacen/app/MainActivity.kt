package com.example.almacen.app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private val productosList = mutableListOf<Producto>()

    private val crearProductoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val nuevoProducto = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                data?.getSerializableExtra("NUEVO_PRODUCTO", Producto::class.java)
            } else {
                @Suppress("DEPRECATION")
                data?.getSerializableExtra("NUEVO_PRODUCTO") as? Producto
            }

            nuevoProducto?.let { producto ->
                productosList.add(producto)
                adapter.notifyItemInserted(productosList.size - 1)
                recyclerView.smoothScrollToPosition(productosList.size - 1)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProductoAdapter(productosList) { producto ->
            val intent = Intent(this, ProductoDetailActivity::class.java).apply {
                putExtra("EXTRA_PRODUCTO", producto)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        // Cargar productos de la API
        cargarProductos()

        val fabAgregar = findViewById<FloatingActionButton>(R.id.fabAgregarProducto)
        fabAgregar.setOnClickListener {
            val intent = Intent(this, CrearProductoActivity::class.java)
            crearProductoLauncher.launch(intent)
        }
    }

    private fun cargarProductos() {
        RetrofitClient.apiService.getProductos().enqueue(object : Callback<List<Producto>> {
            override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                if (response.isSuccessful && response.body() != null) {
                    productosList.clear()
                    productosList.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(applicationContext, "API Error (${response.code()}). Cargando datos locales.", Toast.LENGTH_SHORT).show()
                    cargarDatosInicialesFallback()
                }
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                Toast.makeText(applicationContext, "Sin conexión a API. Cargando datos locales.", Toast.LENGTH_SHORT).show()
                cargarDatosInicialesFallback()
            }
        })
    }

    private fun cargarDatosInicialesFallback() {
        if (productosList.isEmpty()) {
            productosList.add(
                Producto(
                    id = 1,
                    sku = "GARMIN245X",
                    nombre = "Reloj Garmin Forerunner 245",
                    peso = 0.5,
                    stock = 15,
                    requiereSeguridad = false
                )
            )
            adapter.notifyDataSetChanged()
        }
    }
}