package br.com.saulocn.goldenraspberryawards;

import br.com.saulocn.goldenraspberryawards.model.Movie;
import br.com.saulocn.goldenraspberryawards.service.MovieService;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@ApplicationScoped
public class AppLifecycleBean {

    @Inject
    private MovieService movieService;

    private static final Logger LOGGER = Logger.getLogger("AppLifecycleBean");

    @ConfigProperty(name = "file-path", defaultValue = "Movielist.csv")
    private String filePath;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .withIgnoreQuotations(true)
                .build();
        try {
            LOGGER.info("Reading file: " + filePath);
            InputStream inputStream = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(filePath);
            Reader reader = new InputStreamReader(Objects.requireNonNull(inputStream));
            CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withSkipLines(1)
                    .withCSVParser(parser)
                    .build();
            List<String[]> linhas = csvReader.readAll();
            linhas.stream().map(Movie::build).forEach(movieService::saveMovie);
            csvReader.close();
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("The application started...");
    }
}
