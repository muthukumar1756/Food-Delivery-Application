package org.swiggy.database;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class DbActivator implements BundleActivator {
    
    @Override
    public void start(final BundleContext context) {
        System.out.println("Database Bundle Is Started");
    }

    @Override
    public void stop(final BundleContext context) {
        System.out.println("Database Bundle Is Stopped");
    }
}
