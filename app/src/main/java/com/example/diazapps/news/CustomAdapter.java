package com.example.diazapps.news;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.regex.Pattern;

    public class CustomAdapter extends ArrayAdapter<NewsItem> {

        public CustomAdapter(Context context, ArrayList<NewsItem> news_item) {
            super(context, R.layout.custom_row, news_item);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // default -  return super.getView(position, convertView, parent);
            // add the layout
            LayoutInflater myCustomInflater = LayoutInflater.from(getContext());
            View customView = myCustomInflater.inflate(R.layout.custom_row, parent, false);
            // get references.

            TextView title = (TextView) customView.findViewById(R.id.newsTitle);
            TextView desc = (TextView) customView.findViewById(R.id.desc);
            ImageView newsPic = (ImageView) customView.findViewById(R.id.newsPic);

            String title1 = String.valueOf(getItem(position).getTitle());
            String desc1 = String.valueOf(getItem(position).getDesc());
            String url1 = getItem(position).getUrl();

            desc.setText(desc1);
            Picasso.with(getContext()).load(MainActivity.arrayList.get(position).getPic_url()).into(newsPic);
            title.setMovementMethod(LinkMovementMethod.getInstance());
//           "<a href='http://www.google.com'> Google </a>";
            title.setText(Html.fromHtml("<a href='"+url1+"'> "+title1+" </a>")); //Sets a hyperlink to article in the title
            customView.findViewById(R.id.desc).setSelected(true);

            // Now we can finally return our custom View or custom item
            return customView;

        }
    }
