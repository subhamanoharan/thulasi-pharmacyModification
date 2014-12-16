package com.cryptic.imed.app;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.google.inject.Inject;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * @author sharafat
 */
public abstract class AbstractDbHelper extends OrmLiteSqliteOpenHelper {
    private static final Logger log = LoggerFactory.getLogger(AbstractDbHelper.class);

    @Inject
    private static Application application;

    public static OrmLiteSqliteOpenHelper getHelper() {
        return OpenHelperManager.getHelper(application, DbHelper.class);
    }

    public static void release() {
        OpenHelperManager.releaseHelper();
    }

    public AbstractDbHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory,
                            int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    public AbstractDbHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory,
                            int databaseVersion, int configFileId) {
        super(context, databaseName, factory, databaseVersion, configFileId);
    }

    public AbstractDbHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory,
                            int databaseVersion, File configFile) {
        super(context, databaseName, factory, databaseVersion, configFile);
    }

    public AbstractDbHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory,
                            int databaseVersion, InputStream stream) {
        super(context, databaseName, factory, databaseVersion, stream);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        if (!db.isReadOnly()) {
            //enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @SuppressWarnings("unchecked")
    public void createTables(ConnectionSource connectionSource, Class... dataClasses) {
        for (Class clazz : dataClasses) {
            try {
                TableUtils.createTable(connectionSource, clazz);
            } catch (SQLException e) {
                log.error("Error creating table for dataClass: " + clazz, e);
            }
        }
    }
}
