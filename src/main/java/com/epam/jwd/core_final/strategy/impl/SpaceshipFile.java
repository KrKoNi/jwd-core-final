package com.epam.jwd.core_final.strategy.impl;

import com.epam.jwd.core_final.strategy.AbstractFile;

public class SpaceshipFile extends AbstractFile {

    public SpaceshipFile(String path) {
        super(MultilineFileStrategy.getInstance(), path);
    }
}
