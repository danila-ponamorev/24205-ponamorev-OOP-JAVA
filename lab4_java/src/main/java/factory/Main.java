package factory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import factory.core.Dealer;
import factory.core.PartSupplier;
import factory.core.StorageController;
import factory.gui.FactoryWindow;
import factory.model.Accessory;
import factory.model.Auto;
import factory.model.Body;
import factory.model.Motor;
import factory.storage.Storage;
import threadpool.ThreadPool;

public class Main {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                Path fallback = Path.of("config.properties");
                if (Files.exists(fallback)) {
                    System.err.println("WARNING: config.properties not found on classpath; loading from working directory: " + fallback.toAbsolutePath());
                    props.load(Files.newInputStream(fallback));
                } else {
                    throw new IllegalStateException("Unable to load config.properties from classpath or working directory. " +
                            "Make sure src/main/resources/config.properties is on the classpath.");
                }
            }
        }

        System.out.println("Configuration:");
        props.forEach((k, v) -> System.out.println("  " + k + " = " + v));

        Storage<Body> bodyStorage = new Storage<>(Integer.parseInt(props.getProperty("StorageBodySize")));
        Storage<Motor> motorStorage = new Storage<>(Integer.parseInt(props.getProperty("StorageMotorSize")));
        Storage<Accessory> accStorage = new Storage<>(Integer.parseInt(props.getProperty("StorageAccessorySize")));
        Storage<Auto> autoStorage = new Storage<>(Integer.parseInt(props.getProperty("StorageAutoSize")));

        ThreadPool threadPool = new ThreadPool(Integer.parseInt(props.getProperty("Workers")));

        List<PartSupplier<?>> suppliers = new ArrayList<>();
        suppliers.add(new PartSupplier<>(bodyStorage, Body::new));
        suppliers.add(new PartSupplier<>(motorStorage, Motor::new));
        
        int accCount = Integer.parseInt(props.getProperty("AccessorySuppliers"));
        for (int i = 0; i < accCount; i++) {
            suppliers.add(new PartSupplier<>(accStorage, Accessory::new));
        }

        List<Dealer> dealers = new ArrayList<>();
        int dealersCount = Integer.parseInt(props.getProperty("Dealers"));
        boolean logEnabled = Boolean.parseBoolean(props.getProperty("LogSale"));
        for (int i = 0; i < dealersCount; i++) {
            dealers.add(new Dealer(i + 1, autoStorage, logEnabled));
        }

        StorageController controller = new StorageController(autoStorage, bodyStorage, motorStorage, accStorage, threadPool);

        List<Thread> allThreads = new ArrayList<>();
        for (PartSupplier<?> s : suppliers) allThreads.add(new Thread(s));
        for (Dealer d : dealers) allThreads.add(new Thread(d));
        allThreads.add(new Thread(controller));

        allThreads.forEach(Thread::start);

        javax.swing.SwingUtilities.invokeLater(() -> 
            new FactoryWindow(bodyStorage, motorStorage, accStorage, autoStorage, suppliers, dealers, threadPool)
        );


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down gracefully...");
            threadPool.shutdown();
            allThreads.forEach(Thread::interrupt);
        }));
    }
}
