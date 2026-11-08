package com.example.almacen.app

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Producto(
    @SerializedName("idProducto")
    val id: Long,

    @SerializedName("sku")
    val sku: String? = null,

    @SerializedName("nombreArticulo")
    val nombre: String,

    @SerializedName("pesoUnitarioKg")
    val peso: Double? = null,

    @SerializedName("stockDisponible")
    val stock: Int,

    @SerializedName("requiereUbicacionSeguridad")
    val requiereSeguridad: Boolean? = false,

    @SerializedName("fechaAlta")
    val fechaAlta: String? = null
) : Parcelable