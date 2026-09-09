package com.example.recordz;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.scheduling.annotation.EnableScheduling;

@StyleSheet(Lumo.STYLESHEET)
@StyleSheet(Lumo.COMPACT_STYLESHEET)
@SpringBootApplication
@EnableVaadin({"org.vaadin.stefan", "com.example.recordz"})
@EnableScheduling
public class RecordzApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(RecordzApplication.class, args);
    }
}
