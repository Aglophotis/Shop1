package ru.mirea.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mirea.data.dao.BalanceDao;
import ru.mirea.data.dao.CartItemDao;
import ru.mirea.data.dao.CurrencyDao;
import ru.mirea.data.dao.ItemDao;
import ru.mirea.data.entities.CartItem;
import ru.mirea.data.entities.Currency;
import ru.mirea.data.entities.Item;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemDao cartItemDao;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private BalanceDao balanceDao;

    @Autowired
    private CurrencyDao currencyDao;

    public List<CartItem> getCart(){
        return cartItemDao.getAllCartItems();
    }

    public String deleteItemFromCart(int id){
        int err = cartItemDao.deleteFromCartById(id);
        if (err == 0){
            return "Error: stuff wasn't found in cart";
        } else {
            return "The stuff was successfully removed from cart";
        }
    }

    public String putItemToCart(int id){
        Item item = itemDao.getItemById(id);
        if (item == null) {
            return "Error: id wasn't found";
        }
        int nCountInItems = item.getCount();
        if (nCountInItems == 0) {
            return "The stuffs are over";
        }
        int nCountInCart = cartItemDao.getItemsCountInCart(id);
        if (nCountInCart < nCountInItems){
            if (cartItemDao.insertIntoCart(id) == -1) {
                return "Error: connection problems";
            }
        } else {
            return "The stuffs are over";
        }
        return "Stuff was been added to cart";
    }

    public String paymentOfCart(){
        List<CartItem> cartItems = cartItemDao.getDistinctCartItems();
        int paymentAmount = 0;
        if (cartItems.isEmpty()){
            return "Error: cart is empty";
        }
        for (CartItem cartItem : cartItems) {
            Item item = itemDao.getItemById(cartItem.getIdItem());
            int nCountInStuffs = item.getCount();
            int nCountInCart = cartItemDao.getItemsCountInCart(cartItem.getIdItem());
            if (nCountInCart > nCountInStuffs) {
                return "Error: the quantity of the items has been changed.";
            }
            System.out.println(item.getPrice());
            paymentAmount += nCountInCart * item.getPrice();
        }

        double balance = 0;
        List<Currency> currencies = currencyDao.getAllCurrencies();
        for (Currency currency : currencies){
            balance += balanceDao.getBalanceByCurrencyId(currency.getId()).getBalance() * currency.getExchangeRate();
        }

        System.out.println(paymentAmount + " " + balance);
        if (balance < paymentAmount){
            return "You don't have enough money";
        }

        for (Currency currency : currencies) {
            double exchangeRate = currency.getExchangeRate();
            double currencyBalance = balanceDao.getBalanceByCurrencyId(currency.getId()).getBalance() * exchangeRate;
            if (currencyBalance >= paymentAmount) {
                balanceDao.updateBalanceByCurrencyID(currency.getId(),
                        (currencyBalance-paymentAmount)/exchangeRate);
                break;
            } else {
                balanceDao.updateBalanceByCurrencyID(currency.getId(), 0);
                paymentAmount -= currencyBalance;
            }
        }

        for (CartItem cartItem : cartItems) {
            Item item = itemDao.getItemById(cartItem.getIdItem());
            int nCountInItems = item.getCount();
            int nCountInCart = cartItemDao.getItemsCountInCart(cartItem.getIdItem());
            if (nCountInCart <= nCountInItems) {
                item.setCount(nCountInItems - nCountInCart);
                if (itemDao.updateItem(item) == -1){
                    return "Error: connection problem";
                }
            }
        }
        cartItemDao.clearCart();
        return "The payment has been successfully completed";
    }
}
