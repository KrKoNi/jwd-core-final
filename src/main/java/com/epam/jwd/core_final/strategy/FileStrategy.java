package com.epam.jwd.core_final.strategy;


import java.io.IOException;

public interface FileStrategy {
    void read(String path) throws IOException;
}
