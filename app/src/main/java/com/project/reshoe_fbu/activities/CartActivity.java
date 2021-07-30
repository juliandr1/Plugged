package com.project.reshoe_fbu.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.ActivityCartBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.project.reshoe_fbu.adapters.CartAdapter;
import com.project.reshoe_fbu.helper.PaymentsUtil;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartActivity extends AppCompatActivity {

    public static String TAG = "CartActivity";

    private List<Post> cartItems;
    private CartAdapter adapter;
    private ActivityCartBinding binding;
    private View googlePayButton;
    private int totalPrice;

    // Arbitrarily-picked constant integer you define to track a request for payment data activity.
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

    // A client for interacting with the Google Pay API.
    private PaymentsClient paymentsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Context context = this;

        cartItems = new ArrayList<>();
        RecyclerView rvItems = binding.rvItems;
        adapter = new CartAdapter(this, cartItems, binding);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvItems.setLayoutManager(layoutManager);
        rvItems.setAdapter(adapter);

        paymentsClient = PaymentsUtil.createPaymentsClient(this);
        possiblyShowGooglePayButton();

        binding.btnBackCart.setOnClickListener(v -> finish());

        try {
            queryCartItems();
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        googlePayButton = findViewById(R.id.googleCheckout);
        googlePayButton.setOnClickListener(v -> requestPayment(view));
    }

    private void queryCartItems() throws JSONException, ParseException {
        User currentUser = new User(ParseUser.getCurrentUser());
        cartItems.addAll(currentUser.getCart());
        adapter.notifyDataSetChanged();
        totalPrice = getCartTotal();

        if (cartItems.size() != 0) {
            binding.tvTotalCart.setText(getString(R.string.money) + totalPrice);
        } else {
            binding.tvEmptyCart.setVisibility(View.VISIBLE);
        }
    }

    private int getCartTotal() {
        int total = 0;

        for (int i = 0; i < cartItems.size(); i++) {
            total += cartItems.get(i).getPrice();
        }

        return total;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // value passed in AutoResolveHelper
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "Result Ok");
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        handlePaymentSuccess(paymentData);
                        User currentUser = new User(ParseUser.getCurrentUser());

                        currentUser.setItemsSold(cartItems);

                        Log.i(TAG, cartItems.get(0).getIsSold() + "");

                        try {
                            currentUser.clearCart(cartItems);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        finish();
                        break;

                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "cancelled");
                        // The user cancelled the payment attempt
                        break;

                    case AutoResolveHelper.RESULT_ERROR:
                        Log.i(TAG, "error");
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        handleError(status.getStatusCode());
                        break;
                }

                // Re-enables the Google Pay payment button.
                googlePayButton.setClickable(true);
        }
    }

    private void possiblyShowGooglePayButton() {

        final Optional<JSONObject> isReadyToPayJson = PaymentsUtil.getIsReadyToPayRequest();
        if (!isReadyToPayJson.isPresent()) {
            return;
        }

        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
        Task<Boolean> task = paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(this,
                task1 -> {
                    if (task1.isSuccessful()) {
                        setGooglePayAvailable(task1.getResult());
                    } else {
                        Log.w("isReadyToPay failed", task1.getException());
                    }
                });
    }

    private void setGooglePayAvailable(boolean available) {
        if (available) {
            googlePayButton.setVisibility(View.VISIBLE);
        } else {
            // Add string resource
            Toast.makeText(this, "Google pay not available", Toast.LENGTH_LONG).show();
        }
    }

    private void handlePaymentSuccess(PaymentData paymentData) {

        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
        final String paymentInfo = paymentData.toJson();
        if (paymentInfo == null) {
            return;
        }

        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".

            final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
            final String token = tokenizationData.getString("token");
            final JSONObject info = paymentMethodData.getJSONObject("info");
            final String billingName = info.getJSONObject("billingAddress").getString("name");

            // Logging token string.
            Log.d("Google Pay token: ", token);

        } catch (JSONException e) {
            throw new RuntimeException("The selected garment cannot be parsed from the list of elements");
        }
    }

    private void handleError(int statusCode) {
        Log.e("loadPaymentData failed", String.format("Error code: %d", statusCode));
    }

    public void requestPayment(View view) {

        // Disables the button to prevent multiple clicks.
        googlePayButton.setClickable(false);

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.

            Optional<JSONObject> paymentDataRequestJson = PaymentsUtil
                    .getPaymentDataRequest(totalPrice);
            if (!paymentDataRequestJson.isPresent()) {
                return;
            }

            PaymentDataRequest request =
                    PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());

            // Since loadPaymentData may show the UI asking the user to select a payment method, we use
            // AutoResolveHelper to wait for the user interacting with it. Once completed,
            // onActivityResult will be called with the result.
            if (request != null) {
                AutoResolveHelper.resolveTask(
                        paymentsClient.loadPaymentData(request),
                        this, LOAD_PAYMENT_DATA_REQUEST_CODE);
            }
    }
}

