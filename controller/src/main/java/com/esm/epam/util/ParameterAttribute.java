package com.esm.epam.util;

import java.util.Arrays;
import java.util.List;

public class ParameterAttribute {
    private ParameterAttribute() {
    }

    public static final String TAG_SORT = "tag";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String SORT = "sort";
    public static final String DIRECTION = "direction";
    public static final String PAGE = "page";
    public static final String SIZE = "size";

    public static final String ORDER = "Order";
    public static final String TAG = "Tag";
    public static final String MOST_WIDELY_USED_TAG = "mostWidelyUsedTag";

    public static final int DEFAULT_PAGE_NUMBER = 1;
    public static final int DEFAULT_SIZE = 5;

    public static final List<String> SORT_KEYS = Arrays.asList(TAG_SORT, NAME, DESCRIPTION, SORT, DIRECTION, PAGE, SIZE);
}
