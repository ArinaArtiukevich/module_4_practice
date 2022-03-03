package com.esm.epam.util;

public class ParameterAttribute {
    private ParameterAttribute() {
    }

    public static final String USER_NAME = "arina";

    public static final String TAG_FIELD_ID = "id";
    public static final String TAG_FIELD_NAME = "name";
    public static final String TAG_FIELD_CERTIFICATES = "certificateList";

    public static final String CERTIFICATE_FIELD_ID = "id";
    public static final String CERTIFICATE_FIELD_NAME = "name";
    public static final String CERTIFICATE_FIELD_DESCRIPTION = "description";
    public static final String CERTIFICATE_FIELD_TAGS = "tags";
    public static final String CERTIFICATE_FIELD_DATE = "createDate";
    public static final String CERTIFICATE_FIELD_USERS = "userList";


    public static final String ORDER_FIELD_PRICE = "price";
    public static final String ORDER_FIELD_USER_ID = "idUser";

    public static final String PERCENT_SYMBOL = "%";

    public static final String TAG = "tag";

    public static final String CERTIFICATE_NAME = "name";
    public static final String CERTIFICATE_DESCRIPTION = "description";

    public static final String NAME_PARAMETER = "name";
    public static final String DATE_PARAMETER = "date";
    public static final String DIRECTION_PARAMETER = "direction";
    public static final String PAGE_PARAMETER = "page";
    public static final String SIZE_PARAMETER = "size";

    public static final String SORT_STATEMENT = "sort";
    public static final String ASC_STATEMENT = "ASC";
    public static final String DESC_STATEMENT = "DESC";

    public static final String GET_MOST_WIDELY_USED_TAG = "SELECT * FROM tags WHERE tags.tag_id IN (\n" +
            "SELECT tags.tag_id\n" +
            "FROM tags\n" +
            "RIGHT JOIN certificates_tags ON (tags.tag_id = certificates_tags.tag_id) \n" +
            "RIGHT JOIN gift_certificates ON (gift_certificates.id = certificates_tags.certificate_id)\n" +
            "RIGHT JOIN orders ON (gift_certificates.id = orders.certificate_id) \n" +
            "RIGHT JOIN users ON (orders.user_id = users.user_id) \n" +
            "WHERE users.user_id = \n" +
                "(SELECT orders.user_id \n" +
                "FROM orders \n" +
                "GROUP BY orders.user_id\n" +
                "HAVING sum(orders.price) =\n" +
                    "(SELECT max(user_price) FROM (\n" +
                        "SELECT orders.user_id, sum(orders.price) as user_price \n" +
                        "FROM orders \n" +
                        "GROUP BY orders.user_id) AS all_users_price) \n" +
                " LIMIT 1 OFFSET 0)\n" +
            "GROUP BY tags.tag_id\n" +
            "HAVING count(tags.tag_id) = \n" +
                "( SELECT max(tags_count) FROM (\n" +
                    "SELECT tags.tag_id, count(tags.tag_id) AS tags_count\n" +
                    "FROM tags\n" +
                    "RIGHT JOIN certificates_tags ON (tags.tag_id = certificates_tags.tag_id) \n" +
                    "RIGHT JOIN gift_certificates ON (gift_certificates.id = certificates_tags.certificate_id)\n" +
                    "RIGHT JOIN orders ON (gift_certificates.id = orders.certificate_id) \n" +
                    "RIGHT JOIN users ON (orders.user_id = users.user_id) \n" +
                    "WHERE users.user_id = \n" +
                        "(SELECT orders.user_id \n" +
                        "FROM orders \n" +
                        "GROUP BY orders.user_id\n" +
                        "HAVING sum(orders.price) =\n" +
                            "(SELECT max(user_price) FROM (\n" +
                                "SELECT orders.user_id, sum(orders.price) as user_price \n" +
                                "FROM orders \n" +
                                "GROUP BY orders.user_id) AS all_users_price)\n" +
                        " LIMIT 1 OFFSET 0)\n" +
                "GROUP BY tags.tag_id ) AS counted_tags )\n" +
            "LIMIT 1 OFFSET 0\n" +
            ")";

}
