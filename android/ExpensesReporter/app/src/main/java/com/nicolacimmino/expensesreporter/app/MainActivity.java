package com.nicolacimmino.expensesreporter.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends Activity {

    private ExpensesTransactionData transactionData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transactionData = new ExpensesTransactionData(this);
        transactionData.open();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_submit) {
            onTransactionConfirmClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onKeypadClick(View v) {
        TextView amountView = (TextView) findViewById(R.id.textAmount);
        amountView.setText(amountView.getText() + (String)v.getTag());
    }

    public void onKeypadDelClick(View v) {
        TextView amountView = (TextView) findViewById(R.id.textAmount);
        if(amountView.getText().length() > 0) {
            amountView.setText(amountView.getText().subSequence(0, amountView.getText().length() - 1));
        }
    }

    public void onToggleDestination(View v)
    {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupDestination);
        excludeOtherSelections(radioGroup, v);
    }

    public void onToggleSource(View v)
    {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupSource);
        excludeOtherSelections(radioGroup, v);
    }

    public void excludeOtherSelections(RadioGroup radioGroup, View v)
    {
        for(int x=0; x<radioGroup.getChildCount(); x++)
        {
            ToggleButton toggleButton = (ToggleButton)radioGroup.getChildAt(x);
            if(toggleButton.getTag() != v.getTag())
            {
                toggleButton.setChecked(false);
            }
        }
    }

    public String getSelectedText(RadioGroup radioGroup)
    {
        for(int x=0; x<radioGroup.getChildCount(); x++)
        {
            ToggleButton toggleButton = (ToggleButton)radioGroup.getChildAt(x);
            if(toggleButton.isChecked())
            {
                return (String)toggleButton.getTag();
            }
        }
        return "";
    }

    public void onTransactionConfirmClick()
    {
        TextView amountView = (TextView) findViewById(R.id.textAmount);
        RadioGroup radioGroupSource = (RadioGroup) findViewById(R.id.radioGroupSource);
        RadioGroup radioGroupDestination = (RadioGroup) findViewById(R.id.radioGroupDestination);
        transactionData.addTransaction(getSelectedText(radioGroupSource), getSelectedText(radioGroupDestination), Double.parseDouble(amountView.getText().toString()));
        amountView.setText("");
    }

}
