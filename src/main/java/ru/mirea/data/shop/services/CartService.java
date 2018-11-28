package ru.mirea.data.shop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mirea.data.shop.entities.CartItem;
import ru.mirea.data.shop.entities.Currency;
import ru.mirea.data.shop.entities.Item;
import ru.mirea.data.shop.repository.BalanceRepository;
import ru.mirea.data.shop.repository.CartItemRepository;
import ru.mirea.data.shop.repository.CurrencyRepository;
import ru.mirea.data.shop.repository.ItemRepository;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    public List<CartItem> getCart(){
        return cartItemRepository.getAllCartItems();
    }

    public String deleteItemFromCart(int id){
        int err = cartItemRepository.deleteFromCartById(id);
        if (err == 0){
            return "Error: stuff wasn't found in cart";
        } else {
            return "The stuff was successfully removed from cart";
        }
    }

    public String putItemToCart(int id){
        Item item = itemRepository.getItemById(id);
        if (item == null) {
            return "Error: id wasn't found";
        }
        int nCountInItems = item.getCount();
        if (nCountInItems == 0) {
            return "The stuffs are over";
        }
        int nCountInCart = cartItemRepository.getItemsCountInCart(id);
        if (nCountInCart < nCountInItems){
            if (cartItemRepository.insertIntoCart(id) == -1) {
                return "Error: connection problems";
            }
        } else {
            return "The stuffs are over";
        }
        return "Stuff was been added to cart";
    }

    public String paymentOfCart(){
        List<CartItem> cartItems = cartItemRepository.getDistinctCartItems();
        int paymentAmount = 0;
        if (cartItems.isEmpty()){
            return "Error: cart is empty";
        }
        for (CartItem cartItem : cartItems) {
            Item item = itemRepository.getItemById(cartItem.getIdItem());
            int nCountInStuffs = item.getCount();
            int nCountInCart = cartItemRepository.getItemsCountInCart(cartItem.getIdItem());
            if (nCountInCart > nCountInStuffs) {
                return "Error: the quantity of the items has been changed.";
            }
            System.out.println(item.getPrice());
            paymentAmount += nCountInCart * item.getPrice();
        }

        double balance = 0;
        List<Currency> currencies = currencyRepository.getAllCurrencies();
        for (Currency currency : currencies){
            balance += balanceRepository.getBalanceByCurrencyId(currency.getId()).getBalance() * currency.getExchangeRate();
        }

        System.out.println(paymentAmount + " " + balance);
        if (balance < paymentAmount){
            return "You don't have enough money";
        }

        for (Currency currency : currencies) {
            double exchangeRate = currency.getExchangeRate();
            double currencyBalance = balanceRepository.getBalanceByCurrencyId(currency.getId()).getBalance() * exchangeRate;
            if (currencyBalance >= paymentAmount) {
                balanceRepository.updateBalanceByCurrencyID(currency.getId(),
                        (currencyBalance-paymentAmount)/exchangeRate);
                break;
            } else {
                balanceRepository.updateBalanceByCurrencyID(currency.getId(), 0);
                paymentAmount -= currencyBalance;
            }
        }

        for (CartItem cartItem : cartItems) {
            Item item = itemRepository.getItemById(cartItem.getIdItem());
            int nCountInItems = item.getCount();
            int nCountInCart = cartItemRepository.getItemsCountInCart(cartItem.getIdItem());
            if (nCountInCart <= nCountInItems) {
                item.setCount(nCountInItems - nCountInCart);
                if (itemRepository.updateItem(item) == -1) {
                    return "Error: connection problem";
                }
            }
        }
        cartItemRepository.clearCart();
        return "The payment has been successfully completed";
    }
}
