package com.example.android.bwc;


import android.content.ContentUris;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.bwc.data.BWContract;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class HubFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    CildCursorAdapeter mCursorAdapter;
    private final static int CHILD_LOADER = 0;
    int entryCounter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View containerView = inflater.inflate(R.layout.activity_hub, container, false);

        FloatingActionButton fab = (FloatingActionButton) containerView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditorChildActivity.class);
                startActivity(intent);
            }
        });

        ListView displayView = (ListView) containerView.findViewById(R.id.list_view_child);
        View emptyview = containerView.findViewById(R.id.empty_view);
        mCursorAdapter = new CildCursorAdapeter(getActivity(), null);
        displayView.setEmptyView(emptyview);
        displayView.setAdapter(mCursorAdapter);

        displayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EditorChildActivity.class);
                Uri currentChildUri = ContentUris.withAppendedId(BWContract.BwcEntry.CONTENT_URI, id);
                intent.setData(currentChildUri);
                startActivity(intent);

            }
        });

        getLoaderManager().initLoader(CHILD_LOADER, null, this);
        return containerView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_hub, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.delete_all:
                getActivity().getContentResolver().delete(BWContract.BwcEntry.CONTENT_URI, null, null);
                return true;
            case R.id.export_hub:

                ExportTask exporttask = new ExportTask();
                exporttask.execute();
//                Cursor cursor = getActivity().getContentResolver().query(BWContract.BwcEntry.CONTENT_URI
//                        , null, null, null, null);
//
//                ArrayList<String> entry = new ArrayList<>();
//
//
//                while (cursor.moveToNext()) {
//
//                    int nameColumnIndex = cursor.getColumnIndex(BWContract.BwcEntry.COLUMN_CHILD_NAME);
//                    int parentColumnIndex = cursor.getColumnIndex(BWContract.BwcEntry.COLUMN_PARENT_NAME);
//                    int genderColumnIndex = cursor.getColumnIndex(BWContract.BwcEntry.COLUMN_GENDER);
//                    int phoneColumnIndex = cursor.getColumnIndex(BWContract.BwcEntry.COLUMN_PHONE_NUMBER);
//                    int ageColumnIndex = cursor.getColumnIndex(BWContract.BwcEntry.COLUMN_AGE);
//                    int hubColumnIndex = cursor.getColumnIndex(BWContract.BwcEntry.COLUMN_HUB_NUMBER);
//                    int booksColumnIndex = cursor.getColumnIndex(BWContract.BwcEntry.COLUMN_BOOKS);
//
//                    // Extract out the value from the Cursor for the given column index
//
//                    String name = cursor.getString(nameColumnIndex);
//                    String parent = cursor.getString(parentColumnIndex);
//                    String gender = cursor.getString(genderColumnIndex);
//                    String phonenumber = cursor.getString(phoneColumnIndex);
//                    String age = cursor.getString(ageColumnIndex);
//                    String hubnumber = cursor.getString(hubColumnIndex);
//                    String book = cursor.getString(booksColumnIndex);
//
//                    entry.add("Child Name: " + name);
//                    entry.add("Parent Name: " + parent);
//                    entry.add("Gender: " + gender);
//                    entry.add("Phone: " + phonenumber);
//                    entry.add("Age: " + age);
//                    entry.add("Hub: " + hubnumber);
//                    entry.add("Books: " + book);
//
//                    entryCounter++;
//                }
//                cursor.close();
//
//                Document doc = new Document();
//
//
//                try {
//                    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
//
//                    File dir = new File(path, "BWC");
//                    if (!dir.exists())
//                        dir.mkdir();
//
//
//                    Log.d("PDFCreator", "PDF Path: " + path);
//
//                    String docName = "BookWorm" + entryCounter;
//
//
//                    File file = new File(dir, docName + ".pdf");
//
//
//                    FileOutputStream fOut = new FileOutputStream(file);
//
//                    PdfWriter.getInstance(doc, fOut);
//
//                    //open the document
//                    doc.open();
//                    for (int i = 0; i < entry.size(); i++) {
//                        Paragraph p1 = new Paragraph(entry.get(i));
//                        Font paraFont = new Font(Font.COURIER);
//                        p1.setAlignment(Paragraph.ALIGN_LEFT);
//                        p1.setFont(paraFont);
//
//                        doc.add(p1);
//                    }
//                } catch (DocumentException de) {
//                    Log.e("PDFCreator", "DocumentException:" + de);
//                } catch (IOException e) {
//                    Log.e("PDFCreator", "ioException:" + e);
//                } finally {
//                    doc.close();
//                }
//
//                Toast.makeText(getContext(), "Exported Successfully", Toast.LENGTH_SHORT).show();
//                return true;
                }
        return super.onOptionsItemSelected(item);
        }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                BWContract.BwcEntry._ID, BWContract.BwcEntry.COLUMN_CHILD_NAME, BWContract.BwcEntry.COLUMN_PHONE_NUMBER
        };
        return new CursorLoader(getContext(), BWContract.BwcEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mCursorAdapter.swapCursor(null);

    }

    private class ExportTask extends AsyncTask<Void, Void, ArrayList<String>>{

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            Cursor cursor = getActivity().getContentResolver().query(BWContract.BwcEntry.CONTENT_URI
                    , null, null, null, null);

            ArrayList<String> entry = new ArrayList<>();


            while (cursor.moveToNext()) {

                int nameColumnIndex = cursor.getColumnIndex(BWContract.BwcEntry.COLUMN_CHILD_NAME);
                int parentColumnIndex = cursor.getColumnIndex(BWContract.BwcEntry.COLUMN_PARENT_NAME);
                int genderColumnIndex = cursor.getColumnIndex(BWContract.BwcEntry.COLUMN_GENDER);
                int phoneColumnIndex = cursor.getColumnIndex(BWContract.BwcEntry.COLUMN_PHONE_NUMBER);
                int ageColumnIndex = cursor.getColumnIndex(BWContract.BwcEntry.COLUMN_AGE);
                int hubColumnIndex = cursor.getColumnIndex(BWContract.BwcEntry.COLUMN_HUB_NUMBER);
                int booksColumnIndex = cursor.getColumnIndex(BWContract.BwcEntry.COLUMN_BOOKS);

                // Extract out the value from the Cursor for the given column index

                String name = cursor.getString(nameColumnIndex);
                String parent = cursor.getString(parentColumnIndex);
                String gender = cursor.getString(genderColumnIndex);
                String phonenumber = cursor.getString(phoneColumnIndex);
                String age = cursor.getString(ageColumnIndex);
                String hubnumber = cursor.getString(hubColumnIndex);
                String book = cursor.getString(booksColumnIndex);

                entry.add("Child Name: " + name);
                entry.add("Parent Name: " + parent);
                entry.add("Gender: " + gender);
                entry.add("Phone: " + phonenumber);
                entry.add("Age: " + age);
                entry.add("Hub: " + hubnumber);
                entry.add("Books: " + book);

                entryCounter++;
            }
            cursor.close();
            return entry;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);


            Document doc = new Document();


            try {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath();

                File dir = new File(path, "BWC");
                if (!dir.exists())
                    dir.mkdir();


                Log.d("PDFCreator", "PDF Path: " + path);

                String docName = "BookWorm" + entryCounter;


                File file = new File(dir, docName + ".pdf");


                FileOutputStream fOut = new FileOutputStream(file);

                PdfWriter.getInstance(doc, fOut);

                //open the document
                doc.open();
                for (int i = 0; i < strings.size(); i++) {
                    Paragraph p1 = new Paragraph(strings.get(i));
                    Font paraFont = new Font(Font.COURIER);
                    p1.setAlignment(Paragraph.ALIGN_LEFT);
                    p1.setFont(paraFont);

                    doc.add(p1);
                }
            } catch (DocumentException de) {
                Log.e("PDFCreator", "DocumentException:" + de);
            } catch (IOException e) {
                Log.e("PDFCreator", "ioException:" + e);
            } finally {
                doc.close();
            }

            Toast.makeText(getContext(), "Exported Successfully", Toast.LENGTH_SHORT).show();

        }
    }
}
