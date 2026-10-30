package com.example.lexstudy.model

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
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class MapaConceptualDao_Impl(
  __db: RoomDatabase,
) : MapaConceptualDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfMapaConceptual: EntityInsertAdapter<MapaConceptual>
  init {
    this.__db = __db
    this.__insertAdapterOfMapaConceptual = object : EntityInsertAdapter<MapaConceptual>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `mapas_conceptuales` (`id`,`usuarioId`,`titulo`,`conceptoCentral`,`conceptoRelacionadoUno`,`conceptoRelacionadoDos`,`creadoEn`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: MapaConceptual) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.usuarioId)
        statement.bindText(3, entity.titulo)
        statement.bindText(4, entity.conceptoCentral)
        statement.bindText(5, entity.conceptoRelacionadoUno)
        statement.bindText(6, entity.conceptoRelacionadoDos)
        statement.bindLong(7, entity.creadoEn)
      }
    }
  }

  public override suspend fun insertar(mapa: MapaConceptual): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfMapaConceptual.insertAndReturnId(_connection, mapa)
    _result
  }

  public override fun observarPorUsuario(usuarioId: Long): Flow<List<MapaConceptual>> {
    val _sql: String = "SELECT * FROM mapas_conceptuales WHERE usuarioId = ? ORDER BY creadoEn DESC"
    return createFlow(__db, false, arrayOf("mapas_conceptuales")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, usuarioId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUsuarioId: Int = getColumnIndexOrThrow(_stmt, "usuarioId")
        val _columnIndexOfTitulo: Int = getColumnIndexOrThrow(_stmt, "titulo")
        val _columnIndexOfConceptoCentral: Int = getColumnIndexOrThrow(_stmt, "conceptoCentral")
        val _columnIndexOfConceptoRelacionadoUno: Int = getColumnIndexOrThrow(_stmt, "conceptoRelacionadoUno")
        val _columnIndexOfConceptoRelacionadoDos: Int = getColumnIndexOrThrow(_stmt, "conceptoRelacionadoDos")
        val _columnIndexOfCreadoEn: Int = getColumnIndexOrThrow(_stmt, "creadoEn")
        val _result: MutableList<MapaConceptual> = mutableListOf()
        while (_stmt.step()) {
          val _item: MapaConceptual
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpUsuarioId: Long
          _tmpUsuarioId = _stmt.getLong(_columnIndexOfUsuarioId)
          val _tmpTitulo: String
          _tmpTitulo = _stmt.getText(_columnIndexOfTitulo)
          val _tmpConceptoCentral: String
          _tmpConceptoCentral = _stmt.getText(_columnIndexOfConceptoCentral)
          val _tmpConceptoRelacionadoUno: String
          _tmpConceptoRelacionadoUno = _stmt.getText(_columnIndexOfConceptoRelacionadoUno)
          val _tmpConceptoRelacionadoDos: String
          _tmpConceptoRelacionadoDos = _stmt.getText(_columnIndexOfConceptoRelacionadoDos)
          val _tmpCreadoEn: Long
          _tmpCreadoEn = _stmt.getLong(_columnIndexOfCreadoEn)
          _item = MapaConceptual(_tmpId,_tmpUsuarioId,_tmpTitulo,_tmpConceptoCentral,_tmpConceptoRelacionadoUno,_tmpConceptoRelacionadoDos,_tmpCreadoEn)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun eliminar(mapaId: Long, usuarioId: Long) {
    val _sql: String = "DELETE FROM mapas_conceptuales WHERE id = ? AND usuarioId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, mapaId)
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
