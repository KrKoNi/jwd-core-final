package com.epam.jwd.core_final.strategy;

import com.epam.jwd.core_final.strategy.FileStrategy;

import java.io.IOException;

public abstract class AbstractFile {

    private final FileStrategy strategy;
    private final String path;

    protected AbstractFile(FileStrategy strategy, String path) {
        this.strategy = strategy;
        this.path = path;
    }

    public void read() throws IOException {
        strategy.read(path);
    }
}
