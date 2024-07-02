/*
 * Copyright (C) 2024 Andrea Paternesi Rebirth project
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

import it.rebirthproject.versioncomparator.version.Version;
import java.util.Arrays;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

public class MavenVersionParserTest {

    private final MavenVersionParser parser = new MavenVersionParser();

    @ParameterizedTest
    @MethodSource("versionListProvider")
    public void validVersionTest(String version1, Version expectedComparisonResult) {
        Version actualVersionResult = parser.parseVersion(version1);
        assertEquals(expectedComparisonResult.getTokenList(), actualVersionResult.getTokenList());
    }

    @ParameterizedTest
    @CsvSource({
        "a",
        "1.02",
        "001",
        "1&-ok",
        "1-beta.09"
    })
    public void invalidVersionsTest(String invalidVersion) {
        assertThrows(IllegalArgumentException.class, () -> {
            parser.parseVersion(invalidVersion);
        });
    }

    private static Stream<Arguments> versionListProvider() {
        return Stream.of(
                Arguments.of("0", new Version(Arrays.asList("0"))),
                Arguments.of("1.-cep-.200-foo2.0_zap", new Version(Arrays.asList("1", "-", "cep", "-", "", ".", "200", "-", "foo", "-", "2", "-", "zap"))),
                Arguments.of("1.0.0", new Version(Arrays.asList("1"))),
                Arguments.of("1.0.2", new Version(Arrays.asList("1", ".", "0", ".", "2"))),
                Arguments.of("1.5.2-final", new Version(Arrays.asList("1", ".", "5", ".", "2"))),
                Arguments.of("1.5.2-ga", new Version(Arrays.asList("1", ".", "5", ".", "2"))),
                Arguments.of("1.5.2-.--top", new Version(Arrays.asList("1", ".", "5", ".", "2", "-", "top"))),
                Arguments.of("1-sp.1", new Version(Arrays.asList("1", "-", "sp", ".", "1"))),
                Arguments.of("1-sp-1", new Version(Arrays.asList("1", "-", "sp", "-", "1"))),
                Arguments.of("1-ga.1", new Version(Arrays.asList("1", "-", "ga", ".", "1"))),
                Arguments.of("1-ga-1", new Version(Arrays.asList("1", "-", "1"))),
                Arguments.of("1-1.foo-bar1baz-.1", new Version(Arrays.asList("1", "-", "1", ".", "foo", "-", "bar", "-", "1", "-", "baz", "-", "", ".", "1"))),
                Arguments.of("1-pro-1", new Version(Arrays.asList("1", "-", "pro", "-", "1"))),
                Arguments.of("1-0.1", new Version(Arrays.asList("1", "-", "0", ".", "1"))),
                Arguments.of("1-0-1", new Version(Arrays.asList("1", "-", "1"))),
                Arguments.of("1-0.1-1", new Version(Arrays.asList("1", "-", "0", ".", "1", "-", "1"))),
                Arguments.of("1-0-1-1", new Version(Arrays.asList("1",  "-", "1", "-", "1")))
        );
    }
}
