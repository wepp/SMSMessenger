package ua.com.qbee.smscrypt;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MessageAct extends ActionBarActivity {

    private static final int PICK_RESULT = 0;
    boolean send = true;
    String phoneNumber = "";
    String message;
    EditText num;
    EditText mes;
    Button buttonCont;
    Button buttonSend;

    PendingIntent sentPI;
    PendingIntent deliveredPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        num = (EditText) findViewById(R.id.editTextN);
        mes = (EditText) findViewById(R.id.editTextM);
        buttonCont = (Button) findViewById(R.id.buttonC);
        buttonSend = (Button) findViewById(R.id.buttonS);

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        if (send) {
                            ContentValues values = new ContentValues();
                            values.put("address", phoneNumber);
                            values.put("body", message);
                            values.put("date", System.currentTimeMillis());
                            if (!GlobMethods.onlyNumb(num.getText().toString()).equals(""))
                                values.put("persone", num.getText().toString());
                            getContentResolver().insert(Uri.parse("content://sms/sent"), values);
                            send = false;
                        }
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.SMSsent),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.generic_failure),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---пїЅпїЅпїЅпїЅпїЅ SMS пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.SMS_delivered),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.SMS_not_delivered),
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret;
        if (item.getItemId() == android.R.id.home) {
            // Handle Settings
            ret = true;
            MessageAct.this.finish();
        } else {
            if (item.getItemId() == R.id.action_item_3) {
                // Handle Settings
                ret = true;
                Intent intent = new Intent(MessageAct.this, AboutActivity.class);
                startActivity(intent);
            } else {
                ret = super.onOptionsItemSelected(item);
            }

        }
        return ret;
    }

    public void chooseClick(View v) {
        Uri uri = Uri.parse("content://contacts");
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, uri);
        pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_RESULT);
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == PICK_RESULT)
            if (resultCode == RESULT_OK) {

                String[] projection = {Phone.NUMBER, Phone.DISPLAY_NAME};
                Uri contactUri = data.getData();
                Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
                cursor.moveToFirst();
                //пїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅ
                phoneNumber = GlobMethods.onlyNumb(cursor.getString(cursor.getColumnIndex(Phone.NUMBER)));//пїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅ пїЅпїЅ пїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅ
                num.setText(cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME)));// пїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ
            }

    }

    public void sendClick(View v) {
        if (phoneNumber.equals(""))
            phoneNumber = num.getText().toString();
        message = mes.getText().toString();
        if (phoneNumber.length() > 0 && message.length() > 0) {
            sendSMS(phoneNumber, message);
            num.setText("");
            mes.setText("");
            send = true;
        } else {
            Toast.makeText(getBaseContext(),
                    getResources().getString(R.string.enter_both),
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void sendSMS(final String phoneNumber, String message) {
        if (Transliterate.needToTransliterate(message))
            message = Transliterate.transliterate(message);
        SmsManager sms = SmsManager.getDefault();
        if (message.length() > 160) {
            ArrayList<String> mArray = sms.divideMessage(message);
            ArrayList<PendingIntent> sentArrayIntents = new ArrayList<PendingIntent>();
            for (int i = 0; i < mArray.size(); i++)
                sentArrayIntents.add(sentPI);
            sms.sendMultipartTextMessage(phoneNumber, null, mArray, sentArrayIntents, null);
        } else {
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        }
        MainActivity.needToReloadDialogActivity = true;
    }


}
