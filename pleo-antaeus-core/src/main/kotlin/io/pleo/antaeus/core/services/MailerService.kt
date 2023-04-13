package io.pleo.antaeus.core.services

import io.pleo.antaeus.models.Invoice
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.axay.simplekotlinmail.delivery.send
import net.axay.simplekotlinmail.email.emailBuilder
import java.time.LocalDate

// MOCK of an email service using SimpleKotlinMail
// https://jakobkmar.github.io/SimpleKotlinMail/

// Test here https://www.wpoven.com/tools/free-smtp-server-for-testing
// Use "invoicetestmail@pleo.io" to see the confirmation email when charging an invoice
class MailerService() {

    private var inv: Invoice? = null
    private var email = emailBuilder{}
    fun setInvoiceDataToMail(invoice: Invoice) {
        inv = invoice

        // Mail construction example
        email = emailBuilder {
            from("invoices@pleo.io")
            to("invoicetestmail@pleo.io")
            withSubject("Invoice ${inv?.id} - Monthly billing ${LocalDate.now()}")
            withHTMLText("Dear client, as of ${LocalDate.now()} your invoice with number ${inv?.id} " +
                        "has been charged with the amount of ${inv?.amount?.value} ${inv?.amount?.currency} " +
                        "and it is now marked as PAID. " +
                        "If you believe that we have charged you incorrectly , please contact us at invoices@pleo.io")
            withPlainText("Dear client, as of ${LocalDate.now()} your invoice with number ${inv?.id} " +
                    "has been charged with the amount of ${inv?.amount?.value} ${inv?.amount?.currency} " +
                    "and it is now marked as PAID. " +
                    "If you believe that we have charged you incorrectly , please contact us at invoices@pleo.io")
        }
    }

    fun sendConfirmationEmail(ready: Boolean) {
        if (ready) {
            GlobalScope.launch { email.send() }
        }
    }
}