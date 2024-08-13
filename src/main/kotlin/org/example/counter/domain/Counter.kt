package org.example.counter.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Version

@Entity
class Counter(
    @Id val id: Long,
    var count: Long = 0,
    @Version var version: Long = 1,
) {
    fun increaseCount() {
        this.count += 1
    }
}
