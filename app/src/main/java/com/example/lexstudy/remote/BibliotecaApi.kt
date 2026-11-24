package com.example.lexstudy.remote

import java.net.URI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class Ficha(val titulo: String, val detalle: String)
data class Sugerencia(val texto: String)
data class Recibo(val id: Int)

interface BibliotecaApi {
    @GET("glosario") suspend fun glosario(): List<Ficha>
    @GET("materias") suspend fun materias(): List<Ficha>
    @GET("fuentes") suspend fun fuentes(): List<Ficha>
    @GET("ejercicios") suspend fun ejercicios(): List<Ficha>
    @GET("tecnicas") suspend fun tecnicas(): List<Ficha>
    @GET("preguntas") suspend fun preguntas(): List<Ficha>
    @POST("sugerencias") suspend fun sugerir(@Body datos: Sugerencia): Recibo
}

object BibliotecaCliente {
    fun crear(url: String): BibliotecaApi {
        val base = url.trim().ifEmpty { "http://127.0.0.1:8765/" }
        val uri = URI(base)
        require(base == "http://127.0.0.1:8765/" || (uri.scheme == "https" &&
            uri.host != null && uri.userInfo == null && uri.query == null && uri.fragment == null &&
            !uri.path.contains("workspace:"))) { "Introduce la URL HTTPS del grupo de API." }
        return Retrofit.Builder().baseUrl(base.trimEnd('/') + "/")
            .addConverterFactory(GsonConverterFactory.create()).build().create(BibliotecaApi::class.java)
    }
}
