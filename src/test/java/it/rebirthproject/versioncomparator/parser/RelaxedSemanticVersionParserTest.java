/*
 * Copyright (C) 2024 Andrea Paternesi Rebirth project
 * Modifications copyright (C) 2021/2024 Matteo Veroni Rebirth project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.rebirthproject.versioncomparator.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class RelaxedSemanticVersionParserTest {

    private final RelaxedSemanticVersionParser parser = new RelaxedSemanticVersionParser();

    @ParameterizedTest
    @CsvSource({
        "1.0",
        "0.0.4",
        "1.2.3",
        "10.20.30",
        "1.0.0-alpha",
        "1.0.0-beta",
        "1.0.0-alpha.1",
        "1.0.0-alpha.0valid",
        "1.2.3-beta",
        "10.2.3-DEV-SNAPSHOT",
        "1.2.3-SNAPSHOT-123",
        "1.0.0",
        "2.0.0",
        "1.1.7",
        "2.0.1-alpha.1227",
        "9999999999.9999999999.9999999999",
        "1.2.3.DEV",
        "1.2.3DEV",
        "1.2.3.FINAL",
        "1.2-RC-SNAPSHOFT",
        "1.2-SNAPSHOT"
    })
    public void validVersionTest(String version) {
        parser.parseVersion(version);
    }

    @ParameterizedTest
    @CsvSource({
        "1",
        "1.2.3-0123",
        "1.2.3-0123.0123",
        "1.1.2+.123",
        "+invalid",
        "-invalid",
        "-invalid+invalid",
        "-invalid.01",
        "alpha",
        "alpha.beta",
        "alpha.beta.1",
        "alpha.1",
        "alpha+beta",
        "alpha_beta",
        "alpha.",
        "alpha..",
        "beta",
        "1.0.0-alpha_beta",
        "-alpha.",
        "1.0.0-alpha..",
        "1.0.0-alpha..1",
        "1.0.0-alpha...1",
        "1.0.0-alpha....1",
        "1.0.0-alpha.....1",
        "1.0.0-alpha......1",
        "1.0.0-alpha.......1",
        "01.1.1",
        "1.01.1",
        "1.1.01",
        "1.2.31.2.3----RC-SNAPSHOT.12.09.1--..12+788",
        "-1.0.3-gamma+b7718",
        "+justmeta",
        "9.8.7+meta+meta",
        "9.8.7-whatever+meta+meta",
        "99999999999999999999999.999999999999999999.99999999999999999----RC-SNAPSHOT.12.09.1--------------------------------..12",
        "9999999999999999.9999999999999999.999999999999999",
        "1.0.0-alpha.beta",
        "1.0.0-alpha.beta.1",
        "1.2-RC-SNAPSHOT"
    })
    public void invalidVersionsTest(String invalidVersion) {
        assertThrows(IllegalArgumentException.class, () -> {
            parser.parseVersion(invalidVersion);
        });
    }
}
