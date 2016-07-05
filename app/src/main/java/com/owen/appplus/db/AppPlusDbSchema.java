package com.owen.appplus.db;

/**
 * Created by mike on 16/7/3.
 */
public class AppPlusDbSchema {

    public static final class AppTable {
        public static final String NAME = "apps";

        public static final class Cols {
            public static final String APP_NAME = "app_name";
            public static final String ICON = "icon";
            public static final String VERSION_NAME = "version_name";
            public static final String VERSION_CODE = "version_code";
            public static final String PACKAGE_NAME = "package_name";
        }
    }
}
