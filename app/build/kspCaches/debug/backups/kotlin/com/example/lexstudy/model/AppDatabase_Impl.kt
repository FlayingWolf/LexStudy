package com.example.lexstudy.model

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _usuarioDao: Lazy<UsuarioDao> = lazy {
    UsuarioDao_Impl(this)
  }

  private val _apunteDao: Lazy<ApunteDao> = lazy {
    ApunteDao_Impl(this)
  }

  private val _mapaConceptualDao: Lazy<MapaConceptualDao> = lazy {
    MapaConceptualDao_Impl(this)
  }

  private val _sesionDao: Lazy<SesionDao> = lazy {
    SesionDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(3, "9443d72eea1c435c7bde14f6fc87129b", "aae1e288dabf04068d6d2fcea270b132") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `usuarios` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombre` TEXT NOT NULL, `correo` TEXT NOT NULL, `saltClave` TEXT NOT NULL, `hashClave` TEXT NOT NULL, `fotoPerfilPath` TEXT)")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_usuarios_correo` ON `usuarios` (`correo`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `apuntes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `usuarioId` INTEGER NOT NULL, `leyId` TEXT NOT NULL, `nombreLey` TEXT NOT NULL, `titulo` TEXT NOT NULL, `contenido` TEXT NOT NULL, `archivoPdfUri` TEXT, `actualizadoEn` INTEGER NOT NULL, FOREIGN KEY(`usuarioId`) REFERENCES `usuarios`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_apuntes_usuarioId` ON `apuntes` (`usuarioId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `mapas_conceptuales` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `usuarioId` INTEGER NOT NULL, `titulo` TEXT NOT NULL, `conceptoCentral` TEXT NOT NULL, `conceptoRelacionadoUno` TEXT NOT NULL, `conceptoRelacionadoDos` TEXT NOT NULL, `creadoEn` INTEGER NOT NULL, FOREIGN KEY(`usuarioId`) REFERENCES `usuarios`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_mapas_conceptuales_usuarioId` ON `mapas_conceptuales` (`usuarioId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `sesion_activa` (`id` INTEGER NOT NULL, `usuarioId` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '9443d72eea1c435c7bde14f6fc87129b')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `usuarios`")
        connection.execSQL("DROP TABLE IF EXISTS `apuntes`")
        connection.execSQL("DROP TABLE IF EXISTS `mapas_conceptuales`")
        connection.execSQL("DROP TABLE IF EXISTS `sesion_activa`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsUsuarios: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUsuarios.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsuarios.put("nombre", TableInfo.Column("nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsuarios.put("correo", TableInfo.Column("correo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsuarios.put("saltClave", TableInfo.Column("saltClave", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsuarios.put("hashClave", TableInfo.Column("hashClave", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsuarios.put("fotoPerfilPath", TableInfo.Column("fotoPerfilPath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUsuarios: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUsuarios: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesUsuarios.add(TableInfo.Index("index_usuarios_correo", true, listOf("correo"), listOf("ASC")))
        val _infoUsuarios: TableInfo = TableInfo("usuarios", _columnsUsuarios, _foreignKeysUsuarios, _indicesUsuarios)
        val _existingUsuarios: TableInfo = read(connection, "usuarios")
        if (!_infoUsuarios.equals(_existingUsuarios)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |usuarios(com.example.lexstudy.model.Usuario).
              | Expected:
              |""".trimMargin() + _infoUsuarios + """
              |
              | Found:
              |""".trimMargin() + _existingUsuarios)
        }
        val _columnsApuntes: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsApuntes.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsApuntes.put("usuarioId", TableInfo.Column("usuarioId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsApuntes.put("leyId", TableInfo.Column("leyId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsApuntes.put("nombreLey", TableInfo.Column("nombreLey", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsApuntes.put("titulo", TableInfo.Column("titulo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsApuntes.put("contenido", TableInfo.Column("contenido", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsApuntes.put("archivoPdfUri", TableInfo.Column("archivoPdfUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsApuntes.put("actualizadoEn", TableInfo.Column("actualizadoEn", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysApuntes: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysApuntes.add(TableInfo.ForeignKey("usuarios", "CASCADE", "NO ACTION", listOf("usuarioId"), listOf("id")))
        val _indicesApuntes: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesApuntes.add(TableInfo.Index("index_apuntes_usuarioId", false, listOf("usuarioId"), listOf("ASC")))
        val _infoApuntes: TableInfo = TableInfo("apuntes", _columnsApuntes, _foreignKeysApuntes, _indicesApuntes)
        val _existingApuntes: TableInfo = read(connection, "apuntes")
        if (!_infoApuntes.equals(_existingApuntes)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |apuntes(com.example.lexstudy.model.Apunte).
              | Expected:
              |""".trimMargin() + _infoApuntes + """
              |
              | Found:
              |""".trimMargin() + _existingApuntes)
        }
        val _columnsMapasConceptuales: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsMapasConceptuales.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMapasConceptuales.put("usuarioId", TableInfo.Column("usuarioId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMapasConceptuales.put("titulo", TableInfo.Column("titulo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMapasConceptuales.put("conceptoCentral", TableInfo.Column("conceptoCentral", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMapasConceptuales.put("conceptoRelacionadoUno", TableInfo.Column("conceptoRelacionadoUno", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMapasConceptuales.put("conceptoRelacionadoDos", TableInfo.Column("conceptoRelacionadoDos", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMapasConceptuales.put("creadoEn", TableInfo.Column("creadoEn", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysMapasConceptuales: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysMapasConceptuales.add(TableInfo.ForeignKey("usuarios", "CASCADE", "NO ACTION", listOf("usuarioId"), listOf("id")))
        val _indicesMapasConceptuales: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesMapasConceptuales.add(TableInfo.Index("index_mapas_conceptuales_usuarioId", false, listOf("usuarioId"), listOf("ASC")))
        val _infoMapasConceptuales: TableInfo = TableInfo("mapas_conceptuales", _columnsMapasConceptuales, _foreignKeysMapasConceptuales, _indicesMapasConceptuales)
        val _existingMapasConceptuales: TableInfo = read(connection, "mapas_conceptuales")
        if (!_infoMapasConceptuales.equals(_existingMapasConceptuales)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |mapas_conceptuales(com.example.lexstudy.model.MapaConceptual).
              | Expected:
              |""".trimMargin() + _infoMapasConceptuales + """
              |
              | Found:
              |""".trimMargin() + _existingMapasConceptuales)
        }
        val _columnsSesionActiva: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSesionActiva.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSesionActiva.put("usuarioId", TableInfo.Column("usuarioId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSesionActiva: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesSesionActiva: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoSesionActiva: TableInfo = TableInfo("sesion_activa", _columnsSesionActiva, _foreignKeysSesionActiva, _indicesSesionActiva)
        val _existingSesionActiva: TableInfo = read(connection, "sesion_activa")
        if (!_infoSesionActiva.equals(_existingSesionActiva)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |sesion_activa(com.example.lexstudy.model.Sesion).
              | Expected:
              |""".trimMargin() + _infoSesionActiva + """
              |
              | Found:
              |""".trimMargin() + _existingSesionActiva)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "usuarios", "apuntes", "mapas_conceptuales", "sesion_activa")
  }

  public override fun clearAllTables() {
    super.performClear(true, "usuarios", "apuntes", "mapas_conceptuales", "sesion_activa")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(UsuarioDao::class, UsuarioDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ApunteDao::class, ApunteDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(MapaConceptualDao::class, MapaConceptualDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(SesionDao::class, SesionDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun usuarioDao(): UsuarioDao = _usuarioDao.value

  public override fun apunteDao(): ApunteDao = _apunteDao.value

  public override fun mapaConceptualDao(): MapaConceptualDao = _mapaConceptualDao.value

  public override fun sesionDao(): SesionDao = _sesionDao.value
}
