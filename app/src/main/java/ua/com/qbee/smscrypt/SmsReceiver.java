package ua.com.qbee.smscrypt;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.ArrayList;

public class SmsReceiver extends BroadcastReceiver {

    private static final int NOTIFY_ID = 101;
    String temp;

    @Override
    public void onReceive(Context context, Intent intent) {
        //---получить входящее SMS сообщение---
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String message = "", from = "", shortMess = "";

        if (bundle != null)

        {
            //---извлечь полученное SMS ---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            //from +=  msgs[0].getOriginatingAddress();
            System.out.println("first " + from);

            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                if (from == "") from = msgs[i].getOriginatingAddress();
                message += msgs[i].getOriginatingAddress();
                message += " :";
                message += msgs[i].getMessageBody().toString();
                shortMess += msgs[i].getMessageBody().toString();
                message += "\n";
            }
            System.out.println("second " + from);

            StringBuilder bodyText = new StringBuilder();
            for (int i = 0; i < msgs.length; i++)
                bodyText.append(msgs[i].getMessageBody());
            message = bodyText.toString();
            message = Transliterate.unTransliterate(message);
            shortMess = Transliterate.unTransliterate(shortMess);


            //---Показать новое SMS сообщение---

            MainActivity.needToReloadDialogActivity = true;
            if (MainActivity.act != null && (
                    MainActivity.act.phoneNumber.equals(from) ||
                            MainActivity.act.phoneNumber2.equals(from))) {
                Message m = new Message(false, message, System.currentTimeMillis(), "0");
                MainActivity.act.addNewMessage(m);
            } else {
                System.out.println("third " + from);
                createNotification(context, message, from, shortMess);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void createNotification(Context context, String message, String from, String shortMess) {
        //Context context = getApplicationContext();


        Intent notificationIntent = new Intent(context, DialogActivity.class);
        ArrayList<String> str = new ArrayList<String>();
        str.add(from);
        //if(MainActivity.hashGlob.containsKey(from))
        /*temp = from;
		System.out.println("TEMP "+temp);
		from=MainActivity.hashGlob.get(from);
		System.out.println("FROM "+from);
		if(from==null) from = temp;
		System.out.println("fourth "+from);*/
        notificationIntent.putExtra("Number", str);
        notificationIntent.putExtra("title", MainActivity.main.getName(from));

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.aaa)
                // большая картинка
                //.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.cat))
                //.setTicker(res.getString(R.string.warning)) // текст в строке состояния
                .setTicker(message)
                .setWhen(System.currentTimeMillis()) // java.lang.System.currentTimeMillis()
                .setAutoCancel(true)
                //.setContentTitle(res.getString(R.string.notifytitle))
                // Заголовок уведомления
                .setContentTitle(MainActivity.main.getName(from))
                //.setContentText(res.getString(R.string.notifytext))
                .setContentText(shortMess) // Текст уведомленимя
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLights(Color.RED, 1, 0);

        Notification n = builder.getNotification();
        MainActivity.needToReloadDialogActivity = true;
        nm.notify(NOTIFY_ID, n);
        abortBroadcast();

    }
}