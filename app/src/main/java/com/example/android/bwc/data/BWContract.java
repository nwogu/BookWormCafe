package com.example.android.bwc.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.android.bwc.R;

/**
 * Created by TEST on 7/19/2017.
 */

public final class BWContract {

    private BWContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.bwc";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BWC = "child";

    public static final class BwcEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BWC);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BWC;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BWC;


        public static final String TABLE_NAME = "child";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_CHILD_NAME = "childname";

        public static final String COLUMN_PARENT_NAME= "parentname";

        public static final String COLUMN_GENDER = "gender";

        public static final String COLUMN_PHONE_NUMBER = "phone";

        public static final String COLUMN_AGE = "age";

        public static final String COLUMN_HUB_NUMBER = "hub";

        public static final String COLUMN_BOOKS = "books";

        public static final String GENDER_UNKNOWN = "Unknown";

        public static final String GENDER_MALE = "Male";

        public static final String GENDER_FEMALE = "Female";

    }
}
