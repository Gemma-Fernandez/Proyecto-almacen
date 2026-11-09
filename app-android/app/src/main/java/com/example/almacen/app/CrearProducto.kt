package com.example.almacen.app

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrearProductoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_producto)

        val toolbar = findViewById<Toolbar>(R.id.toolbarCrear)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etSku = findViewById<EditText>(R.id.etSku)
        val etStock = findViewById<EditText>(R.id.etStock)
        val etPeso = findViewById<EditText>(R.id.etPeso)
        val cbSeguridad = findViewById<CheckBox>(R.id.cbSeguridad)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarProducto)

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val stockText = etStock.text.toString().trim()

            // Validación de los campos obligatorios
            if (nombre.isEmpty() || stockText.isEmpty()) {
                Toast.makeText(this, "Por favor completa el nombre y el stock", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sku = etSku.text.toString().trim().ifEmpty { null }
            val stock = stockText.toInt()
            val peso = etPeso.text.toString().toDoubleOrNull()
            val requiereSeguridad = cbSeguridad.isChecked

            val nuevoProducto = Producto(
                id = 0, // generará el ID automáticamente
                sku = sku,
                nombre = nombre,
                peso = peso,
                stock = stock,
                requiereSeguridad = requiereSeguridad
            )

            enviarProductoAPI(nuevoProducto)
        }
    }

    private fun enviarProductoAPI(producto: Producto) {
        RetrofitClient.apiService.crearProducto(producto).enqueue(object : Callback<Producto> {
            override fun onResponse(call: Call<Producto>, response: Response<Producto>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CrearProductoActivity, "¡Producto guardado con éxito!", Toast.LENGTH_SHORT).show()
                    finish() // Regresa a la lista principal
                } else {
                    Toast.makeText(this@CrearProductoActivity, "Error ${response.code()}: No se pudo guardar", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Producto>, t: Throwable) {
                Toast.makeText(this@CrearProductoActivity, "Error de red: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}