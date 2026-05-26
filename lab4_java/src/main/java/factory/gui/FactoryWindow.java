package factory.gui;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;

import factory.core.Dealer;
import factory.core.PartSupplier;
import factory.storage.Storage;
import threadpool.ThreadPool;

/**
 * The main GUI window for the factory emulator. It displays the current state of the storages, the delays of suppliers and dealers, and allows adjusting those delays with sliders.
 */
public class FactoryWindow extends JFrame {
    public FactoryWindow(Storage<?> bodyStorage, Storage<?> motorStorage, Storage<?> accStorage, Storage<?> autoStorage,
                         List<PartSupplier<?>> suppliers, List<Dealer> dealers, ThreadPool threadPool) {
        setTitle("Factory Emulator");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1));

        JLabel SupplierJLabel = new JLabel();
        JLabel DealerJLabel = new JLabel();
        JLabel bodyLabel = new JLabel();
        JLabel motorLabel = new JLabel();
        JLabel accLabel = new JLabel();
        JLabel autoLabel = new JLabel();
        JLabel poolLabel = new JLabel();
        JLabel totalProducedLabel = new JLabel();

        add(createControlPanel("Suppliers delay", suppliers, PartSupplier::setDelayMs));
        add(createControlPanel("Dealers delay", dealers, Dealer::setDelayMs));

        add(bodyLabel); add(motorLabel); add(accLabel); add(autoLabel); add(poolLabel); add(SupplierJLabel); add(DealerJLabel); add(totalProducedLabel);

        Timer timer = new Timer(500, e -> {
            SupplierJLabel.setText("Suppliers delay: " + suppliers.get(0).getDelayMs() + " ms");
            DealerJLabel.setText("Dealers delay: " + dealers.get(0).getDelayMs() + " ms");
            bodyLabel.setText("Body Storage: " + bodyStorage.getSize() + " | Total: " + bodyStorage.getTotalProduced());
            motorLabel.setText("Motor Storage: " + motorStorage.getSize() + " | Total: " + motorStorage.getTotalProduced());
            accLabel.setText("Acc Storage: " + accStorage.getSize() + " | Total: " + accStorage.getTotalProduced());
            autoLabel.setText("Auto Storage: " + autoStorage.getSize() + " | Total: " + autoStorage.getTotalProduced());
            poolLabel.setText("Tasks in Queue: " + threadPool.getQueueSize());
            totalProducedLabel.setText("Total Produced: " + (bodyStorage.getTotalProduced() + motorStorage.getTotalProduced() + accStorage.getTotalProduced() + autoStorage.getTotalProduced()));
        });
        timer.start();
        setVisible(true);
    }

    private <T> JPanel createControlPanel(String name, List<T> workers, BiConsumer<T, Integer> action) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(name));
        JSlider slider = new JSlider(100, 5000, 1000);
        slider.addChangeListener(e -> {
            for (T worker : workers) action.accept(worker, slider.getValue());
        });
        panel.add(slider);
        return panel;
    }
    
    interface BiConsumer<T, U> { void accept(T t, U u); }
}