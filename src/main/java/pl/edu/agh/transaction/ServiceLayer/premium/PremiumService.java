package pl.edu.agh.transaction.ServiceLayer.premium;

import com.paypal.orders.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.DataAccessLayer.clientDao.ClientDaoServiceLayer;
import pl.edu.agh.transaction.ServiceLayer.PriceList;
import pl.edu.agh.transaction.Utils.Model.Client;
import pl.edu.agh.transaction.Utils.Model.clientRole.ClientRole;
import pl.edu.agh.transaction.Utils.ServerException.IllegalDatabaseState;
import pl.edu.agh.transaction.Utils.ServerException.ObjectNotFoundException;
import pl.edu.agh.transaction.Utils.ServerException.PriceListLoadException;
import pl.edu.agh.transaction.DataAccessLayer.invoiceDao.InvoiceDao;
import pl.edu.agh.transaction.Utils.Model.Invoice;

import java.io.IOException;
import java.util.*;

import static pl.edu.agh.transaction.Utils.Model.clientRole.ClientRole.PREMIUM;
/*
@Service
public class PremiumService {
    private final ClientDaoServiceLayer clientDaoServiceLayer;
    private final PriceList priceList;
    private final InvoiceDao invoiceDao;
    private final PremiumTransaction premiumTransaction;

    @Autowired
    public PremiumService(ClientDaoServiceLayer clientDaoServiceLayer, PriceList priceList, InvoiceDao invoiceDao,
                          PremiumTransaction premiumTransaction) {
        this.clientDaoServiceLayer = clientDaoServiceLayer;
        this.priceList = priceList;
        this.invoiceDao = invoiceDao;
        this.premiumTransaction = premiumTransaction;
    }

    public ResponseEntity<String> buyPremium(String premiumId) {
        Client client;
        try {
            String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            client = clientDaoServiceLayer.getClientByEmail(email);
        } catch(IllegalDatabaseState e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(ObjectNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        //check whether client has this service already bought
        if(client.getRoles().contains(new SimpleGrantedAuthority(PREMIUM.name())))
            return new ResponseEntity<>("Premium failure - this client already has premium", HttpStatus.FORBIDDEN);

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
            String paymentLink = premiumTransaction.getPaymentLink(premiumMonthNumber, premiumPrice);
            return new ResponseEntity<>(paymentLink, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Payment failure", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> approvePayment(String orderId) {
        String email;
        Client client;
        try {
            email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            client = clientDaoServiceLayer.getClientByEmail(email);
        } catch(IllegalDatabaseState e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(ObjectNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        int premiumMonthNumber;
        double premiumPrice;
        try {
            premiumMonthNumber = premiumTransaction.getPremiumMonthNumber(orderId);
            premiumPrice = premiumTransaction.getPremiumPrice(orderId);
        } catch(IllegalDatabaseState e) {
            return new ResponseEntity<>(String.format("Premium failure - %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(ObjectNotFoundException e) {
            return new ResponseEntity<>(String.format("Premium failure - %s", e.getMessage()), HttpStatus.FORBIDDEN);
        }

        try {
            premiumTransaction.approvePayment(orderId);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Premium failure", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(IllegalDatabaseState e) {
            return new ResponseEntity<>(String.format("Premium failure - %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(ObjectNotFoundException e) {
            return new ResponseEntity<>(String.format("Premium failure - %s", e.getMessage()), HttpStatus.FORBIDDEN);
        }

        DateTime todayDate = new DateTime();
        DateTime subscriptionStartDate, subscriptionEndDate, invoiceSubscriptionStartDate, invoiceSubscriptionEndDate;

        if(client.getSubscriptionEndDate() == null || todayDate.isAfter(client.getSubscriptionEndDate())) {
            subscriptionStartDate = todayDate.withTime(0,0,0,0);
            subscriptionEndDate = todayDate.withTime(23,59,59,999).plusMonths(premiumMonthNumber);

            invoiceSubscriptionStartDate = subscriptionStartDate;
            invoiceSubscriptionEndDate = subscriptionEndDate;
        } else {
            subscriptionStartDate = client.getSubscriptionStartDate();
            subscriptionEndDate = client.getSubscriptionEndDate().plusMonths(premiumMonthNumber);

            invoiceSubscriptionStartDate = client.getSubscriptionEndDate().plusDays(1).withTime(0,0,0,0);
            invoiceSubscriptionEndDate = subscriptionEndDate;
        }

        Set<GrantedAuthority> roles = client.getRoles();
        roles.add(new SimpleGrantedAuthority(ClientRole.PREMIUM.name()));
        client.setRoles(roles);
        client.setSubscriptionStartDate(subscriptionStartDate);
        client.setSubscriptionEndDate(subscriptionEndDate);
        clientDaoServiceLayer.update(email, client);

        invoiceDao.addInvoice(new Invoice(client.getEmail(), client.getFirstName(), client.getLastName(), todayDate,
                premiumPrice, invoiceSubscriptionStartDate, invoiceSubscriptionEndDate));

        return new ResponseEntity<>("Premium bought successful", HttpStatus.OK);
    }
}
*/
