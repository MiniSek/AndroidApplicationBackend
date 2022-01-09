package pl.edu.agh.transaction.premium;

import com.paypal.http.HttpResponse;
import com.paypal.http.exceptions.HttpException;
import com.paypal.orders.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.agh.transaction.client.clientDao.ClientDaoServiceLayer;
import pl.edu.agh.transaction.client.clientModels.Client;
import pl.edu.agh.transaction.client.clientModels.roles.ClientRole;
import pl.edu.agh.transaction.exception.IllegalDatabaseState;
import pl.edu.agh.transaction.exception.ObjectNotFoundException;
import pl.edu.agh.transaction.exception.PriceListLoadException;
import pl.edu.agh.transaction.invoice.invoiceDao.InvoiceDao;
import pl.edu.agh.transaction.invoice.invoiceModels.Invoice;
import pl.edu.agh.transaction.order.orderDao.OrderDaoServiceLayer;
import pl.edu.agh.transaction.premium.paypal.Credentials;
import pl.edu.agh.transaction.priceList.PriceList;

import java.io.IOException;
import java.util.*;

import static pl.edu.agh.transaction.client.clientModels.roles.ClientRole.PREMIUM;

@Service
public class BuyPremiumService {
    private final ClientDaoServiceLayer clientDaoServiceLayer;
    private final OrderDaoServiceLayer orderDaoServiceLayer;
    private final PriceList priceList;
    private final InvoiceDao invoiceDao;

    @Autowired
    public BuyPremiumService(ClientDaoServiceLayer clientDaoServiceLayer, OrderDaoServiceLayer orderDaoServiceLayer, PriceList priceList, InvoiceDao invoiceDao) {
        this.clientDaoServiceLayer = clientDaoServiceLayer;
        this.orderDaoServiceLayer = orderDaoServiceLayer;
        this.priceList = priceList;
        this.invoiceDao = invoiceDao;
    }

    public ResponseEntity<String> buyPremium(String monthNumber) {
        try {
            String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Client client = clientDaoServiceLayer.getClientByEmail(email);

            if(client.getRoles().contains(new SimpleGrantedAuthority(PREMIUM.name())))
                return new ResponseEntity<>("Premium failure - this client already has premium", HttpStatus.FORBIDDEN);

            // Construct a request object and set desired parameters
            // Here, OrdersCreateRequest() creates a POST request to /v2/checkout/orders
            Order order = null;
            double price;
            try {
                price = priceList.getPriceForMonthNumber(Integer.parseInt(monthNumber));
            } catch(PriceListLoadException e) {
                return new ResponseEntity<>(String.format("Premium failure - %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            } catch(IllegalArgumentException e) {
                return new ResponseEntity<>(String.format("Premium failure - %s", e.getMessage()), HttpStatus.FORBIDDEN);
            }

            OrderRequest orderRequest = new OrderRequest();
            orderRequest.checkoutPaymentIntent("CAPTURE");
            List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
            purchaseUnits.add(
                    new PurchaseUnitRequest().amountWithBreakdown(
                            new AmountWithBreakdown().currencyCode("PLN").value(String.valueOf(price))));
            orderRequest.purchaseUnits(purchaseUnits);

            OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);

            // Call API with your client and get a response for your call
            HttpResponse<Order> response = Credentials.client.execute(request);

            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            order = response.result();

            //TODO fix passing now date
            orderDaoServiceLayer.addOrder(order.id(), price, Integer.parseInt(monthNumber), new DateTime());

            //System.out.println("Order ID: " + order.id());
            //order.links().forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));

            return new ResponseEntity<>(order.links().get(1).href(), HttpStatus.OK);
        } catch (IOException | IllegalDatabaseState ioe) {
            if (ioe instanceof HttpException || ioe instanceof IllegalDatabaseState) {
                HttpException he = (HttpException) ioe;
                System.out.println(he.getMessage());
                he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
                return new ResponseEntity<>("Premium failure - server internal failure", HttpStatus.CONFLICT);
            } else
                return new ResponseEntity<>("Premium failure - something went wrong client-side", HttpStatus.FORBIDDEN);
        } catch(ObjectNotFoundException e) {
            return new ResponseEntity<>("Premium failure - client not found", HttpStatus.NOT_FOUND);
        } catch(IllegalArgumentException e) {
            return new ResponseEntity<>("Premium failure - wrong month number", HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<String> captureOrder(String orderID) {
        try {
            String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            int monthNumber = orderDaoServiceLayer.getOrder(orderID).getMonthNumber();
            double price = orderDaoServiceLayer.getOrder(orderID).getPrice();
            orderDaoServiceLayer.delete(orderID);

            Client client = clientDaoServiceLayer.getClientByEmail(email);
            Order order = null;
            OrdersCaptureRequest request = new OrdersCaptureRequest(orderID);

            // Call API with your client and get a response for your call
            //HttpResponse<Order> response = Credentials.client.execute(request);

            // If call returns body in response, you can get the de-serialized version by
            // calling result() on the response
            //order = response.result();
            //System.out.println("Capture ID: " + order.purchaseUnits().get(0).payments().captures().get(0).id());
            //order.purchaseUnits().get(0).payments().captures().get(0).links().forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));

            DateTime todayDate = new DateTime();
            DateTime subscriptionStartDate, subscriptionEndDate, invoiceSubscriptionStartDate, invoiceSubscriptionEndDate;

            if(client.getSubscriptionEndDate() == null || todayDate.isAfter(client.getSubscriptionEndDate())) {
                subscriptionStartDate = todayDate.withTime(0,0,0,0);
                subscriptionEndDate = todayDate.withTime(23,59,59,999).plusMonths(monthNumber);

                invoiceSubscriptionStartDate = subscriptionStartDate;
                invoiceSubscriptionEndDate = subscriptionEndDate;
            } else {
                subscriptionStartDate = client.getSubscriptionStartDate();
                subscriptionEndDate = client.getSubscriptionEndDate().plusMonths(monthNumber);

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
                    price, invoiceSubscriptionStartDate, invoiceSubscriptionEndDate));

            return new ResponseEntity<>("Premium bought successful", HttpStatus.OK);
        } /*catch (IOException ioe) {
            if (ioe instanceof HttpException) {
                // Something went wrong server-side
                HttpException he = (HttpException) ioe;
                System.out.println(he.getMessage());
                he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
            } else {
                // Something went wrong client-side
            }
        } */catch(IllegalDatabaseState e) {
            return new ResponseEntity<>("Premium failure - server internal failure", HttpStatus.CONFLICT);
        } catch(ObjectNotFoundException e) {
            return new ResponseEntity<>("Premium failure - client not found", HttpStatus.NOT_FOUND);
        }
    }
}
