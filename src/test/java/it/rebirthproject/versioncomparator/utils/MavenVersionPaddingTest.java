/*
 * Copyright (C) 2024/2025 Andrea Paternesi Rebirth project
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
package it.rebirthproject.versioncomparator.utils;

import it.rebirthproject.versioncomparator.version.PaddedLists;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class MavenVersionPaddingTest {

    @ParameterizedTest
    @MethodSource("versionListProvider")
    public void compareTest(List<String> version1, List<String> version2, PaddedLists expectedOutput) {
        MavenRulesVersionPadder mavenVersionPadder = new MavenRulesVersionPadder();
        PaddedLists result = mavenVersionPadder.padShorterVersion(version1, version2);
        assertEquals(expectedOutput.getVersion1(), result.getVersion1());
        assertEquals(expectedOutput.getVersion2(), result.getVersion2());
    }

    private static Stream<Arguments> versionListProvider() {
        return Stream.of(
                Arguments.of(Arrays.asList("1", "-", "cep", "-", "foo", "-", "2", "-", "zap"), Arrays.asList("1", ".", "2", "-", "rc"), new PaddedLists(Arrays.asList("1", "-", "cep", "-", "foo", "-", "2", "-", "zap"), Arrays.asList("1", ".", "2", "-", "rc", "-", "0", "-", ""))),
                Arguments.of(Arrays.asList("1", "-", "cep", "-", "foo", "-", "2", "-", "zap"), Arrays.asList("2"), new PaddedLists(Arrays.asList("1", "-", "cep", "-", "foo", "-", "2", "-", "zap"), Arrays.asList("2", "-", "", "-", "", "-", "0", "-", ""))),
                Arguments.of(Arrays.asList("2"), Arrays.asList("1", "-", "cep", "-", "foo", "-", "2", "-", "zap"), new PaddedLists(Arrays.asList("2", "-", "", "-", "", "-", "0", "-", ""), Arrays.asList("1", "-", "cep", "-", "foo", "-", "2", "-", "zap")))
        );
    }
}
