package com.example.android.bwc.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import static android.R.attr.id;
import static android.R.attr.name;

/**
 * Created by TEST on 7/19/2017.
 */

public class BWCProvider extends ContentProvider {

    private BWCDBHelper bwcdbHelper;

    private static final int CHILD = 100;

    private static final int CHILD_ID = 101;

    private int rowsDeleted;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /** Tag for the log messages */
    public static final String LOG_TAG = BWCProvider.class.getSimpleName();

    static {
        sUriMatcher.addURI(BWContract.CONTENT_AUTHORITY, BWContract.PATH_BWC, CHILD);
        sUriMatcher.addURI(BWContract.CONTENT_AUTHORITY, BWContract.PATH_BWC + "/#", CHILD_ID);
    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        // TODO: Create and initialize a PetDbHelper object to gain access to the pets database.
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        bwcdbHelper = new BWCDBHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = bwcdbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case CHILD:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                // TODO: Perform database query on pets table
                cursor = database.query(BWContract.BwcEntry.TABLE_NAME, null, null, null, null, null, null);
                break;
            case CHILD_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = BWContract.BwcEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(BWContract.BwcEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CHILD:
                return insertChild(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertChild(Uri uri, ContentValues values) {

        // TODO: Insert a new pet into the pets database table with the given ContentValues

        SQLiteDatabase db = bwcdbHelper.getWritableDatabase();


        long id = db.insert(BWContract.BwcEntry.TABLE_NAME, null, values);

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }


        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CHILD:
                return updateChild(uri, contentValues, selection, selectionArgs);
            case CHILD_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = BWContract.BwcEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateChild(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updateChild(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
//        if (values.containsKey(BWContract.BwcEntry.COLUMN_CHILD_NAME)) {
//            String child_name = values.getAsString(BWContract.BwcEntry.COLUMN_CHILD_NAME);
//            if (child_name == null) {
//                throw new IllegalArgumentException("Child requires a name");
//            }
//        }
//
//        if (values.containsKey(BWContract.BwcEntry.COLUMN_PARENT_NAME)) {
//            String parent_name = values.getAsString(BWContract.BwcEntry.COLUMN_PARENT_NAME);
//            if (parent_name == null) {
//                throw new IllegalArgumentException("Child requires a Parent Name");
//            }
//        }
//
//        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
//        // check that the gender value is valid.
//        if (values.containsKey(BWContract.BwcEntry.COLUMN_GENDER)) {
//            String gender = values.getAsString(BWContract.BwcEntry.COLUMN_GENDER);
//            if (gender == null) {
//                throw new IllegalArgumentException("Child requires valid gender");
//            }
//        }
//
//        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
//        // check that the weight value is valid.
//        if (values.containsKey(BWContract.BwcEntry.COLUMN_AGE)) {
//            // Check that the weight is greater than or equal to 0 kg
//            String age = values.getAsString(BWContract.BwcEntry.COLUMN_AGE);
//            if (age == null) {
//                throw new IllegalArgumentException("Age requires valid number");
//            }
//        }
//
//        if (values.containsKey(BWContract.BwcEntry.COLUMN_PHONE_NUMBER)) {
//            String phonenumber = values.getAsString(BWContract.BwcEntry.COLUMN_PHONE_NUMBER);
//            if (phonenumber == null){
//                throw new IllegalArgumentException("Phone Number Requires a valid number");
//            }
//        }
//
//        if (values.containsKey(BWContract.BwcEntry.COLUMN_HUB_NUMBER)) {
//            String hubnumber = values.getAsString(BWContract.BwcEntry.COLUMN_HUB_NUMBER);
//            if (hubnumber == null){
//                throw new IllegalArgumentException("Hub Number Requires a valid number");
//            }
//        }
//
//
//        // No need to check the breed, any value is valid (including null).
//
//        // If there are no values to update, then don't try to update the database
//        if (values.size() == 0) {
//            return 0;
//        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = bwcdbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        int rowsUpdated = database.update(BWContract.BwcEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        // Get writeable database
        SQLiteDatabase database = bwcdbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CHILD:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(BWContract.BwcEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);}
                return rowsDeleted;
            case CHILD_ID:
                // Delete a single row given by the ID in the URI
                selection = BWContract.BwcEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(BWContract.BwcEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);}
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }


    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CHILD:
                return BWContract.BwcEntry.CONTENT_LIST_TYPE;
            case CHILD_ID:
                return BWContract.BwcEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
