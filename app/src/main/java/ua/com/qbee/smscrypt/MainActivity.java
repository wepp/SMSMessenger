package ua.com.qbee.smscrypt;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ActionBarActivity {
    public static boolean needToReloadDialogActivity = false;
    public static DialogActivity act = null;
    public static MainActivity main = null;
    private static long back_pressed;
    Button buttonNew;
    private ListView lv;
    public static HashMap<String, String> hashGlob = new HashMap<String, String>();
    MyLazyAdapter catAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.main = this;
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.listView2);
        lv.setOnItemClickListener(clicker);
        lv.setOnItemLongClickListener(lclicker);
        catAdapter = new MyLazyAdapter(this, getAllnames());
        lv.setAdapter(catAdapter);
        //getName("+380986178495");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret;
        if (item.getItemId() == R.id.action_new) {
            // Handle Settings
            ret = true;
            Intent intent = new Intent(MainActivity.this, MessageAct.class);
            startActivity(intent);
        } else {
            if (item.getItemId() == R.id.action_item_3) {
                // Handle Settings
                ret = true;
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            } else {
                ret = super.onOptionsItemSelected(item);
            }

        }
        return ret;
    }

    public void newClick(View v) {
        Intent in = new Intent(MainActivity.this, MessageAct.class);
        startActivity(in);
    }


    OnItemClickListener clicker = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent in = new Intent(MainActivity.this, DialogActivity.class);
            if (checkForClick((ObjectItem) parent.getItemAtPosition(position))) return;
            ArrayList<String> value = new ArrayList<String>();
            value.add(((ObjectItem) parent.getItemAtPosition(position)).addressReal);
            value.add(((ObjectItem) parent.getItemAtPosition(position)).address);
            in.putStringArrayListExtra("Number", value);
            in.putExtra("title", ((ObjectItem) parent.getItemAtPosition(position)).getTitle());
            startActivity(in);
        }
    };

    OnItemLongClickListener lclicker = new OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(true)
                    .setNegativeButton("Delete",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ArrayList<String> value = new ArrayList<String>();
                                    value.add(((ObjectItem) parent.getItemAtPosition(position)).addressReal);
                                    value.add(((ObjectItem) parent.getItemAtPosition(position)).address);
                                    delete(getArreyOfMessages(value));
                                    catAdapter = new MyLazyAdapter(MainActivity.this, getAllnames());
                                    lv.setAdapter(catAdapter);
                                    System.out.println("long click");
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }

    };

    public ArrayList<Message> getArreyOfMessages(ArrayList<String> str) {
        String formattedDate;
        Message[] mes = null;
        String args = "address = '" + str.get(0) + "'";
        for (int i = 1; i < str.size(); i++)
            args += " or address = '" + str.get(i) + "'";
        Cursor cIn = getContentResolver().query(Uri.parse("content://sms/inbox"),
                new String[]{"date", "body", "address", "_id"}, args, null, "date desc");
        Cursor cOut = getContentResolver().query(Uri.parse("content://sms/sent"),
                new String[]{"date", "body", "_id"}, args, null, "date desc");
        mes = new Message[cIn.getCount() + cOut.getCount()];
        int i = 0;
        {
            cIn.moveToFirst();
            if (cIn.getCount() != 0)
                do {
                    long date = Long.valueOf(cIn.getString(0));
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss; dd.MM.yyyy");
                    formattedDate = sdf.format(date).toString();
                    String mess = cIn.getString(cIn.getColumnIndex("body")) + "\n\n" + formattedDate;
                    /*if(Transliterate.needToUntransliterate(mess)){
						//MainActivity.main.replace(mess,cIn.getString(2), cIn.getString(0));
						mess=Transliterate.unTransliterate(mess);
					}*/
                    mes[i++] = (new Message(
                            mess, false,
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

    public void onResume() {
        super.onResume();
        if (MainActivity.needToReloadDialogActivity) {
            MainActivity.needToReloadDialogActivity = false;
            setContentView(R.layout.activity_main);
            lv = (ListView) findViewById(R.id.listView2);
            lv.setOnItemClickListener(clicker);
            lv.setOnItemLongClickListener(lclicker);
            MyLazyAdapter catAdapter = new MyLazyAdapter(this, getAllnames());
            lv.setAdapter(catAdapter);
        }
    }

    private boolean checkForClick(ObjectItem obj) {
        if (obj.getTitle().equals("Sorry")) {
            Toast.makeText(getBaseContext(),
                    getResources().getString(R.string.notConversation),
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public ArrayList<ObjectItem> getAllnames() {
        HashMap<String, ObjectItem> hash = new HashMap<String, ObjectItem>();
        {
            {
                Cursor c = getContentResolver().query(Uri.parse("content://sms")
                        , new String[]{"address", "date", "body", "read"}, null, null, "date desc");
                if (c.getCount() == 0) {
                    ArrayList<ObjectItem> s = new ArrayList<ObjectItem>();
                    s.add(new ObjectItem(getResources().getString(R.string.sorry), getResources().getString(R.string.conversationsfound),
                            Long.toString(System.currentTimeMillis()), null));
                    return s;
                }
                c.moveToFirst();
                ObjectItem n;
                do {
                    n = new ObjectItem();
                    if (c.getString(0) != null) n.setAdd(c.getString(0));
                    if (c.getString(1) != null) n.putDate(c.getString(1));
                    if (c.getString(2) != null) n.setMessage(c.getString(2));
                    if (c.getString(3) != null && c.getString(3).equals("0")) {
                        n.setRead(true);
                        System.out.println("READ READ READ READ");
                    }
                    if (!hash.containsKey(n.address)) {
                        hash.put(n.address, n);
                        if (c.getString(3).equals("0")) {
                            n.setRead(true);
                            System.out.println("READ READ READ READ");
                        }

                    }
                } while (c.moveToNext());
                c.close();
            }
            {
                Cursor c = getContentResolver().query(Phone.CONTENT_URI
                        , new String[]{Phone.DISPLAY_NAME, Phone.NUMBER, Phone.PHOTO_THUMBNAIL_URI},
                        null, null, null);
                c.moveToFirst();
                String name = "";
                String numb = "";
                Uri photo = null;
                if (c.getCount() != 0)
                    do {
                        photo = null;
                        if (c.getString(0) != null) name = c.getString(0);
                        if (c.getString(1) != null) numb = c.getString(1);
                        if (c.getString(2) != null) photo = Uri.parse(c.getString(2));
                        numb = wockOv(numb);
                        if (hash.containsKey(numb)) {
                            hash.get(numb).setTitle(name);
                            hash.get(numb).setImage(photo);
                        }
                    } while (c.moveToNext());
                c.close();
            }
        }
        ObjectItem ar[] = new ObjectItem[hash.size()];
        int i = 0;
        for (ObjectItem a : hash.values()) {
            ar[i++] = a;
//            System.out.println(a.addressReal);
        }
        MergeSort.sort(ar);
        ArrayList<ObjectItem> obj = new ArrayList<ObjectItem>();
        for (i = 0; i < ar.length; i++) {
            obj.add(ar[i]);
            hashGlob.put(ar[i].addressReal, ar[i].getTitle());
        }
        return obj;
    }

    private String wockOv(String numb) {
        String res = "";
        numb = onlyNumb(numb);
        if (numb.length() <= 10)
            return numb;
        for (int i = 10; i > 0; i--)
            res += numb.charAt(numb.length() - i);
        return res;
    }

    private String onlyNumb(String number) {
        String res = "";
        for (char a : number.toCharArray())
            if ((a <= '9' && a >= '0') || a == '+') res += a;
        return res;
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else
            Toast.makeText(getBaseContext(), getResources().getString(R.string.exit),
                    Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

    public InputStream openPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = getContentResolver().query(photoUri,
                new String[]{Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return new ByteArrayInputStream(data);
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    public void delete(String id) {
        getContentResolver().delete(Uri.parse("content://sms/" + id), null, null);
    }

    public void delete(ArrayList<Message> in) {
        for (Message a : in)
            getContentResolver().delete(Uri.parse("content://sms/" + a.getId()), null, null);
    }

    String getName(String numb) {
		/*Cursor c = getContentResolver().query(Phone.CONTENT_URI
				, new String[]{Phone.DISPLAY_NAME}, 
				"data4 = '"+number+"' or data4 = '"+wockOv(number)+"'", null, null);
		c.moveToFirst();
		String ret="";
		if(c.getCount()==0)ret = number;
		else ret = c.getString(0);
		c.close();
		return ret;*/
        Uri uri = Phone.CONTENT_URI;
        String[] projection = new String[]{Phone.DISPLAY_NAME,
                Phone.NUMBER};

        Cursor people = getContentResolver().query(uri, projection, null, null, null);

        int indexName = people.getColumnIndex(Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(Phone.NUMBER);

        people.moveToFirst();
        do {
            String name = people.getString(indexName);
            String number = people.getString(indexNumber);
            System.out.println(name + " " + number);
            if (onlyNumb(number).equals(numb) || onlyNumb(number).equals(wockOv(numb)) ||
                    wockOv(onlyNumb(number)).equals(numb)) {
                System.out.println("           YPA name = " + name);
                return name;
            }
            // Do work...
        } while (people.moveToNext());
        System.out.println("GOLYAK");
        return numb;
    }
}
