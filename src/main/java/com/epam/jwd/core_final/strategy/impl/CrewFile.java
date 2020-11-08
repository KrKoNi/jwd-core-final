package com.epam.jwd.core_final.strategy.impl;

import com.epam.jwd.core_final.strategy.AbstractFile;

public class CrewFile extends AbstractFile {
    public CrewFile(String path) {
        super(InlineFileStrategy.getInstance(), path);
    }
}
