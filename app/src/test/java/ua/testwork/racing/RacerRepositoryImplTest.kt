package ua.testwork.racing

import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ua.testwork.racing.data.local.dao.RacersDao
import ua.testwork.racing.data.local.entity.RacerEntity
import ua.testwork.racing.data.repository.RacersRepositoryImpl

class RacersRepositoryImplTest {

    private lateinit var repository: RacersRepositoryImpl
    private val racersDao: RacersDao = mockk()

    @Before
    fun setUp() {
        repository = RacersRepositoryImpl(racersDao)
    }

    @Test
    fun `getRandomRacersByCount returns racers when enough in DB`() = runTest {
        val count = 3
        val racersInDb = listOf(
            RacerEntity(racerId = 0, "Racer1", 2),
            RacerEntity(racerId = 1, "Racer2", 5),
            RacerEntity(racerId = 2, "Racer3", 1)
        )
        coEvery { racersDao.getRacerCount() } returns racersInDb.size
        coEvery { racersDao.getRandomRacers(count) } returns racersInDb

        val result = repository.getRandomRacersByCount(count)

        assertEquals(3, result.size)
        assertEquals("Racer1", result[0].name)
        assertEquals("Racer2", result[1].name)
        assertEquals("Racer3", result[2].name)
    }

    @Test
    fun `getRandomRacersByCount creates new racers when not enough in DB`() = runTest {
        val existingCount = 1
        val requestedCount = 3
        coEvery { racersDao.getRacerCount() } returns existingCount
        coEvery { racersDao.updateRacers(any()) } just Runs
        coEvery { racersDao.getRandomRacers(requestedCount) } returns listOf(
            RacerEntity(0, "Racer0", 1),
            RacerEntity(1, "Racer1", 0),
            RacerEntity(2, "Racer2", 0)
        )

        val result = repository.getRandomRacersByCount(requestedCount)

        coVerify { racersDao.updateRacers(withArg { assertEquals(2, it.size) }) }
        assertEquals(3, result.size)
    }

    @Test
    fun `updateRacersAfterRace calls DAO to increment scores`() = runTest {
        val racerIds = listOf(1, 2, 3)
        coEvery { racersDao.incrementScores(racerIds) } just Runs

        repository.updateRacersAfterRace(racerIds)

        coVerify { racersDao.incrementScores(racerIds) }
    }

    @Test
    fun `getAllRacersStatistic returns flow from DAO`() = runTest {
        val racers = listOf(
            RacerEntity(racerId = 0, "Racer1", 3),
            RacerEntity(racerId = 1, "Racer2", 1)
        )
        every { racersDao.getAllRacers() } returns flowOf(racers)

        val resultFlow = repository.getAllRacersStatistic()
        val resultList = resultFlow.first()

        assertEquals(2, resultList.size)
        assertEquals("Racer1", resultList.get(0).name)
    }
}