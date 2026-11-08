package com.example.almacen.app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Llamada de prueba a WireMock
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiService = RetrofitClient.apiService
                // 1. llamada a la API para obtener lista de productos
                val productos = apiService.getProductos()

                // 2.mostramos en el Logcat desde el hilo principal
                withContext(Dispatchers.Main) {
                    Log.d("API_TEST", "Productos obtenidos: $productos")
                }
            } catch (e: Exception) {
                // 3. Si hay error, capturamos y mostramos el detalle
                withContext(Dispatchers.Main) {
                    Log.e("API_TEST", "Error detallado: ${e.javaClass.simpleName} - ${e.localizedMessage}")
                }
            }
        }
    }
}