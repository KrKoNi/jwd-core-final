package com.epam.jwd.core_final.strategy.impl;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.exception.EntityDuplicateException;
import com.epam.jwd.core_final.service.impl.SpaceshipServiceImpl;
import com.epam.jwd.core_final.strategy.FileStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MultilineFileStrategy implements FileStrategy {

    private MultilineFileStrategy() {

    }

    private final static MultilineFileStrategy INSTANCE = new MultilineFileStrategy();

    public static MultilineFileStrategy getInstance() {
        return INSTANCE;
    }

    private static final Logger logger = LoggerFactory.getLogger(MultilineFileStrategy.class);

    @Override
    public void read(String path) throws IOException {

        String[] separatedCrewInput = Stream.of(Files
                .readString(Path.of(path)))
                .map(str -> str.split("\\r?\\n"))
                .findAny()
                .get();

        for (String s : separatedCrewInput) {
            if (s.startsWith("#")) continue;
            String[] temp = s.split(";");
            String name = temp[0];
            Long distance = Long.valueOf(temp[1]);
            String stringMap = temp[2];
            String[] separatedMap = stringMap.split("[{}:,]");

            Map<Role, Short> crewMap = new HashMap<>();
            for (int i = 1; i < separatedMap.length; i+=2) {
                crewMap.put(Role.resolveRoleById(Long.valueOf(separatedMap[i])), Short.valueOf(separatedMap[i+1]));
            }

            try {
                SpaceshipServiceImpl.getInstance().createSpaceship(name, distance, crewMap);
                logger.info("Spaceship{" + name + ";" + distance + "} successfully added");
            } catch (EntityDuplicateException e) {
                logger.error("Spaceship " + name + " already exists");
            }
        }
    }
}
