
package ir.erfansn.siliconecalculator.data.source.local.db.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ir.erfansn.siliconecalculator.data.source.local.db.SiliconeCalculatorDatabase
import ir.erfansn.siliconecalculator.data.source.local.db.model.HistoryEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class HistoryDaoTest {

    private lateinit var database: SiliconeCalculatorDatabase
    private lateinit var historyDao: HistoryDao

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            SiliconeCalculatorDatabase::class.java
        ).build()

        historyDao = database.historyDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun oneEntity_whenInserts_retrievesCorrectly() = runTest {
        val historyEntity = HistoryEntity(
            id = 1,
            expression = "1 + 0",
            result = "1"
        )

        historyDao.insertHistoryEntity(historyEntity)

        assertEquals(historyDao.getHistoryEntitiesStream().first(), listOf(historyEntity))
    }

    @Test
    fun autoGenerateIdEntities_whenInsertsAndDeletes_worksCorrectly() = runTest {
        val historyEntities = testHistoryEntities

        historyEntities.forEach { historyDao.insertHistoryEntity(it) }
        assertEquals(historyDao.getHistoryEntitiesStream().first().size, historyEntities.size)

        historyDao.deleteAllHistoryEntities()
        assertEquals(historyDao.getHistoryEntitiesStream().first(), emptyList<HistoryEntity>())
    }
}

private val testHistoryEntities = listOf(
    HistoryEntity(
        expression = "0 + 0",
        result = "0"
    ),
    HistoryEntity(
        expression = "1 + 0",
        result = "1"
    ),
    HistoryEntity(
        expression = "0 + 1",
        result = "1"
    ),
    HistoryEntity(
        expression = "1 + 1",
        result = "2"
    )
)
