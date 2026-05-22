package br.com.hernan.bndes;

import br.com.hernan.bndes.util.DotenvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PainelPropostasBndesApplication {

    public static void main(String[] args) {
        DotenvLoader.load();
        SpringApplication.run(PainelPropostasBndesApplication.class, args);
    }
}
