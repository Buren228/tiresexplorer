package ru.tiresexplorer.tiresexplorerservice.shedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.tiresexplorer.tiresexplorerservice.DataFetcher;

import java.time.LocalDateTime;

@Service
public class StockUpdater {

    private final DataFetcher dataFetcher;

    @Autowired
    public StockUpdater(DataFetcher dataFetcher) {
        this.dataFetcher = dataFetcher;
    }

    @Scheduled(fixedRate = 1800000)
    public void executeTask() {
        // Ваш код, который будет выполняться каждые 30 минут
        dataFetcher.fetchData();
        System.out.println("Выгрузка произведена" +  LocalDateTime.now());
    }
}
