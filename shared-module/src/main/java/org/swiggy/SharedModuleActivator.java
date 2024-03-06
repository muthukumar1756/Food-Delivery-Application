package org.swiggy;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

@Component
public class SharedModuleActivator {

    @Activate
    public void start() {
        System.out.println("Shared Bundle Is Started");
    }

    @Deactivate
    public void stop() {
        System.out.println("Shared Bundle Is Stopped");
    }
}
