package org.example.counter.application

import org.example.counter.domain.CounterService
import org.redisson.api.RedissonClient
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.TimeUnit

@Service
class CounterCountFacade(
    private val counterService: CounterService,
    private val redisTemplate: RedisTemplate<String, String>,
    private val redissonClient: RedissonClient,
) {

    fun count(id: Long) {
        counterService.count(id)
    }

    @Synchronized
    fun countSynchronized(id: Long) {
        counterService.count(id)
    }

    fun countByPessimisticLock(id: Long) {
        counterService.countByPessimisticLock(id)
    }

    fun countByJPQL(id: Long) {
        counterService.countByJPQL(id)
    }

    fun countByOptimisticLock(id: Long) {
        while (true) {
            try {
                counterService.countByOptimisticLock(id)
                break
            } catch (e: OptimisticLockingFailureException) {
            }
        }
    }

    fun countingByNamedLock(id: Long) {
        try {
            counterService.getLock(id)
            counterService.count(id)
        } finally {
            counterService.releaseLock(id)
        }
    }

    fun countingByRedisLock(id: Long) {
        while (
            !redisTemplate.opsForValue()
                .setIfAbsent("lock:$id", "lock", Duration.ofMillis(3_000))!!
        ) {
        }

        counterService.count(id)
        redisTemplate.delete("lock:$id")
    }

    fun countingByRedisson(id: Long) {
        val lock = redissonClient.getLock("lock:$id")
        try {
            lock.lock(10, TimeUnit.SECONDS)
            counterService.count(id)
        } finally {
            lock.unlock()
        }
    }
}
