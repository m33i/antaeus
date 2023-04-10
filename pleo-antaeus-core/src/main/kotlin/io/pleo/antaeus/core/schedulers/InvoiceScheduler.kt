package io.pleo.antaeus.core.schedulers

import kotlinx.coroutines.runBlocking
import dev.inmo.krontab.builder.buildSchedule
import dev.inmo.krontab.doInfinity

// Simple cron routine for each 1 of the month
// Made with Krontab, see docs here https://github.com/InsanusMokrassar/krontab

// In case it changes to another day
private const val NUM_MONTH_DAY = 1

object InvoiceScheduler {
    fun main(value: () -> Unit) = runBlocking {
        val routine = buildSchedule {
            dayOfMonth {
                each(NUM_MONTH_DAY)
            }
        }
        routine.doInfinity { value() }
    }
}
