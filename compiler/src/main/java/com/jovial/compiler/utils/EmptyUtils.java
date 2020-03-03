package com.jovial.compiler.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/21 0021]
 * About Class:[ ]
 */
public final class EmptyUtils {
    public static boolean isEmpty(CharSequence cs ){return cs == null || cs.length() == 0;}

    public static boolean isEmpty(Collection<?> coll){ return coll == null || coll.isEmpty();}

    public static boolean isEmpty(final Map<?,?> map){ return map == null || map.isEmpty();}
}
