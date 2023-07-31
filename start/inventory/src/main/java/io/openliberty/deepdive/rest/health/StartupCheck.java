package io.openliberty.deepdive.rest.health;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.Startup;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

@Startup
@ApplicationScoped
public class StartupCheck implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean)
        ManagementFactory.getOperatingSystemMXBean();
        double cpuUsed = bean.getSystemCpuLoad();
        String cpuUsage = String.valueOf(cpuUsed);
        return HealthCheckResponse.named("Startup Check")
                                  .status(cpuUsed < 0.95).build();
    }
}

