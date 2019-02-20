package com.hn.lshn_morgage_calculator_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable; // for EditText event handling
import android.text.TextWatcher; // EditText listener
import android.widget.EditText; // for bill amount input
import android.widget.SeekBar; // for changing the tip percentage
import android.widget.SeekBar.OnSeekBarChangeListener; // SeekBar listener
import android.widget.TextView; // for displaying text

import java.text.NumberFormat; // for currency formatting

public class MainActivity extends AppCompatActivity {
    private static final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance();
    private static final NumberFormat percentFormat =
            NumberFormat.getPercentInstance();

    private double purchasePrice = 0.0;
    private double downPayment = 0.0;
    private double interestRate = 0.0;
    private int loanDuration = 0;
    private TextView loanAmountTextView;
    private TextView monthly10LoanTextView;
    private TextView  monthly20LoanTextView;
    private TextView  monthly30LoanTextView;
    private TextView monthlyLoanTextView;
    private TextView purchasePriceTextView;
    private TextView downPaymentTextView;
    private TextView interestRateTextView;
    private TextView loanDurationLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get references to programmatically manipulated TextViews
        loanAmountTextView = (TextView) findViewById(R.id.loanAmountTextView);
        monthly10LoanTextView = (TextView) findViewById(R.id.monthy10LoanTextView);
        monthly20LoanTextView = (TextView) findViewById(R.id.monthy20LoanTextView);
        monthly30LoanTextView = (TextView) findViewById(R.id.monthy30LoanTextView);
        monthlyLoanTextView = (TextView) findViewById(R.id.monthyLoanTextView);
        purchasePriceTextView = (TextView) findViewById(R.id.purchasePriceTextView);
        downPaymentTextView = (TextView) findViewById(R.id.downPaymentTextView);
        interestRateTextView = (TextView) findViewById(R.id.interestRateTextView);
        loanDurationLabel = (TextView) findViewById(R.id.loanDurationLabel);

        loanAmountTextView.setText(currencyFormat.format(0));
        monthly10LoanTextView.setText(currencyFormat.format(0));
        monthly20LoanTextView.setText(currencyFormat.format(0));
        monthly30LoanTextView.setText(currencyFormat.format(0));
        monthlyLoanTextView.setText(currencyFormat.format(0));

        //set purchasePriceEditText's TextWatcher
        EditText purchasePriceEditText =
                (EditText) findViewById(R.id.purchasePriceEditText);
        purchasePriceEditText.addTextChangedListener(purchasePriceEditTextWatcher);

        EditText downPaymentEditText =
                (EditText) findViewById(R.id.downPaymentEditText);
        downPaymentEditText.addTextChangedListener(downPaymentEditTextWatcher);

        EditText interestRateEditText =
                (EditText) findViewById(R.id.interestRateEditText);
        interestRateEditText.addTextChangedListener(interestRateEditTextWatcher);

        SeekBar loanDurationSeekBar =
                (SeekBar) findViewById(R.id.loanDurationSeekBar);
        loanDurationSeekBar.setOnSeekBarChangeListener(seekBarListener);
    }

    private void calculate(){
        loanDurationLabel.setText(String.format("%d years", loanDuration));
        double loanAmount = purchasePrice - downPayment;
        double c = interestRate/12;
        double monthly10 = loanAmount*c*Math.pow((1 + c),120)/(Math.pow((1 + c),120) - 1);
        double monthly20 = loanAmount * c * (Math.pow((1 + c),240)) / (Math.pow((1 + c),240) - 1);
        double monthly30 = loanAmount*c*Math.pow((1 + c),360)/(Math .pow((1 + c),360) - 1);
        double months = loanDuration * 12;
        double monthlyCustom = loanAmount*c*Math.pow((1 + c),months)/(Math.pow((1 + c),months) - 1);
        loanAmountTextView.setText(currencyFormat.format(loanAmount));
        monthly10LoanTextView.setText(currencyFormat.format(monthly10));
        monthly20LoanTextView.setText(currencyFormat.format(monthly20));
        monthly30LoanTextView.setText(currencyFormat.format(monthly30));
        monthlyLoanTextView.setText(currencyFormat.format(monthlyCustom));
    }

    // listener object for the SeekBar's progress changed events
    private final OnSeekBarChangeListener seekBarListener =
            new OnSeekBarChangeListener() {
                // update loanDuration, then call calculate
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    loanDuration = progress; // set percent based on progress\
                    calculate(); // calculate and display tip and total
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) { }
            };
    // listener object for the EditText's text-changed events
    private final TextWatcher purchasePriceEditTextWatcher = new TextWatcher() {
        // called when the user modifies the bill amount
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {

            try { // get bill amount  and display currency formatted value
                purchasePrice = Double.parseDouble(s.toString()) / 100.0;
                purchasePriceTextView.setText(currencyFormat.format(purchasePrice));
            }
            catch (NumberFormatException e) { // if s is empty or non-numeric
                purchasePriceTextView.setText("");
                purchasePrice = 0.0;
            }

            calculate(); // update the tip and total TextViews
        }

        @Override
        public void afterTextChanged(Editable s) { }

        @Override
        public void beforeTextChanged(
                CharSequence s, int start, int count, int after) { }
    };
    // listener object for the EditText's text-changed events
    private final TextWatcher downPaymentEditTextWatcher = new TextWatcher() {
        // called when the user modifies the bill amount
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {

            try { // get bill amount and display currency formatted value
                downPayment = Double.parseDouble(s.toString()) / 100.0;
                downPaymentTextView.setText(currencyFormat.format(downPayment));
            }
            catch (NumberFormatException e) { // if s is empty or non-numeric
                downPaymentTextView.setText("");
                downPayment = 0.0;
            }

            calculate(); // update the tip and total TextViews
        }

        @Override
        public void afterTextChanged(Editable s) { }

        @Override
        public void beforeTextChanged(
                CharSequence s, int start, int count, int after) { }
    };

    // listener object for the EditText's text-changed events
    private final TextWatcher interestRateEditTextWatcher = new TextWatcher() {
        // called when the user modifies the bill amount
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {

            if(interestRate>=0 && interestRate <=100){ // get bill amount and display currency formatted value
                interestRate = Double.parseDouble(s.toString());
                interestRateTextView.setText(percentFormat.format(interestRate));
            }
            else { // if s is empty or non-numeric
                interestRateTextView.setText("");
                interestRate = 0.0;
            }

            calculate(); // update the tip and total TextViews
        }

        @Override
        public void afterTextChanged(Editable s) { }

        @Override
        public void beforeTextChanged(
                CharSequence s, int start, int count, int after) { }
    };
}
