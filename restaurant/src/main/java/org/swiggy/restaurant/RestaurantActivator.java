package org.swiggy.restaurant;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class RestaurantActivator implements BundleActivator {

    @Override
    public void start(final BundleContext context) {
        System.out.println("Restaurant Bundle Is Started");
    }

    @Override
    public void stop(final BundleContext context) {
        System.out.println("Restaurant Bundle Is Stopped");
    }
}
