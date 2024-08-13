package org.example.counter.domain

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CounterService(
    private val counterRepository: CounterRepository,
) {

    @Transactional
    fun count(id: Long) {
        val counter = counterRepository.findById(id).orElseThrow()
        counter.increaseCount()
    }

    fun getLock(id: Long) {
        counterRepository.getLock(id.toString())
    }

    fun releaseLock(id: Long) {
        counterRepository.releaseLock(id.toString())
    }

    @Transactional
    fun countByJPQL(id: Long) {
        counterRepository.updateCount(id)
    }

    @Transactional
    fun countByPessimisticLock(id: Long) {
        val counter = counterRepository.findOneWithPessimisticLock(id) ?: throw EntityNotFoundException()
        counter.increaseCount()
    }

    @Transactional
    fun countByOptimisticLock(id: Long) {
        val counter = counterRepository.findOneWithOptimisticLock(id) ?: throw EntityNotFoundException()
        counter.increaseCount()
    }
}
