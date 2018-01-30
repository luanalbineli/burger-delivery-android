package com.burgerdelivery.repository.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

public abstract class BurgerDeliveryContract {
    static final String CONTENT_AUTHORITY = "com.burgerdelivery.provider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String PATH_ORDER = "order";
    static final String PATH_ITEM = "item";

    public static final class OrderEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_ORDER)
                .build();

        static final String TABLE_NAME = "orderr"; // With an extra r, because order is a SQLite special keyword.

        public static final String COLUMN_STATUS = "status";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_SERVER_ID = "serverId";

        public static Uri buildOrderWithId(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(id))
                    .build();
        }
    }

    public static final class OrderItemEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_ITEM)
                .build();

        static final String TABLE_NAME = "order_item";

        public static final String COLUMN_BURGER_ID = "burgerId";

        public static final String COLUMN_ORDER_ID = "orderId";

        public static final String COLUMN_BURGER_NAME = "burgerName";

        public static final String COLUMN_BURGER_DESCRIPTION = "burgerDescription";

        public static final String COLUMN_BURGER_PRICE = "burgerPrice";

        public static final String COLUMN_BURGER_IMAGE_URL = "burgerImageUrl";

        public static final String COLUMN_BURGER_INGREDIENTS = "burgerIngredients";

        public static final String COLUMN_ADDITIONAL = "additional";

        public static final String COLUMN_OBSERVATION = "observation";

        public static final String COLUMN_QUANTITY = "quantity";
    }
}
