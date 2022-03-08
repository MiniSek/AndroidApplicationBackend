package pl.edu.agh.transaction.premium;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.client.clientDao.ClientDao;
import pl.edu.agh.transaction.client.clientModels.Client;
import pl.edu.agh.transaction.client.clientModels.roles.ClientRole;
import pl.edu.agh.transaction.exception.IllegalDatabaseState;
import pl.edu.agh.transaction.exception.ObjectNotFoundException;
import pl.edu.agh.transaction.exception.PayPalClientException;
import pl.edu.agh.transaction.exception.PriceListLoadException;
import pl.edu.agh.transaction.invoice.invoiceDao.InvoiceDao;
import pl.edu.agh.transaction.invoice.invoiceModels.Invoice;
import pl.edu.agh.transaction.order.orderDao.PaymentOrderDao;
import pl.edu.agh.transaction.order.orderModels.PaymentOrder;
import pl.edu.agh.transaction.premium.paymentProvider.PaymentProvider;
import pl.edu.agh.transaction.priceList.PriceList;

import java.io.IOException;
import java.util.*;

@Service
public class PremiumService {
    private final ClientDao clientDao;
    private final PriceList priceList;
    private final InvoiceDao invoiceDao;
    private final PaymentOrderDao paymentOrderDao;

    private final PaymentProvider paymentProvider;

    @Autowired
    public PremiumService(ClientDao clientDao, PriceList priceList, InvoiceDao invoiceDao,
                          PaymentOrderDao paymentOrderDao, PaymentProvider paymentProvider) {
        this.clientDao = clientDao;
        this.priceList = priceList;
        this.invoiceDao = invoiceDao;
        this.paymentOrderDao = paymentOrderDao;
        this.paymentProvider = paymentProvider;
    }

    public ResponseEntity<String> buyPremium(String premiumId) {
        Client client;
        try {
            String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            client = clientDao.getClientByEmail(email);
        } catch(IllegalDatabaseState e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(ObjectNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        //get price and monthNumber
        int premiumMonthNumber;
        double premiumPrice;
        try {
            premiumMonthNumber = priceList.getMonthNumber(premiumId);
            premiumPrice = priceList.getPrice(premiumId);
        } catch(PriceListLoadException e) {
            return new ResponseEntity<>(String.format("Premium failure - %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>(String.format("Premium failure - %s", e.getMessage()), HttpStatus.FORBIDDEN);
        }

        try {
            String orderId = paymentProvider.createOrder(premiumPrice);
            String creationTime = paymentProvider.getCreationTime(orderId);
            paymentOrderDao.insert(
                    new PaymentOrder(orderId, premiumMonthNumber, premiumPrice, new DateTime(creationTime).plusDays(3)));
            String paymentLink = paymentProvider.getPaymentLink(orderId);

            return new ResponseEntity<>(paymentLink, HttpStatus.OK);
        } catch (IOException | PayPalClientException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Payment failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> approvePayment(String orderId) {
        String email;
        Client client;
        try {
            email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            client = clientDao.getClientByEmail(email);
        } catch(IllegalDatabaseState e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(ObjectNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        int premiumMonthNumber;
        double premiumPrice;
        try {
            PaymentOrder paymentOrder = paymentOrderDao.getOrder(orderId);
            premiumMonthNumber = paymentOrder.getPremiumMonthNumber();
            premiumPrice = paymentOrder.getPremiumPrice();
        } catch(IllegalDatabaseState e) {
            return new ResponseEntity<>(String.format("Premium failure - %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(ObjectNotFoundException e) {
            return new ResponseEntity<>(String.format("Premium failure - %s", e.getMessage()), HttpStatus.FORBIDDEN);
        }

        try {
            paymentProvider.captureOrder(orderId);
            paymentOrderDao.delete(orderId);
        } catch (IOException | PayPalClientException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Premium failure", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(IllegalDatabaseState e) {
            return new ResponseEntity<>(String.format("Premium failure - %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(ObjectNotFoundException e) {
            return new ResponseEntity<>(String.format("Premium failure - %s", e.getMessage()), HttpStatus.FORBIDDEN);
        }

        LocalDate todayDate = new LocalDate();
        LocalDate subscriptionStartDate, subscriptionEndDate, invoiceSubscriptionStartDate, invoiceSubscriptionEndDate;

        if(client.getSubscriptionEndDate() == null || todayDate.isAfter(client.getSubscriptionEndDate())) {
            subscriptionStartDate = todayDate;
            subscriptionEndDate = todayDate.plusMonths(premiumMonthNumber);

            invoiceSubscriptionStartDate = subscriptionStartDate;
            invoiceSubscriptionEndDate = subscriptionEndDate;
        } else {
            invoiceSubscriptionStartDate = client.getSubscriptionEndDate();
            invoiceSubscriptionEndDate = client.getSubscriptionEndDate().plusMonths(premiumMonthNumber);

            subscriptionStartDate = client.getSubscriptionStartDate();
            subscriptionEndDate = client.getSubscriptionEndDate().plusMonths(premiumMonthNumber);
        }

        Set<GrantedAuthority> roles = client.getRoles();
        roles.add(new SimpleGrantedAuthority(ClientRole.PREMIUM.name()));
        client.setRoles(roles);
        client.setSubscriptionStartDate(subscriptionStartDate);
        client.setSubscriptionEndDate(subscriptionEndDate);
        clientDao.update(email, client);
        invoiceDao.insert(new Invoice(client.getEmail(), client.getFirstName(), client.getLastName(), todayDate,
                premiumPrice, invoiceSubscriptionStartDate, invoiceSubscriptionEndDate));

        return new ResponseEntity<>("Premium bought successful", HttpStatus.OK);
    }
}