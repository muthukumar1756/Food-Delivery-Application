package org.swiggy;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

@Component
public class RestaurantActivator {

    @Activate
    public void start() {
        System.out.println("Restaurant Bundle Is Started");
    }

    @Deactivate
    public void stop() {
        System.out.println("Restaurant Bundle Is Stopped");
    }
}
