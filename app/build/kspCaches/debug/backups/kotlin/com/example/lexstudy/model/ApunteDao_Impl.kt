package com.example.lexstudy.model

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ApunteDao_Impl(
  __db: RoomDatabase,
) : ApunteDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfApunte: EntityInsertAdapter<Apunte>

  private val __updateAdapterOfApunte: EntityDeleteOrUpdateAdapter<Apunte>
  init {
    this.__db = __db
    this.__insertAdapterOfApunte = object : EntityInsertAdapter<Apunte>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `apuntes` (`id`,`usuarioId`,`leyId`,`nombreLey`,`titulo`,`contenido`,`archivoPdfUri`,`actualizadoEn`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Apunte) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.usuarioId)
        statement.bindText(3, entity.leyId)
        statement.bindText(4, entity.nombreLey)
        statement.bindText(5, entity.titulo)
        statement.bindText(6, entity.contenido)
        val _tmpArchivoPdfUri: String? = entity.archivoPdfUri
        if (_tmpArchivoPdfUri == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpArchivoPdfUri)
        }
        statement.bindLong(8, entity.actualizadoEn)
      }
    }
    this.__updateAdapterOfApunte = object : EntityDeleteOrUpdateAdapter<Apunte>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `apuntes` SET `id` = ?,`usuarioId` = ?,`leyId` = ?,`nombreLey` = ?,`titulo` = ?,`contenido` = ?,`archivoPdfUri` = ?,`actualizadoEn` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Apunte) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.usuarioId)
        statement.bindText(3, entity.leyId)
        statement.bindText(4, entity.nombreLey)
        statement.bindText(5, entity.titulo)
        statement.bindText(6, entity.contenido)
        val _tmpArchivoPdfUri: String? = entity.archivoPdfUri
        if (_tmpArchivoPdfUri == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpArchivoPdfUri)
        }
        statement.bindLong(8, entity.actualizadoEn)
        statement.bindLong(9, entity.id)
      }
    }
  }

  public override suspend fun insertar(apunte: Apunte): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfApunte.insertAndReturnId(_connection, apunte)
    _result
  }

  public override suspend fun actualizar(apunte: Apunte): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfApunte.handle(_connection, apunte)
  }

  public override fun observarPorUsuario(usuarioId: Long): Flow<List<Apunte>> {
    val _sql: String = "SELECT * FROM apuntes WHERE usuarioId = ? ORDER BY actualizadoEn DESC"
    return createFlow(__db, false, arrayOf("apuntes")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, usuarioId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUsuarioId: Int = getColumnIndexOrThrow(_stmt, "usuarioId")
        val _columnIndexOfLeyId: Int = getColumnIndexOrThrow(_stmt, "leyId")
        val _columnIndexOfNombreLey: Int = getColumnIndexOrThrow(_stmt, "nombreLey")
        val _columnIndexOfTitulo: Int = getColumnIndexOrThrow(_stmt, "titulo")
        val _columnIndexOfContenido: Int = getColumnIndexOrThrow(_stmt, "contenido")
        val _columnIndexOfArchivoPdfUri: Int = getColumnIndexOrThrow(_stmt, "archivoPdfUri")
        val _columnIndexOfActualizadoEn: Int = getColumnIndexOrThrow(_stmt, "actualizadoEn")
        val _result: MutableList<Apunte> = mutableListOf()
        while (_stmt.step()) {
          val _item: Apunte
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpUsuarioId: Long
          _tmpUsuarioId = _stmt.getLong(_columnIndexOfUsuarioId)
          val _tmpLeyId: String
          _tmpLeyId = _stmt.getText(_columnIndexOfLeyId)
          val _tmpNombreLey: String
          _tmpNombreLey = _stmt.getText(_columnIndexOfNombreLey)
          val _tmpTitulo: String
          _tmpTitulo = _stmt.getText(_columnIndexOfTitulo)
          val _tmpContenido: String
          _tmpContenido = _stmt.getText(_columnIndexOfContenido)
          val _tmpArchivoPdfUri: String?
          if (_stmt.isNull(_columnIndexOfArchivoPdfUri)) {
            _tmpArchivoPdfUri = null
          } else {
            _tmpArchivoPdfUri = _stmt.getText(_columnIndexOfArchivoPdfUri)
          }
          val _tmpActualizadoEn: Long
          _tmpActualizadoEn = _stmt.getLong(_columnIndexOfActualizadoEn)
          _item = Apunte(_tmpId,_tmpUsuarioId,_tmpLeyId,_tmpNombreLey,_tmpTitulo,_tmpContenido,_tmpArchivoPdfUri,_tmpActualizadoEn)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun eliminar(apunteId: Long, usuarioId: Long) {
    val _sql: String = "DELETE FROM apuntes WHERE id = ? AND usuarioId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, apunteId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, usuarioId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
