package com.epam.jwd.core_final.strategy.impl;

import com.epam.jwd.core_final.strategy.AbstractFile;
import com.epam.jwd.core_final.strategy.FileStrategy;

public class SpaceshipFile extends AbstractFile {

    public SpaceshipFile(String path) {
        super(MultilineFileStrategy.getInstance(), path);
    }
}
