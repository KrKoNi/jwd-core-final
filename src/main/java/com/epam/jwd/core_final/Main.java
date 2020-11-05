package com.epam.jwd.core_final;

import com.epam.jwd.core_final.context.Application;
import com.epam.jwd.core_final.context.ApplicationMenu;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.strategy.impl.InlineFileStrategy;
import com.epam.jwd.core_final.strategy.impl.MultilineFileStrategy;
import com.epam.jwd.core_final.util.PropertyReaderUtil;

public class Main {
    public static void main(String[] args) throws InvalidStateException {
        PropertyReaderUtil.loadProperties();

        Application.start();
    }
}