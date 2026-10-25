package com.mymonkmindset.openingstock.database;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutors {
    public static final ExecutorService diskIO = Executors.newSingleThreadExecutor();
}
