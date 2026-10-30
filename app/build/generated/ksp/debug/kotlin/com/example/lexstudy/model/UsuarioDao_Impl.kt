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
import kotlin.collections.List
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class UsuarioDao_Impl(
  __db: RoomDatabase,
) : UsuarioDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfUsuario: EntityInsertAdapter<Usuario>
  init {
    this.__db = __db
    this.__insertAdapterOfUsuario = object : EntityInsertAdapter<Usuario>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `usuarios` (`id`,`nombre`,`correo`,`saltClave`,`hashClave`,`fotoPerfilPath`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Usuario) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.nombre)
        statement.bindText(3, entity.correo)
        statement.bindText(4, entity.saltClave)
        statement.bindText(5, entity.hashClave)
        val _tmpFotoPerfilPath: String? = entity.fotoPerfilPath
        if (_tmpFotoPerfilPath == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpFotoPerfilPath)
        }
      }
    }
  }

  public override suspend fun insertar(usuario: Usuario): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfUsuario.insertAndReturnId(_connection, usuario)
    _result
  }

  public override suspend fun buscarPorCorreo(correo: String): Usuario? {
    val _sql: String = "SELECT * FROM usuarios WHERE correo = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, correo)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfNombre: Int = getColumnIndexOrThrow(_stmt, "nombre")
        val _columnIndexOfCorreo: Int = getColumnIndexOrThrow(_stmt, "correo")
        val _columnIndexOfSaltClave: Int = getColumnIndexOrThrow(_stmt, "saltClave")
        val _columnIndexOfHashClave: Int = getColumnIndexOrThrow(_stmt, "hashClave")
        val _columnIndexOfFotoPerfilPath: Int = getColumnIndexOrThrow(_stmt, "fotoPerfilPath")
        val _result: Usuario?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpNombre: String
          _tmpNombre = _stmt.getText(_columnIndexOfNombre)
          val _tmpCorreo: String
          _tmpCorreo = _stmt.getText(_columnIndexOfCorreo)
          val _tmpSaltClave: String
          _tmpSaltClave = _stmt.getText(_columnIndexOfSaltClave)
          val _tmpHashClave: String
          _tmpHashClave = _stmt.getText(_columnIndexOfHashClave)
          val _tmpFotoPerfilPath: String?
          if (_stmt.isNull(_columnIndexOfFotoPerfilPath)) {
            _tmpFotoPerfilPath = null
          } else {
            _tmpFotoPerfilPath = _stmt.getText(_columnIndexOfFotoPerfilPath)
          }
          _result = Usuario(_tmpId,_tmpNombre,_tmpCorreo,_tmpSaltClave,_tmpHashClave,_tmpFotoPerfilPath)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun buscarPorId(usuarioId: Long): Usuario? {
    val _sql: String = "SELECT * FROM usuarios WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, usuarioId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfNombre: Int = getColumnIndexOrThrow(_stmt, "nombre")
        val _columnIndexOfCorreo: Int = getColumnIndexOrThrow(_stmt, "correo")
        val _columnIndexOfSaltClave: Int = getColumnIndexOrThrow(_stmt, "saltClave")
        val _columnIndexOfHashClave: Int = getColumnIndexOrThrow(_stmt, "hashClave")
        val _columnIndexOfFotoPerfilPath: Int = getColumnIndexOrThrow(_stmt, "fotoPerfilPath")
        val _result: Usuario?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpNombre: String
          _tmpNombre = _stmt.getText(_columnIndexOfNombre)
          val _tmpCorreo: String
          _tmpCorreo = _stmt.getText(_columnIndexOfCorreo)
          val _tmpSaltClave: String
          _tmpSaltClave = _stmt.getText(_columnIndexOfSaltClave)
          val _tmpHashClave: String
          _tmpHashClave = _stmt.getText(_columnIndexOfHashClave)
          val _tmpFotoPerfilPath: String?
          if (_stmt.isNull(_columnIndexOfFotoPerfilPath)) {
            _tmpFotoPerfilPath = null
          } else {
            _tmpFotoPerfilPath = _stmt.getText(_columnIndexOfFotoPerfilPath)
          }
          _result = Usuario(_tmpId,_tmpNombre,_tmpCorreo,_tmpSaltClave,_tmpHashClave,_tmpFotoPerfilPath)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun actualizarFotoPerfil(usuarioId: Long, rutaFoto: String) {
    val _sql: String = "UPDATE usuarios SET fotoPerfilPath = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, rutaFoto)
        _argIndex = 2
        _stmt.bindLong(_argIndex, usuarioId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun actualizarCredenciales(
    usuarioId: Long,
    saltClave: String,
    hashClave: String,
  ) {
    val _sql: String = "UPDATE usuarios SET saltClave = ?, hashClave = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, saltClave)
        _argIndex = 2
        _stmt.bindText(_argIndex, hashClave)
        _argIndex = 3
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
