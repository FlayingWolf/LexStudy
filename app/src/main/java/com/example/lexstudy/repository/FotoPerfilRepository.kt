package com.example.lexstudy.repository

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Guarda la foto en el almacenamiento privado y actualiza la ruta en el repositorio en memoria. */
class FotoPerfilRepository(
    context: Context,
    private val repository: LexStudyRepository
) {
    private val appContext = context.applicationContext

    suspend fun guardarFotoPerfil(usuarioId: Long, bitmap: Bitmap) = withContext(Dispatchers.IO) {
        val directorio = File(appContext.filesDir, "fotos_perfil")
        if (!directorio.exists() && !directorio.mkdirs()) {
            throw IOException("No se pudo crear el directorio de fotos de perfil.")
        }
        val usuarioAnterior = repository.obtenerUsuarioPorId(usuarioId) ?: return@withContext null
        val archivoFoto = File(directorio, "alumno_${usuarioId}_${System.currentTimeMillis()}.jpg")
        FileOutputStream(archivoFoto).use { salida ->
            val guardada = bitmap.compress(Bitmap.CompressFormat.JPEG, 90, salida)
            if (!guardada) throw IOException("No se pudo guardar la fotografía.")
        }
        val actualizado = repository.actualizarFotoPerfilPath(usuarioId, archivoFoto.absolutePath)
        runCatching {
            val rutaAnterior = usuarioAnterior.fotoPerfilPath
            if (!rutaAnterior.isNullOrBlank()) {
                val archivoAnterior = File(rutaAnterior)
                if (archivoAnterior.parentFile?.canonicalPath == directorio.canonicalPath) archivoAnterior.delete()
            }
        }
        actualizado
    }
}
