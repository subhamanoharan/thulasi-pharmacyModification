package com.cryptic.imed.util.view;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cryptic.imed.R;

/**
 * @author sharafat
 */
public class TwoLineListItemView {
    public static View getView(LayoutInflater layoutInflater, View convertView, ViewGroup parent,
                                String primaryText, String secondaryText) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    Build.VERSION.SDK_INT < 11
                            ? android.R.layout.simple_list_item_2
                            : android.R.layout.simple_list_item_activated_2,
                    parent, false);
        }

        TextView primaryTextView = (TextView) convertView.findViewById(android.R.id.text1);
        TextView secondaryTextView = (TextView) convertView.findViewById(android.R.id.text2);

        primaryTextView.setText(primaryText);
        secondaryTextView.setText(secondaryText);

        return convertView;
    }
}
