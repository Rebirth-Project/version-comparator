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

public class MinimalVersionParserTest {

    private final MinimalVersionParser parser = new MinimalVersionParser();

    @ParameterizedTest
    @CsvSource({
        "1.2",
        "1.0.0",
        "1.0.1",
        "1.0.99999",
        "1500.0.10",
        "1.2.66",
        "1.5555.0",
        "1.2.0",
        "111.27.0 ",
        "13.40.888",
        "0.1.12345"
    })
    public void validVersionTest(String version) {
        parser.parseVersion(version);
    }

    @ParameterizedTest
    @CsvSource({
        "01.45.0",
        "1.02.0",
        "1.2.03",
        "1.2.03",
        "1.2.0-SNAPSHOT",
        "1.2.0FINAL",
        "1.2.0STABLE",
        "1.2000.03",
        "1.255.03",
        "0.1.1234567890123456789"
    })
    public void invalidVersionsTest(String invalidVersion) {

        assertThrows(IllegalArgumentException.class, () -> {
            parser.parseVersion(invalidVersion);
        });
    }
}
