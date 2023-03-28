package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus

class BillingService(
    private val paymentProvider: PaymentProvider,
    private val invoiceService: InvoiceService
) {
    // TODO - Add code e.g. here

    // Billing all unpaid invoices
    fun monthlyBilling() {
        val unpaid = invoiceService.fetchUnpaid()

        unpaid.forEach {
            if (paymentProvider.charge(it)) {
                invoiceService.setAsPaid(it.id)
            }
        }
    }

    // Billing only one specific invoice
    fun simpleBilling(id: Int): Invoice {

        if (invoiceService.fetch(id).status == InvoiceStatus.PENDING) {
            invoiceService.setAsPaid(id)
        }

        return invoiceService.fetch(id)
    }


}
