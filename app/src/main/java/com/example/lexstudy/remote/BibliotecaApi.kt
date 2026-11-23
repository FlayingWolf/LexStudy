package com.example.lexstudy.remote

import com.example.lexstudy.BuildConfig
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
    val api: BibliotecaApi by lazy {
        check(BuildConfig.API_URL.isNotBlank()) { "Configura LEXSTUDY_API_URL en gradle.properties y recompila." }
        Retrofit.Builder().baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create(BibliotecaApi::class.java)
    }
}
