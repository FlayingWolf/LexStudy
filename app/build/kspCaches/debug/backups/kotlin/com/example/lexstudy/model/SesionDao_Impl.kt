package com.example.lexstudy.model

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
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
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class SesionDao_Impl(
  __db: RoomDatabase,
) : SesionDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfSesion: EntityInsertAdapter<Sesion>
  init {
    this.__db = __db
    this.__insertAdapterOfSesion = object : EntityInsertAdapter<Sesion>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `sesion_activa` (`id`,`usuarioId`) VALUES (?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Sesion) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindLong(2, entity.usuarioId)
      }
    }
  }

  public override suspend fun guardarSesion(sesion: Sesion): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfSesion.insert(_connection, sesion)
  }

  public override suspend fun obtenerSesionActiva(): Sesion? {
    val _sql: String = "SELECT * FROM sesion_activa WHERE id = 1 LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUsuarioId: Int = getColumnIndexOrThrow(_stmt, "usuarioId")
        val _result: Sesion?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpUsuarioId: Long
          _tmpUsuarioId = _stmt.getLong(_columnIndexOfUsuarioId)
          _result = Sesion(_tmpId,_tmpUsuarioId)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun limpiarSesion() {
    val _sql: String = "DELETE FROM sesion_activa"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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
