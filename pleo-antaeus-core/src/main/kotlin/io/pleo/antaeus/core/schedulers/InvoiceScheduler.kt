package io.pleo.antaeus.core.schedulers

import dev.inmo.krontab.builder.buildSchedule
import dev.inmo.krontab.doInfinity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// Simple coroutine for each 1 of the month
// Made with Krontab, see docs here https://github.com/InsanusMokrassar/krontab

// In case it changes to another day
// Testing: put the current day (-1) and you will se all invoices 'PAID'
// Use endpoint localhost:7000/rest/v1/pending to check if still appear 'PENDING' invoices

private const val NUM_MONTH_DAY = 0

object InvoiceScheduler {
     fun main(value: () -> Unit) {
        val routine = buildSchedule {
            // https://insanusmokrassar.github.io/krontab/kdocs/dev.inmo.krontab.builder/-scheduler-builder/index.html
            dayOfMonth {
                each(NUM_MONTH_DAY)
            }
        }
        GlobalScope.launch {
            routine.doInfinity { value() }
        }
    }
}

