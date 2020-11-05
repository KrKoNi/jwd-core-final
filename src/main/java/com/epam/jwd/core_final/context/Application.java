package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.exception.InvalidStateException;

import java.util.function.Supplier;

public interface Application {

    static ApplicationMenu start() throws InvalidStateException {
        final Supplier<ApplicationContext> applicationContextSupplier = null;
        //NassaContext::getInstance; // todo
        // final NassaContext nassaContext = NassaContext.getInstance();
        NassaContext.getInstance().init();
        //return applicationContextSupplier::get;
        return null;
    }
}
