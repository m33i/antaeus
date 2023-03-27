package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.external.PaymentProvider

class BillingService(
    private val paymentProvider: PaymentProvider,
    private val invoiceService: InvoiceService
) {
    // TODO - Add code e.g. here

    // fetch unpaid invoices
    // foreach invoice i => i.contains(status == 'PENDING') (invoiceService?)
    // PaymentProvider has charge bool
    // charge customer
    // create scheduler to execute the monthlyBilling each 1st

    fun monthlyBilling() {
        val unpaids = invoiceService.fetchUnpaid()

        unpaids.forEach {
            if (paymentProvider.charge(it)) {
                invoiceService.setAsPaid(it.id)
            }
        }
    }


}
