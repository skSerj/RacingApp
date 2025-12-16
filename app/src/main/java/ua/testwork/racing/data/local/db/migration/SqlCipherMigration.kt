package ua.testwork.racing.data.local.db.migration

import android.content.Context
import java.io.File
import androidx.core.content.edit
import net.zetetic.database.sqlcipher.SQLiteDatabase

object SqlCipherMigration {

    /**
     * Миграция plaintext SQLite в зашифрованную SQLCipher DB.
     *
     * @param context - контекст приложения
     * @param dbName - имя вашей базы данных (например "racing.db")
     * @param passphrase - ключ для SQLCipher
     */
    fun migrate(context: Context, dbName: String, passphrase: ByteArray) {
        val dbFile = context.getDatabasePath(dbName)
        if (!dbFile.exists()) return

        val tempEncrypted = File(dbFile.parent, "temp_encrypted.db")
        if (tempEncrypted.exists()) tempEncrypted.delete()

        // 1️⃣ Открываем существующую plaintext DB (ключ пустой)
        // Используем доступный метод openOrCreateDatabase
        val plainDb = SQLiteDatabase.openOrCreateDatabase(dbFile, null) // CursorFactory = null

        // 2️⃣ Присоединяем новую зашифрованную DB через ATTACH
        plainDb.execSQL(
            "ATTACH DATABASE '${tempEncrypted.absolutePath}' AS encrypted KEY '${String(passphrase)}'"
        )

        // 3️⃣ Экспортируем все данные из main -> encrypted
        plainDb.rawQuery("SELECT sqlcipher_export('encrypted')", null).use {}

        // 4️⃣ Отсоединяем зашифрованную DB
        plainDb.execSQL("DETACH DATABASE encrypted")
        plainDb.close()

        // 5️⃣ Заменяем старый файл новым зашифрованным
        if (!dbFile.delete()) throw IllegalStateException("Не удалось удалить старую базу")
        if (!tempEncrypted.renameTo(dbFile)) throw IllegalStateException("Не удалось переименовать зашифрованную базу")
    }
}