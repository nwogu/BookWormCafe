package com.example.android.bwc;


import android.content.ContentUris;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.bwc.data.BWCDBHelper;
import com.example.android.bwc.data.BWContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class HubSearchFragment extends Fragment {

    CildCursorAdapeter mCursorAdapter;
    String hubNumber;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View containerView = inflater.inflate(R.layout.activity_hub_search, container, false);

        final ListView displayView = (ListView) containerView.findViewById(R.id.list_view_hub_search);
        Button searchButton = (Button) containerView.findViewById(R.id.search_button);
        final EditText editTextHub = (EditText) containerView.findViewById(R.id.edit_text_search_hub);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hubNumber = editTextHub.getText().toString();
                BWCDBHelper dbhelper = new BWCDBHelper(getContext());
                SQLiteDatabase db = dbhelper.getReadableDatabase();
                String[] Args = new String[] {hubNumber};
                Cursor cursor = db.query(BWContract.BwcEntry.TABLE_NAME, null
                        ,BWContract.BwcEntry.COLUMN_HUB_NUMBER + "=?", Args, null, null, null);
                mCursorAdapter = new CildCursorAdapeter(getActivity(), cursor);
                displayView.setAdapter(mCursorAdapter);



            }
        });


        displayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EditorChildActivity.class);
                Uri currentChildUri = ContentUris.withAppendedId(BWContract.BwcEntry.CONTENT_URI, id);
                intent.setData(currentChildUri);
                startActivity(intent);

            }
        });

        return containerView;
    }
}
