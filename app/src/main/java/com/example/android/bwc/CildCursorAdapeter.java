package com.example.android.bwc;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.bwc.data.BWContract;

/**
 * Created by TEST on 7/20/2017.
 */

public class CildCursorAdapeter extends CursorAdapter {

    public CildCursorAdapeter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // TODO: Fill out this method and return the list item view (instead of null)
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO: Fill out this method

        TextView nameTextView = (TextView) view.findViewById(R.id.child_name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.phone_number);

        int column_index_child_name = cursor.getColumnIndex(BWContract.BwcEntry.COLUMN_CHILD_NAME);
        int column_index_phone_number = cursor.getColumnIndex(BWContract.BwcEntry.COLUMN_PHONE_NUMBER);

        String child_name = cursor.getString(column_index_child_name);
        String phone_number = cursor.getString(column_index_phone_number);

        nameTextView.setText(child_name);
        summaryTextView.setText(phone_number);
    }
}
