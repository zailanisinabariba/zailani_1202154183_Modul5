package com.modul5.ZAILANI_1202154183_MODUL5;

import android.provider.BaseColumns;

/**
 * Created by waski on 24/03/2018.
 */

public class TodoPointer {
    private TodoPointer(){}
    public static final class TodoEntry implements BaseColumns {
        public static final String TABLE_NAME = "TodoAktivitas";
        public static final String COLUMN_NAMA_ = "namaAktivitas";
        public static final String COLUMN_DESKRIPSI = "deskripsi";
        public static final String COLUMN_PRIOTIRTY = "prioritas";
    }

}
