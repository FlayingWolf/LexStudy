package com.example.lexstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.lexstudy.model.AppDatabase
import com.example.lexstudy.repository.FotoPerfilRepository
import com.example.lexstudy.repository.LexStudyRepository
import com.example.lexstudy.repository.SesionRepository
import com.example.lexstudy.ui.LexStudyApp
import com.example.lexstudy.ui.theme.LexStudyTheme

class MainActivity : ComponentActivity() {
    private val baseDeDatos: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "lexstudy.db"
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }

    private val repositorioSesion: SesionRepository by lazy {
        SesionRepository(baseDeDatos.sesionDao())
    }

    private val repositorioFotoPerfil: FotoPerfilRepository by lazy {
        FotoPerfilRepository(applicationContext, baseDeDatos.usuarioDao())
    }

    private val repositorio: LexStudyRepository by lazy {
        LexStudyRepository(
            usuarioDao = baseDeDatos.usuarioDao(),
            apunteDao = baseDeDatos.apunteDao(),
            mapaDao = baseDeDatos.mapaConceptualDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LexStudyTheme {
                LexStudyApp(
                    repository = repositorio,
                    sesionRepository = repositorioSesion,
                    fotoPerfilRepository = repositorioFotoPerfil
                )
            }
        }
    }
    private companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `sesion_activa` (" +
                        "`id` INTEGER NOT NULL, " +
                        "`usuarioId` INTEGER NOT NULL, " +
                        "PRIMARY KEY(`id`))"
                )
            }
        }

        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE `usuarios` ADD COLUMN `fotoPerfilPath` TEXT")
            }
        }
    }
}
