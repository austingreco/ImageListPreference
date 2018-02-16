package com.austingreco.imagelistpreference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ImageListPreference extends ListPreference {
    private static final String TAG = "ImageListPreference";
    private static final int DEFAULT_TINT = 0xFF000000;
    private static final int DEFAULT_BACKGROUND_TINT = 0xFFFFFFFF;

    private int mErrorResource;
    private List<Integer> mImages;
    private int mTintColor;
    private int mBackgroundColor;
    private boolean mUseCard;
    private int mCustomItemLayout;

    private class ImageListPreferenceAdapter extends ArrayAdapter<ImageListItem> {
        private List<ImageListItem> mItems;
        private int mLayoutResource;

        ImageListPreferenceAdapter(Context context, int layoutResource, List<ImageListItem> items) {
            super(context, layoutResource, items);
            mLayoutResource = layoutResource;
            mItems = items;
        }

        @Override
        public @NonNull
        View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)getContext().
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                try {
                    assert inflater != null;
                    convertView = inflater.inflate(mLayoutResource, parent, false);

                    holder = new ViewHolder();
                    holder.iconName = convertView.findViewById(R.id.imagelistpreference_text);
                    holder.iconImage = convertView.findViewById(R.id.imagelistpreference_image);
                    holder.radioButton = convertView.findViewById(R.id.imagelistpreference_radio);
                    convertView.setTag(holder);
                } catch (Exception e) {
                    e.printStackTrace();
                    return super.getView(position, null, parent);
                }
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            if (holder == null) {
                return super.getView(position, convertView, parent);
            }

            ImageListItem item = mItems.get(position);

            holder.iconName.setText(item.name);

            if (item.resource != 0) {
                holder.iconImage.setImageResource(item.resource);
            } else {
                holder.iconImage.setImageResource(mErrorResource);
            }

            if (mTintColor != 0) {
                holder.iconImage.setColorFilter(mTintColor);
            }
            if (mBackgroundColor != 0) {
                holder.iconImage.setBackgroundColor(mBackgroundColor);
            }

            holder.radioButton.setChecked(item.isChecked);

            return convertView;
        }
    }

    private class ImageListItem {
        private int resource;
        private boolean isChecked;
        private String name;

        ImageListItem(CharSequence name, int resource, boolean isChecked) {
            this(name.toString(), resource, isChecked);
        }

        ImageListItem(String name, int resource, boolean isChecked) {
            this.name = name;
            this.resource = resource;
            this.isChecked = isChecked;
        }
    }

    private class ViewHolder {
        ImageView iconImage;
        TextView iconName;
        RadioButton radioButton;
    }

    public ImageListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        mImages = new ArrayList<>();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ImageListPreference);

        try {
            int entryImagesArrayResource = array.getResourceId(R.styleable.ImageListPreference_ilp_entryImages, 0);
            String tintKey = array.getNonResourceString(R.styleable.ImageListPreference_ilp_tintKey);
            String backgroundKey = array.getNonResourceString(R.styleable.ImageListPreference_ilp_backgroundTint);

            mTintColor = array.getColor(R.styleable.ImageListPreference_ilp_tint, DEFAULT_TINT);
            mBackgroundColor = array.getColor(R.styleable.ImageListPreference_ilp_backgroundTint, 0);
            mErrorResource = array.getResourceId(R.styleable.ImageListPreference_ilp_errorImage, 0);
            mUseCard = array.getBoolean(R.styleable.ImageListPreference_ilp_useCard, false);
            mCustomItemLayout = array.getResourceId(R.styleable.ImageListPreference_ilp_itemLayout, 0);

            if (tintKey != null) {
                mTintColor = sharedPreferences.getInt(tintKey, mTintColor);
            }
            if (backgroundKey != null) {
                mBackgroundColor = sharedPreferences.getInt(backgroundKey, mBackgroundColor);
            }

            TypedArray images = context.getResources().obtainTypedArray(entryImagesArrayResource);

            for (int i=0 ; i<images.length() ; i++) {
                mImages.add(images.getResourceId(i, 0));
            }

            images.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            array.recycle();
        }
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        List<ImageListItem> items = new ArrayList<>();

        int length = getEntries().length;
        for (int i=0 ; i<length ; i++) {
            int resource = 0;
            if (mImages.size() > i) {
                resource = mImages.get(i);
            }
            items.add(new ImageListItem(getEntries()[i], resource, (getEntryValues()[i]).equals(getValue())));
        }

        int layout = R.layout.imagelistpreference_item;
        if (mUseCard) {
            layout = R.layout.imagelistpreference_item_card;
        }
        if (mCustomItemLayout != 0) {
            layout = mCustomItemLayout;
        }

        ListAdapter adapter = new ImageListPreferenceAdapter(getContext(), layout, items);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ImageListPreference.super.onClick(dialog, which);
            }
        });

        super.onPrepareDialogBuilder(builder);
    }
}