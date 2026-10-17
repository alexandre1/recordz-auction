package com.example.recordz.service;

import java.util.Locale;

@FunctionalInterface
public interface CurrentLocaleProvider {
    Locale get();
}