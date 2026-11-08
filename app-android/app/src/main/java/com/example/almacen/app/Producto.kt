package com.example.almacen.app

import com.google.gson.annotations.SerializedName

data class Producto(
    @SerializedName("idProducto")
    val id: Long,

    @SerializedName("nombreArticulo")
    val nombre: String,

    @SerializedName("stockDisponible")
    val stock: Int
)