package siri_xlite.common;

import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.util.List;

public class Configuration extends ArrayListValuedHashMap<String, String> {

    private static final long serialVersionUID = 1L;

    public String getFirst(String key) {
        List<String> list = get(key);
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }

}
