package com.example.almacen.app

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable

class ProductoDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto_details)

        // Botón atrás en la barra superior
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val producto = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("EXTRA_PRODUCTO", Producto::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("EXTRA_PRODUCTO") as? Producto
        }

        producto?.let { p ->
            title = p.nombre

            findViewById<TextView>(R.id.tvDetailNombre).text = p.nombre
            findViewById<TextView>(R.id.tvDetailSku).text = "SKU: ${p.sku ?: "N/A"}"
            findViewById<TextView>(R.id.tvDetailStock).text = "Stock disponible: ${p.stock} uds."
            findViewById<TextView>(R.id.tvDetailPeso).text = "Peso unitario: ${p.peso ?: 0.0} kg"

            val esSeguro = if (p.requiereSeguridad == true) "Sí (Ubicación de seguridad)" else "No"
            findViewById<TextView>(R.id.tvDetailSeguridad).text = "Ubicación especial: $esSeguro"
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}