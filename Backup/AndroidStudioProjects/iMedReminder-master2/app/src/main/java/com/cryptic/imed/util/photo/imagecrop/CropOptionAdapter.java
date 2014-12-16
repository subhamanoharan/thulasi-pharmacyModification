package com.cryptic.imed.util.photo.imagecrop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter for crop option list.
 *
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 * @author Sharafat Ibn Mollah Mosharraf <sharafat_8271@yahoo.co.uk>
 */
class CropOptionAdapter extends ArrayAdapter<CropOption> {
    private Context mContext;
    private ArrayList<CropOption> mOptions;

    public CropOptionAdapter(Context context, ArrayList<CropOption> options) {
        super(context, 0, options);

        mContext = context;
        mOptions = options;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup group) {
        if (convertView == null) {
            convertView = createCropSelectionView();
        }

        CropOption item = mOptions.get(position);

        if (item != null) {
            ((ImageView) convertView.findViewById(1)).setImageDrawable(item.icon);
            ((TextView) convertView.findViewById(2)).setText(item.title);

            return convertView;
        }

        return null;
    }

    private View createCropSelectionView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ImageView imageView = new ImageView(mContext);
        imageView.setId(1);
        imageView.setLayoutParams(layoutParams);

        TextView textView = new TextView(mContext);
        textView.setId(2);
        textView.setLayoutParams(layoutParams);

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setPadding(10, 10, 10, 10);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);

        linearLayout.addView(imageView);
        linearLayout.addView(textView);

        return linearLayout;
    }
}
