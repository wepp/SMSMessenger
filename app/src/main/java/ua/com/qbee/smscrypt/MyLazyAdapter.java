package ua.com.qbee.smscrypt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyLazyAdapter extends BaseAdapter {
    Context cont;
    LayoutInflater lInflater;
    ArrayList<ObjectItem> objects;

    MyLazyAdapter(Context context, ArrayList<ObjectItem> mylist) {
        cont = context;
        objects = mylist;
        lInflater = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return objects.size();
    }

    public Object getItem(int position) {
        return objects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // ���������� ���������, �� �� ������������ view
        View view = convertView;
        if (view == null) {
            //�������� LayoutInflater ��� ������ � layout-���������
            view = lInflater.inflate(R.layout.my_listview, parent, false);
        }

        ObjectItem p = ((ObjectItem) getItem(position));

        // ��������� View � ������ ������ �������
        ((TextView) view.findViewById(R.id.textView1)).setText(p.message);
        ((TextView) view.findViewById(R.id.time)).setText(p.date.toString());
        ((ImageView) view.findViewById(R.id.imageView)).setImageURI(p.image);
        ((TextView) view.findViewById(R.id.title)).setText(p.title);
        if (p.getRead())
            ((ImageView) view.findViewById(R.id.imageView1)).setImageResource(android.R.drawable.presence_online);
        if (!p.getRead())
            ((ImageView) view.findViewById(R.id.imageView1)).setImageResource(android.R.drawable.presence_offline);
        return view;
    }
}
