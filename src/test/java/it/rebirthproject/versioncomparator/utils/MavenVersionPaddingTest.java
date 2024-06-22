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
package it.rebirthproject.versioncomparator.utils;

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
    public void compareTest(List<String> version1, List<String> version2, List<String> expectedOutput) {
        MavenVersionPadder xx = new MavenVersionPadder();
        List<String> result = xx.padShorterVersion(version1, version2);
        assertEquals(expectedOutput, result);
    }

    private static Stream<Arguments> versionListProvider() {
        return Stream.of(
                Arguments.of(Arrays.asList("1", "-", "cep", "-", "foo", "-", "2", "-", "zap"), Arrays.asList("1", ".", "2", "-", "rc"), Arrays.asList("1", ".", "2", "-", "rc", "-", "0", "-", "")),
                Arguments.of(Arrays.asList("1", "-", "cep", "-", "foo", "-", "2", "-", "zap"), Arrays.asList("2"), Arrays.asList("2", "-", "", "-", "", "-", "0", "-", ""))
        );
    }
}
