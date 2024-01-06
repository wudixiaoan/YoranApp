package com.yoran.app.model.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.internal.DaoConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MigrationHelper {

    private static String TAG = "MigrationHelper";
    private static final String CONVERSION_CLASS_NOT_FOUND_EXCEPTION = "MIGRATION HELPER - CLASS DOESN'T MATCH WITH THE CURRENT " + "PARAMETERS";
    private static List<String> tablenames = new ArrayList<>();

    public static List<String> getTables(SQLiteDatabase db) {
        List<String> tables = new ArrayList<>();

        Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' order by name", null);
        while (cursor.moveToNext()) {
            //遍历出表名
            tables.add(cursor.getString(0));
        }
        cursor.close();
        return tables;
    }

    public static void migrate(SQLiteDatabase db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        Database database = new StandardDatabase(db);
        Log.d(TAG, "【Database Version】" + db.getVersion());
        Log.d(TAG, "【Generate temp table】start");
        tablenames = getTables(db);
        Log.d(TAG, "【Generate temp table】" + tablenames);
        generateTempTables(database, daoClasses);
        Log.d(TAG, "【Generate temp table】complete");
        dropAllTables(database, true, daoClasses);
        createAllTables(database, false, daoClasses);
        Log.d(TAG, "【Restore data】start");
        restoreData(database, daoClasses);
        Log.d(TAG, "【Restore data】complete");
    }

    private static void generateTempTables(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (int i = 0; i < daoClasses.length; i++) {
            String tempTableName = null;

            try {
                DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
                if (!tablenames.contains(daoConfig.tablename)) {//如果数据库中没有该表，则继续下次循环
                    continue;
                }
                String tableName = daoConfig.tablename;
                tempTableName = daoConfig.tablename.concat("_TEMP");

                StringBuilder dropTableStringBuilder = new StringBuilder();
                dropTableStringBuilder.append("DROP TABLE IF EXISTS ").append(tempTableName).append(";");
                db.execSQL(dropTableStringBuilder.toString());

                StringBuilder insertTableStringBuilder = new StringBuilder();
                insertTableStringBuilder.append("CREATE TEMPORARY TABLE ").append(tempTableName);
                insertTableStringBuilder.append(" AS SELECT * FROM ").append(tableName).append(";");
                db.execSQL(insertTableStringBuilder.toString());
                Log.d(TAG, "【Table】" + tableName + "\n ---Columns-->" + getColumnsStr(daoConfig));
                Log.d(TAG, "【Generate temp table】" + tempTableName);
            } catch (SQLException e) {
                Log.e(TAG, "【Failed to generate temp table】" + tempTableName, e);
            }
        }
    }

    private static String getColumnsStr(DaoConfig daoConfig) {
        if (daoConfig == null) {
            return "no columns";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < daoConfig.allColumns.length; i++) {
            builder.append(daoConfig.allColumns[i]);
            builder.append(",");
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    private static void dropAllTables(Database db, boolean ifExists,  Class<? extends AbstractDao<?, ?>>... daoClasses) {
        reflectMethod(db, "dropTable", ifExists, daoClasses);
        Log.d(TAG, "【Drop all table】");
    }

    private static void createAllTables(Database db, boolean ifNotExists,Class<? extends AbstractDao<?, ?>>... daoClasses) {
        reflectMethod(db, "createTable", ifNotExists, daoClasses);
        Log.d(TAG, "【Create all table】");
    }

    /**
     * dao class already define the sql exec method, so just invoke it
     */
    private static void reflectMethod(Database db, String methodName, boolean isExists,  Class<? extends AbstractDao<?, ?>>... daoClasses) {
        if (daoClasses.length < 1) {
            return;
        }
        try {
            for (Class cls : daoClasses) {
                Method method = cls.getDeclaredMethod(methodName, Database.class, boolean.class);
                method.invoke(null, db, isExists);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void restoreData(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);

            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");
            ArrayList<String> properties = new ArrayList();
            ArrayList<String> propertiesQuery = new ArrayList();
            for (int j = 0; j < daoConfig.properties.length; j++) {
                String columnName = daoConfig.properties[j].columnName;

                if (getColumns(db, tempTableName).contains(columnName)) {
                    properties.add(columnName);
                    propertiesQuery.add(columnName);
                } else {
                    try {
                        if (getTypeByClass(daoConfig.properties[j].type).equals("INTEGER")) {
                            propertiesQuery.add("0 as " + columnName);
                            properties.add(columnName);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            StringBuilder insertTableStringBuilder = new StringBuilder();
            insertTableStringBuilder.append("INSERT INTO ").append(tableName).append(" (");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(") SELECT ");
            insertTableStringBuilder.append(TextUtils.join(",", propertiesQuery));
            insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";");
            StringBuilder dropTableStringBuilder = new StringBuilder();
            dropTableStringBuilder.append("DROP TABLE ").append(tempTableName);
            try {
                db.execSQL(insertTableStringBuilder.toString());
                db.execSQL(dropTableStringBuilder.toString());
            } catch (Exception e) {

            }
        }
    }

    private static String getTypeByClass(Class<?> type) throws Exception {
        if (type.equals(String.class)) {
            return "TEXT";
        }
        if (type.equals(Long.class) || type.equals(Integer.class) || type.equals(long.class) || type.equals(int.class)) {
            return "INTEGER";
        }
        if (type.equals(Boolean.class)) {
            return "BOOLEAN";
        }

        Exception exception = new Exception(CONVERSION_CLASS_NOT_FOUND_EXCEPTION.concat(" - Class: ").concat(type.toString()));
        System.out.print(exception.getMessage());
        throw exception;
    }

    private static List<String> getColumns(Database db, String tableName) {
        List<String> columns = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 1", null);
            if (cursor != null) {
                columns = new ArrayList<>(Arrays.asList(cursor.getColumnNames()));
            }
        } catch (Exception e) {
            Log.v(tableName, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return columns;
    }
}