package com.example.android.bwc;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.bwc.data.BWCDBHelper;
import com.example.android.bwc.data.BWContract;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.android.bwc.R.id.phone_number;
import static org.bouncycastle.asn1.x500.style.RFC4519Style.c;

/**
 * Created by TEST on 7/1/2017.
 */

public class EditorChildActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private EditText mName;

    private EditText mAge;

    private String mGender = BWContract.BwcEntry.GENDER_UNKNOWN;

    private EditText mPhoneNumber;

    private EditText mParentName;

    private EditText mHubNumber;

    private EditText mBooks;

    private final static int EXISTING_CHILD_LOADER = 0;

    private Uri mCurrentChildUri;

    private Uri mNewUri;

    private int rowsUpdated;

    private Spinner mGenderSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentChildUri = intent.getData();

        if (mCurrentChildUri == null){
            setTitle("Add New Child");
        }

        else {
            setTitle("Edit Child");
            getLoaderManager().initLoader(EXISTING_CHILD_LOADER, null, this);
        }

        mName = (EditText) findViewById(R.id.edit_child_name);
        mAge = (EditText) findViewById(R.id.edit_age);
        mPhoneNumber = (EditText) findViewById(R.id.edit_phone_number);
        mParentName = (EditText) findViewById(R.id.edit_parent_name);
        mBooks = (EditText) findViewById(R.id.edit_books);
        mHubNumber = (EditText) findViewById(R.id.edit_hub_number);
        mGenderSpinner = (Spinner)findViewById(R.id.spinner_gender);

        setupSpinner();


    }

    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = BWContract.BwcEntry.GENDER_MALE; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = BWContract.BwcEntry.GENDER_FEMALE; // Female
                    } else {
                        mGender = BWContract.BwcEntry.GENDER_UNKNOWN; // Unknown
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = BWContract.BwcEntry.GENDER_UNKNOWN; // Unknown
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentChildUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                String child_name = mName.getText().toString();
                String _age = mAge.getText().toString();
                String parent_name = mParentName.getText().toString();
                String _phone = mPhoneNumber.getText().toString();
                String _hub_number = mHubNumber.getText().toString();
                String books = mBooks.getText().toString();

                if (TextUtils.isEmpty(child_name)){
                    child_name = "No Name";
                }

                if (TextUtils.isEmpty(_age)){
                    _age = "Unknown";
                }

                if (TextUtils.isEmpty(parent_name)){
                    parent_name = "Unknown";
                }

                if (TextUtils.isEmpty(_phone)){
                    _phone = "Unknown";
                }

                if (TextUtils.isEmpty(_hub_number)){
                    _hub_number = "0";
                }

                if (TextUtils.isEmpty(books)){
                    books = "No Books";
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put(BWContract.BwcEntry.COLUMN_CHILD_NAME, child_name);
                contentValues.put(BWContract.BwcEntry.COLUMN_AGE, _age);
                contentValues.put(BWContract.BwcEntry.COLUMN_PARENT_NAME, parent_name);
                contentValues.put(BWContract.BwcEntry.COLUMN_PHONE_NUMBER, _phone);
                contentValues.put(BWContract.BwcEntry.COLUMN_HUB_NUMBER, _hub_number);
                contentValues.put(BWContract.BwcEntry.COLUMN_BOOKS, books);
                contentValues.put(BWContract.BwcEntry.COLUMN_GENDER, mGender);

                if (mCurrentChildUri == null){
                    mNewUri = getContentResolver().insert(BWContract.BwcEntry.CONTENT_URI, contentValues);
                    Toast.makeText(this, "Child Added Successfully",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }

                   else{ rowsUpdated = getContentResolver().update(mCurrentChildUri, contentValues, null, null);

                    Toast.makeText(this, "Updated Successfully",
                            Toast.LENGTH_SHORT).show();
                finish();}

                return true;

            case R.id.delete:
                int rowsDeleted = getContentResolver().delete(mCurrentChildUri, null, null);
                if (rowsDeleted == 0) {
                    // If no rows were deleted, then there was an error with the delete.
                    Toast.makeText(this, "Delete failed",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the delete was successful and we can display a toast.
                    Toast.makeText(this, "Deleted successfully",
                            Toast.LENGTH_SHORT).show();
                }

        finish();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                BWContract.BwcEntry._ID,
                BWContract.BwcEntry.COLUMN_PHONE_NUMBER,
                BWContract.BwcEntry.COLUMN_CHILD_NAME,
                BWContract.BwcEntry.COLUMN_AGE,
                BWContract.BwcEntry.COLUMN_GENDER,
                BWContract.BwcEntry.COLUMN_PARENT_NAME,
                BWContract.BwcEntry.COLUMN_HUB_NUMBER,
                BWContract.BwcEntry.COLUMN_BOOKS};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentChildUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.moveToFirst()){
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = data.getColumnIndex(BWContract.BwcEntry.COLUMN_CHILD_NAME);
            int parentColumnIndex = data.getColumnIndex(BWContract.BwcEntry.COLUMN_PARENT_NAME);
            int genderColumnIndex = data.getColumnIndex(BWContract.BwcEntry.COLUMN_GENDER);
            int phoneColumnIndex = data.getColumnIndex(BWContract.BwcEntry.COLUMN_PHONE_NUMBER);
            int ageColumnIndex = data.getColumnIndex(BWContract.BwcEntry.COLUMN_AGE);
            int hubColumnIndex = data.getColumnIndex(BWContract.BwcEntry.COLUMN_HUB_NUMBER);
            int booksColumnIndex = data.getColumnIndex(BWContract.BwcEntry.COLUMN_BOOKS);

            // Extract out the value from the Cursor for the given column index
            String name = data.getString(nameColumnIndex);
            String parent = data.getString(parentColumnIndex);
            String gender = data.getString(genderColumnIndex);
            String phonenumber = data.getString(phoneColumnIndex);
            String age = data.getString(ageColumnIndex);
            String hubnumber = data.getString(hubColumnIndex);
            String books = data.getString(booksColumnIndex);

            // Update the views on the screen with the values from the database
            mName.setText(name);
            mParentName.setText(parent);
            mPhoneNumber.setText(phonenumber);
            mAge.setText(age);
            mHubNumber.setText(hubnumber);
            mBooks.setText(books);


            // Gender is a dropdown spinner, so map the constant value from the database
            // into one of the dropdown options (0 is Unknown, 1 is Male, 2 is Female).
            // Then call setSelection() so that option is displayed on screen as the current selection.
            switch (gender) {
                case BWContract.BwcEntry.GENDER_MALE:
                    mGenderSpinner.setSelection(1);
                    break;
                case BWContract.BwcEntry.GENDER_FEMALE:
                    mGenderSpinner.setSelection(2);
                    break;
                default:
                    mGenderSpinner.setSelection(0);
                    break;
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
