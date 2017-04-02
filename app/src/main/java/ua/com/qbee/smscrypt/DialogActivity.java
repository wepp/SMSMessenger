package ua.com.qbee.smscrypt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DialogActivity extends ActionBarActivity {

    String formattedDate;
    ArrayList<Message> messages;
    AwesomeAdapter adapter;
    EditText text;
    String nameTitle;
    String phoneNumber = "";
    String phoneNumber2 = "";
    String message;
    PendingIntent sentPI;
    PendingIntent deliveredPI;
    ListView list;
    private boolean send = true;
    ArrayList<String> str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        text = (EditText) this.findViewById(R.id.text);
        list = (ListView) this.findViewById(R.id.listView1);

        str = getIntent().getStringArrayListExtra("Number");
        messages = getArreyOfMessages(str);
        nameTitle = getIntent().getStringExtra("title");
        this.setTitle(nameTitle);
        phoneNumber = str.get(0);
        if (str.size() >= 2)
            phoneNumber2 = str.get(1);
        this.setTitle(nameTitle);


        adapter = new AwesomeAdapter(this, messages);
        list.setAdapter(adapter);
        list.setOnItemLongClickListener(lclicker);
        MainActivity.act = this;

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
                            values.put("body", Transliterate.unTransliterate(message));
                            values.put("date", System.currentTimeMillis());
                            if (!GlobMethods.onlyNumb(phoneNumber).equals(""))
                                values.put("person", nameTitle);
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

    OnItemLongClickListener lclicker = new OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DialogActivity.this);
            builder.setCancelable(true)
                    .setNegativeButton("Delete",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    MainActivity.main.delete(((Message) parent.getItemAtPosition(position)).getId());
                                    messages = getArreyOfMessages(str);
                                    adapter = new AwesomeAdapter(DialogActivity.this, messages);
                                    list.setAdapter(adapter);
                                    list.setOnItemLongClickListener(lclicker);

                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }

    };

    public ArrayList<Message> getArreyOfMessages(ArrayList<String> str) {
        Message[] mes = null;
        String args = "address = '" + str.get(0) + "'";
        for (int i = 1; i < str.size(); i++)
            args += " or address = '" + str.get(i) + "'";
        Cursor cIn = getContentResolver().query(Uri.parse("content://sms/inbox"),
                new String[]{"date", "body", "address", "_id", "read"}, args, null, "date desc");
        Cursor cOut = getContentResolver().query(Uri.parse("content://sms/sent"),
                new String[]{"date", "body", "_id"}, args, null, "date desc");
        mes = new Message[cIn.getCount() + cOut.getCount()];
        int i = 0;
        {
            cIn.moveToFirst();
            if (cIn.getCount() != 0)
                do {
                    System.out.println(cIn.getString(4));
                    long date = Long.valueOf(cIn.getString(0));
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss; dd.MM.yyyy");
                    formattedDate = sdf.format(date).toString();
                    String mess = cIn.getString(cIn.getColumnIndex("body")) + "\n\n" + formattedDate;
                    if (Transliterate.needToUntransliterate(mess)) {
                        //MainActivity.main.replace(mess,cIn.getString(2), cIn.getString(0));
                        mess = Transliterate.unTransliterate(mess);
                        ContentValues values = new ContentValues();
                        values.put("body", mess);
                        getContentResolver().update(Uri.parse("content://sms/inbox"),
                                values, "_id=" + cIn.getString(3), null);
                    }
                    if (cIn.getString(4) != null && cIn.getString(4).equals("0")) {
                        ContentValues values = new ContentValues();
                        values.put("read", true);
                        getContentResolver().update(Uri.parse("content://sms/inbox"),
                                values, "_id=" + cIn.getString(3), null);
                        MainActivity.needToReloadDialogActivity = true;
                    }
                    mes[i++] = (new Message(mess, false,
                            Long.valueOf(GlobMethods.onlyNumb(cIn.getString(cIn.getColumnIndex("date"))))
                            , cIn.getString(3)));
                } while (cIn.moveToNext());
        }
        {
            cOut.moveToFirst();
            if (cOut.getCount() != 0)
                do {
                    long date = Long.valueOf(cOut.getString(0));
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss; dd.MM.yyyy");
                    formattedDate = sdf.format(date).toString();
                    mes[i++] = (new Message(
                            cOut.getString(cOut.getColumnIndex("body")) + "\n\n" + formattedDate, true,
                            Long.valueOf(GlobMethods.onlyNumb(cOut.getString(
                                    cOut.getColumnIndex("date")))), cOut.getString(2)));
                } while (cOut.moveToNext());
        }
        cIn.close();
        cOut.close();
        ArrayList<Message> messages = new ArrayList<Message>();
        MergeSort.sort(mes);
        for (int j = 0; j < mes.length; j++) {
            messages.add(mes[j]);
        }
        return messages;
    }

    protected void onPause() {
        MainActivity.act = null;
        messages.clear();
        super.onPause();
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dialog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret;
        if (item.getItemId() == android.R.id.home) {
            // Handle Settings
            ret = true;
            DialogActivity.this.finish();
        } else {
            if (item.getItemId() == R.id.action_item_3) {
                // Handle Settings
                ret = true;
                Intent intent = new Intent(DialogActivity.this, AboutActivity.class);
                startActivity(intent);
            } else {
                ret = super.onOptionsItemSelected(item);
            }

        }
        return ret;
    }

    public void sendMessage(View v) {
        message = text.getText().toString();

        if (message != null && !message.equals("")) {
            Message mes = new Message(message, true, System.currentTimeMillis(), "0");
            addNewMessage(mes);
            sendSMS(phoneNumber, message);
            text.setText("");
        } else
            Toast.makeText(getBaseContext(),
                    getResources().getString(R.string.enter_message),
                    Toast.LENGTH_SHORT).show();
    }

    public void addNewMessage(Message m) {

        messages.add(m);
        adapter.notifyDataSetChanged();
        list.setSelection(messages.size() - 1);
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
        send = true;
        MainActivity.needToReloadDialogActivity = true;
    }


}
