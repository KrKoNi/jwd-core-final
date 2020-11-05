package com.epam.jwd.core_final.strategy.impl;

import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.strategy.FileStrategy;
import com.epam.jwd.core_final.util.PropertyReaderUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

public class InlineFileStrategy implements FileStrategy {
    @Override
    public void read() {
        String path = "src/main/resources/"
                + PropertyReaderUtil.getProperties().getProperty("inputRootDir")
                + "/"
                + PropertyReaderUtil.getProperties().getProperty("crewFileName");
        try {
            Optional<String> optionalFilteredCrewInput = Files.lines(Paths.get(path), StandardCharsets.UTF_8)
                    .filter(s -> !s.startsWith("#"))
                    .findAny();


            String[] separatedCrewInput = optionalFilteredCrewInput.stream()
                    .map(str -> str.split(";"))
                    .findAny()
                    .get();

            Arrays.stream(separatedCrewInput)
                    .map(s -> s.split(",")).forEachOrdered(temp -> {
                        Role role = Role.resolveRoleById(Long.valueOf(temp[0]));
                        String name = temp[1];
                        Rank rank = Rank.resolveRankById(Long.valueOf(temp[2]));
                        new CrewServiceImpl().createCrewMember(role, name, rank);
                    });


        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
