package com.example.lexstudy.repository

import android.content.Context
import android.graphics.Bitmap
import com.example.lexstudy.model.Usuario
import com.example.lexstudy.model.UsuarioDao
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Guarda la fotografía en el almacenamiento privado y su ruta en Room. */
class FotoPerfilRepository(
    context: Context,
    private val usuarioDao: UsuarioDao
) {
    private val appContext = context.applicationContext

    suspend fun guardarFotoPerfil(usuarioId: Long, bitmap: Bitmap): Usuario? =
        withContext(Dispatchers.IO) {
            val directorio = File(appContext.filesDir, "fotos_perfil")
            if (!directorio.exists() && !directorio.mkdirs()) {
                throw IOException("No se pudo crear el directorio de fotos de perfil.")
            }

            val usuarioAnterior = usuarioDao.buscarPorId(usuarioId) ?: return@withContext null
            // El nombre cambia en cada captura para que la interfaz vuelva a cargar la imagen.
            val archivoFoto = File(directorio, "alumno_${usuarioId}_${System.currentTimeMillis()}.jpg")
            FileOutputStream(archivoFoto).use { salida ->
                val guardada = bitmap.compress(Bitmap.CompressFormat.JPEG, 90, salida)
                if (!guardada) throw IOException("No se pudo guardar la fotografía.")
            }

            usuarioDao.actualizarFotoPerfil(usuarioId, archivoFoto.absolutePath)
            val usuarioActualizado = usuarioDao.buscarPorId(usuarioId)

            // Elimina la fotografía anterior solo si estaba dentro de la carpeta privada de perfiles.
            runCatching {
                val rutaAnterior = usuarioAnterior.fotoPerfilPath
                if (!rutaAnterior.isNullOrBlank()) {
                    val archivoAnterior = File(rutaAnterior)
                    if (archivoAnterior.parentFile?.canonicalPath == directorio.canonicalPath) {
                        archivoAnterior.delete()
                    }
                }
            }
            usuarioActualizado
        }
}
