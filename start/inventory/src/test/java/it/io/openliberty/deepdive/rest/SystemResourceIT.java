package it.io.openliberty.deepdive.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Base64;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
public class SystemResourceIT {

    private static Logger logger = LoggerFactory.getLogger(SystemResourceIT.class);
    private static String appPath = "/inventory/api";
    private static String postgresHost = "postgres";
    private static String postgresImageName = "postgres-sample:latest";
    private static String appImageName = "liberty-deepdive-inventory:1.0-SNAPSHOT";

    public static SystemResourceClient client;
    public static Network network = Network.newNetwork();
    private static String authHeader;

    @Container
    public static GenericContainer<?> postgresContainer
        = new GenericContainer<>(postgresImageName)
              .withNetwork(network)
              .withExposedPorts(5432)
              .withNetworkAliases(postgresHost)
              .withLogConsumer(new Slf4jLogConsumer(logger));

    @Container
    public static LibertyContainer libertyContainer
        = new LibertyContainer(appImageName)
              .withEnv("POSTGRES_HOSTNAME", postgresHost)
              .withNetwork(network)
              .waitingFor(Wait.forHttp("/health/ready"))
              .withLogConsumer(new Slf4jLogConsumer(logger));

    @BeforeAll
    public static void setupTestClass() throws Exception {
        System.out.println("TEST: Starting Liberty Container setup");
        client = libertyContainer.createRestClient(
            SystemResourceClient.class, appPath);
        String userPassword = "bob" + ":" + "bobpwd";
        authHeader = "Basic "
            + Base64.getEncoder().encodeToString(userPassword.getBytes());
    }

    private void showSystemData(SystemData system) {
        System.out.println("TEST: SystemData > "
            + system.getId() + ", "
            + system.getHostname() + ", "
            + system.getOsName() + ", "
            + system.getJavaVersion() + ", "
            + system.getHeapSize());
    }

    @Test
    @Order(1)
    public void testAddSystem() {
        System.out.println("TEST: Testing add a system");
        client.addSystem("localhost", "linux", "11", Long.valueOf(2048));
        List<SystemData> systems = client.listContents();
        assertEquals(1, systems.size());
        showSystemData(systems.get(0));
        assertEquals("11", systems.get(0).getJavaVersion());
        assertEquals(Long.valueOf(2048), systems.get(0).getHeapSize());
    }

    @Test
    @Order(2)
    public void testUpdateSystem() {
        System.out.println("TEST: Testing update a system");
        client.updateSystem(authHeader, "localhost", "linux", "8", Long.valueOf(1024));
        SystemData system = client.getSystem("localhost");
        showSystemData(system);
        assertEquals("8", system.getJavaVersion());
        assertEquals(Long.valueOf(1024), system.getHeapSize());
    }

    @Test
    @Order(3)
    public void testRemoveSystem() {
        System.out.println("TEST: Testing remove a system");
        client.removeSystem(authHeader, "localhost");
        List<SystemData> systems = client.listContents();
        assertEquals(0, systems.size());
    }
}

