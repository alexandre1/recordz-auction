package com.example.recordz.config;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;

@PWA(
        name = "Recordz",
        shortName = "Recordz",
        offlinePath = "offline.html",
        offlineResources = { "images/offline.png" }
)
public class AppShell implements AppShellConfigurator {
}