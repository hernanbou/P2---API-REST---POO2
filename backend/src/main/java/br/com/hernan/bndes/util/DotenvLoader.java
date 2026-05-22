package br.com.hernan.bndes.util;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class DotenvLoader {

    private static final Logger log = LoggerFactory.getLogger(DotenvLoader.class);

    private DotenvLoader() {
    }

    public static void load() {
        List<Path> candidates = List.of(Path.of(".env"), Path.of("../.env"));

        for (Path candidate : candidates) {
            if (Files.exists(candidate)) {
                loadFrom(candidate.toAbsolutePath().normalize().getParent());
                return;
            }
        }

        log.warn("Arquivo .env nao encontrado na pasta atual nem em ../.env. Variaveis do sistema serao usadas.");
    }

    private static void loadFrom(Path directory) {
        Dotenv dotenv = Dotenv.configure()
                .directory(directory.toString())
                .filename(".env")
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        for (DotenvEntry entry : dotenv.entries()) {
            if (System.getenv(entry.getKey()) == null && System.getProperty(entry.getKey()) == null) {
                System.setProperty(entry.getKey(), entry.getValue());
            }
        }

        log.info("Variaveis de ambiente carregadas de {}", directory.resolve(".env"));
    }
}
