package com.cryptic.imed.app;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

/**
 * @author sharafat
 */
class DbConfigUtil extends OrmLiteConfigUtil {
    public static void main(String[] args) throws Exception {
        writeConfigFile("ormlite_config.txt");
    }
}
