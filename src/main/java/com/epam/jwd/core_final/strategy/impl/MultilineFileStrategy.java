package com.epam.jwd.core_final.strategy.impl;

import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.exception.EntityDuplicateException;
import com.epam.jwd.core_final.service.impl.SpaceshipServiceImpl;
import com.epam.jwd.core_final.strategy.FileStrategy;
import com.epam.jwd.core_final.util.PropertyReaderUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MultilineFileStrategy implements FileStrategy {
    @Override
    public void read() {
        String path = "src/main/resources/"
                + PropertyReaderUtil.getProperties().getProperty("inputRootDir")
                + "/"
                + PropertyReaderUtil.getProperties().getProperty("spaceshipsFileName");
        try {
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
                } catch (EntityDuplicateException e) {
                    System.out.println(e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
