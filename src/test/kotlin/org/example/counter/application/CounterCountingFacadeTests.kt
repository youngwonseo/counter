package org.example.counter.application

import org.example.counter.domain.Counter
import org.example.counter.domain.CounterRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@SpringBootTest
class CounterCountingFacadeTests(
    @Autowired private val counterCountFacade: CounterCountFacade,
    @Autowired private val counterRepository: CounterRepository,
) {

    val counterId = 1L

    @BeforeEach
    fun before() {
        counterRepository.save(Counter(id = counterId))
    }

    @AfterEach
    fun after() {
        counterRepository.deleteAll()
    }

    @Test
    fun counting() {
        val threadCount = 100
        val executor = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        for (i in 0 until threadCount) {
            executor.submit {
                try {
                    counterCountFacade.count(counterId)
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await()
        val counter = counterRepository.findById(counterId).orElseThrow()
        assertNotEquals(100, counter.count)
    }


    @Test
    fun countingUsingJPQL() {
        val threadCount = 100
        val executor = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        for (i in 0 until threadCount) {
            executor.submit {
                try {
                    counterCountFacade.countByJPQL(counterId)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        val counter = counterRepository.findById(counterId).orElseThrow()
        assertEquals(100, counter.count)
    }

    @Test
    fun countingUsingPessimisticLock() {
        val threadCount = 100
        val executor = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        for (i in 0 until threadCount) {
            executor.submit {
                try {
                    counterCountFacade.countByPessimisticLock(counterId)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        val counter = counterRepository.findById(counterId).orElseThrow()
        assertEquals(100, counter.count)
    }

    @Test
    fun countingUsingNamedLock() {
        val threadCount = 100
        val executor = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        for (i in 0 until threadCount) {
            executor.submit {
                try {
                    counterCountFacade.countingByNamedLock(counterId)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        val counter = counterRepository.findById(counterId).orElseThrow()
        assertEquals(100, counter.count)
    }

    @Test
    fun countingUsingOptimisticLock() {
        val threadCount = 100
        val executor = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        for (i in 0 until threadCount) {
            executor.submit {
                try {
                    counterCountFacade.countByOptimisticLock(counterId)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        val counter = counterRepository.findById(counterId).orElseThrow()
        assertEquals(100, counter.count)
    }

    @Test
    fun countingSynchronized() {
        val threadCount = 100
        val executor = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        for (i in 0 until threadCount) {
            executor.submit {
                try {
                    counterCountFacade.countSynchronized(counterId)
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await()
        val counter = counterRepository.findById(counterId).orElseThrow()
        assertEquals(100, counter.count)
    }

    @Test
    fun countingUsingRedisLock() {
        val threadCount = 100
        val executor = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        for (i in 0 until threadCount) {
            executor.submit {
                try {
                    counterCountFacade.countingByRedisLock(counterId)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        val counter = counterRepository.findById(counterId).orElseThrow()
        assertEquals(100, counter.count)
    }


    @Test
    fun countingUsingRedisson() {
        val threadCount = 100
        val executor = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        for (i in 0 until threadCount) {
            executor.submit {
                try {
                    counterCountFacade.countingByRedisson(counterId)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        val counter = counterRepository.findById(counterId).orElseThrow()
        assertEquals(100, counter.count)
    }

}
