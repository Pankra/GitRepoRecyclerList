package com.pankra.gitrepolist.model;

/**
 * Created by Serge on 01.11.2015.
 */
public class Rate {

    Resources resources;

    class Resources {
        Limits core;
        Limits search;
    }

    class Limits {
        long limit;
        long remaining;
        long reset;
    }

    public Long getCoreLimit() {
        return resources == null || resources.core == null ? null : resources.core.limit;
    }

    public Long getCoreRemaining() {
        return resources == null || resources.core == null ? null : resources.core.remaining;
    }

    public Long getCoreReset() {
        return resources == null || resources.core == null ? null : resources.core.reset;
    }
}
