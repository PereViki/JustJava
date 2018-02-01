package com.example.android.justjava;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.NumberFormat;


/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    static final String STATE_QUANTITY = "quantity";
    EditText userNameET;
    String userName;
    CheckBox whippedCreamCB;
    Boolean hasWhippedCream;
    CheckBox chocolateCB;
    Boolean hasChocolate;
    int price;
    Intent mailIntent;
    CharSequence text;
    Toast toast;
    TextView quantityTextView;
    int quantity = 2;
    int priceCoffee = 5;
    int priceWhippedCream = 1;
    int priceChocolate = 2;
    int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameET = findViewById(R.id.user_name);
        whippedCreamCB = findViewById(R.id.has_whipped_cream);
        chocolateCB = findViewById(R.id.has_chocolate);
        quantityTextView = findViewById(R.id.quantity_text_view);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt(STATE_QUANTITY, quantity);
        super.onSaveInstanceState(savedInstanceState);

    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            quantity = savedInstanceState.getInt(STATE_QUANTITY);
            displayQuantity(quantity);
        }

    }

    /**
     * This method is called when the order button is clicked.
     * }
     */

    public void submitOrder(View view) {

        userName = userNameET.getText().toString();
        hasWhippedCream = whippedCreamCB.isChecked();
        hasChocolate = chocolateCB.isChecked();
        price = calculatePrice(hasWhippedCream, hasChocolate);

        mailIntent = new Intent(Intent.ACTION_SENDTO);
        mailIntent.setData(Uri.parse("mailto:pere.viktoria@gmail.com")); // only email apps should handle this
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_subject, userName));
        mailIntent.putExtra(Intent.EXTRA_TEXT, createOrderSummary(price, hasWhippedCream, hasChocolate, userName));
        if (mailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mailIntent);
        }
    }

    /**
     * Calculates the price of the order.
     *
     * @return the total price
     */

    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {

        if (addWhippedCream) {
            priceCoffee += priceWhippedCream;
        }

        if (addChocolate) {
            priceCoffee += priceChocolate;
        }

        return quantity * priceCoffee;
    }

    /**
     * Creates the order summary
     *
     * @param price           total amount
     * @param hasChocolate    does the user want chocolate?
     * @param hasWhippedCream does the user want whipped cream?
     * @return the text summary
     */

    private String createOrderSummary(int price, boolean hasWhippedCream, boolean hasChocolate, String userName) {

        return getString(R.string.order_name, userName) +
                "\n" + getString(R.string.order_whipped_cream, hasWhippedCream) +
                "\n" + getString(R.string.order_chocolate, hasChocolate) +
                "\n" + getString(R.string.order_quantity, quantity) +
                "\n" + getString(R.string.order_total, NumberFormat.getCurrencyInstance().format(price)) +
                "\n" + getString(R.string.thank_you);
    }

    /**
     * This method is called when the - button is clicked.
     * }
     */

    public void decrement(View view) {
        if (quantity > 1) {
            quantity = quantity - 1;
            displayQuantity(quantity);
        } else {
            text = "You have to order at least 1 coffee!";
            duration = Toast.LENGTH_SHORT;

            toast = Toast.makeText(this, text, duration);
            toast.show();
        }
    }

    /**
     * This method is called when the + button is clicked.
     * }
     */

    public void increment(View view) {
        if (quantity < 100) {
            quantity = quantity + 1;
            displayQuantity(quantity);
        } else {
            text = "You cannot order more than 100 coffees!";
            duration = Toast.LENGTH_SHORT;

            toast = Toast.makeText(this, text, duration);
            toast.show();
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */

    private void displayQuantity(int szam) {
        quantityTextView.setText((String.valueOf(szam)));
    }

}