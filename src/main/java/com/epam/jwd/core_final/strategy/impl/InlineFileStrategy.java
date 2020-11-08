package com.epam.jwd.core_final.strategy.impl;

import com.epam.jwd.core_final.context.ApplicationMenu;
import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.exception.EntityDuplicateException;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.strategy.FileStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

public class InlineFileStrategy implements FileStrategy {

    private InlineFileStrategy() {

    }

    private final static InlineFileStrategy INSTANCE = new InlineFileStrategy();

    public static InlineFileStrategy getInstance() {
        return INSTANCE;
    }

    private static final Logger logger = LoggerFactory.getLogger(InlineFileStrategy.class);

    @Override
    public void read(String path) throws IOException{


        Optional<String> optionalFilteredCrewInput = Files.lines(Paths.get(path), StandardCharsets.UTF_8)
                    .filter(s -> !s.startsWith("#"))
                    .findAny();

        String[] separatedCrewInput = optionalFilteredCrewInput.stream()
                .map(str -> str.split(";"))
                .findAny()
                .get();

        Arrays.stream(separatedCrewInput)
                .map(s -> s.split(","))
                .forEachOrdered(temp -> {
                    Role role = Role.resolveRoleById(Long.valueOf(temp[0]));
                    String name = temp[1];
                    Rank rank = Rank.resolveRankById(Long.valueOf(temp[2]));
                    try {
                        CrewServiceImpl.getInstance().createCrewMember(role, name, rank);
                        logger.info("CrewMember{" + name + ";" + role + ";" + rank + "} successfully added");
                    } catch (EntityDuplicateException e) {
                        logger.error("CrewMember " + name + " already exists");
                    }
                });
    }
}
