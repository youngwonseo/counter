package org.example.counter.domain

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface CounterRepository : JpaRepository<Counter, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Counter c WHERE c.id = :id")
    fun findOneWithPessimisticLock(id: Long): Counter?

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT c FROM Counter c WHERE c.id = :id")
    fun findOneWithOptimisticLock(id: Long): Counter?

    @Modifying
    @Query("UPDATE Counter c SET c.count = c.count + 1 WHERE c.id = :id")
    fun updateCount(id: Long): Int

    @Query(value = "SELECT get_lock(:key, 1000)", nativeQuery = true)
    fun getLock(key: String)

    @Query(value = "SELECT release_lock(:key)", nativeQuery = true)
    fun releaseLock(key: String)
}
