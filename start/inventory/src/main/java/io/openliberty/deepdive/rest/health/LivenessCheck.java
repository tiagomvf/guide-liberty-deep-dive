package io.openliberty.deepdive.rest.health;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

@Liveness
@ApplicationScoped
public class LivenessCheck implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
        double memUsed = memBean.getHeapMemoryUsage().getUsed();
        double memMax = memBean.getHeapMemoryUsage().getMax();

        return HealthCheckResponse.named("Liveness Check")
                                  .status(memUsed < memMax * 0.9)
                                  .build();
    }
}

