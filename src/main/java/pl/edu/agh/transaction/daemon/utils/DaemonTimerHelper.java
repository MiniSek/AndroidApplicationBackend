package pl.edu.agh.transaction.daemon.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class DaemonTimerHelper {
    private final int orderCleanerHourOfDay;
    private final int orderCleanerMinute;
    private final int orderCleanerSecond;
    private final int orderCleanerMillisecond;

    private final int paymentReminderHourOfDay;
    private final int paymentReminderMinute;
    private final int paymentReminderSecond;
    private final int paymentReminderMillisecond;

    private final int invoiceSendingHourOfDay;
    private final int invoiceSendingMinute;
    private final int invoiceSendingSecond;
    private final int invoiceSendingMillisecond;

    private final int clientRoleCleanerHourOfDay;
    private final int clientRoleCleanerMinute;
    private final int clientRoleCleanerSecond;
    private final int clientRoleCleanerMillisecond;

    @Autowired
    public DaemonTimerHelper(@Value("${ORDER_CLEANER_HOUR_OF_DAY}") Integer ORDER_CLEANER_HOUR_OF_DAY,
                             @Value("${ORDER_CLEANER_MINUTE}") Integer ORDER_CLEANER_MINUTE,
                             @Value("${ORDER_CLEANER_SECOND}") Integer ORDER_CLEANER_SECOND,
                             @Value("${ORDER_CLEANER_MILLISECOND}") Integer ORDER_CLEANER_MILLISECOND,

                             @Value("${PAYMENT_REMINDER_HOUR_OF_DAY}") Integer PAYMENT_REMINDER_HOUR_OF_DAY,
                             @Value("${PAYMENT_REMINDER_MINUTE}") Integer PAYMENT_REMINDER_MINUTE,
                             @Value("${PAYMENT_REMINDER_SECOND}") Integer PAYMENT_REMINDER_SECOND,
                             @Value("${PAYMENT_REMINDER_MILLISECOND}") Integer PAYMENT_REMINDER_MILLISECOND,

                             @Value("${INVOICE_SENDING_HOUR_OF_DAY}") Integer INVOICE_SENDING_HOUR_OF_DAY,
                             @Value("${INVOICE_SENDING_MINUTE}") Integer INVOICE_SENDING_MINUTE,
                             @Value("${INVOICE_SENDING_SECOND}") Integer INVOICE_SENDING_SECOND,
                             @Value("${INVOICE_SENDING_MILLISECOND}") Integer INVOICE_SENDING_MILLISECOND,

                             @Value("${CLIENT_CLEANER_HOUR_OF_DAY}") Integer CLIENT_CLEANER_HOUR_OF_DAY,
                             @Value("${CLIENT_CLEANER_MINUTE}") Integer CLIENT_CLEANER_MINUTE,
                             @Value("${CLIENT_CLEANER_SECOND}") Integer CLIENT_CLEANER_SECOND,
                             @Value("${CLIENT_CLEANER_MILLISECOND}") Integer CLIENT_CLEANER_MILLISECOND) {

        orderCleanerHourOfDay = ORDER_CLEANER_HOUR_OF_DAY;
        orderCleanerMinute = ORDER_CLEANER_MINUTE;
        orderCleanerSecond = ORDER_CLEANER_SECOND;
        orderCleanerMillisecond = ORDER_CLEANER_MILLISECOND;

        paymentReminderHourOfDay = PAYMENT_REMINDER_HOUR_OF_DAY;
        paymentReminderMinute = PAYMENT_REMINDER_MINUTE;
        paymentReminderSecond = PAYMENT_REMINDER_SECOND;
        paymentReminderMillisecond = PAYMENT_REMINDER_MILLISECOND;

        invoiceSendingHourOfDay = INVOICE_SENDING_HOUR_OF_DAY;
        invoiceSendingMinute = INVOICE_SENDING_MINUTE;
        invoiceSendingSecond = INVOICE_SENDING_SECOND;
        invoiceSendingMillisecond = INVOICE_SENDING_MILLISECOND;

        clientRoleCleanerHourOfDay = CLIENT_CLEANER_HOUR_OF_DAY;
        clientRoleCleanerMinute = CLIENT_CLEANER_MINUTE;
        clientRoleCleanerSecond = CLIENT_CLEANER_SECOND;
        clientRoleCleanerMillisecond = CLIENT_CLEANER_MILLISECOND;
    }

    public void setOrderCleanerTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, orderCleanerHourOfDay);
        calendar.set(Calendar.MINUTE, orderCleanerMinute);
        calendar.set(Calendar.SECOND, orderCleanerSecond);
        calendar.set(Calendar.MILLISECOND, orderCleanerMillisecond);
    }

    public void setPaymentReminderTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, paymentReminderHourOfDay);
        calendar.set(Calendar.MINUTE, paymentReminderMinute);
        calendar.set(Calendar.SECOND, paymentReminderSecond);
        calendar.set(Calendar.MILLISECOND, paymentReminderMillisecond);
    }

    public void setInvoiceSendingTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, invoiceSendingHourOfDay);
        calendar.set(Calendar.MINUTE, invoiceSendingMinute);
        calendar.set(Calendar.SECOND, invoiceSendingSecond);
        calendar.set(Calendar.MILLISECOND, invoiceSendingMillisecond);
    }

    public void setClientRoleCleanerTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, clientRoleCleanerHourOfDay);
        calendar.set(Calendar.MINUTE, clientRoleCleanerMinute);
        calendar.set(Calendar.SECOND, clientRoleCleanerSecond);
        calendar.set(Calendar.MILLISECOND, clientRoleCleanerMillisecond);
    }
}
