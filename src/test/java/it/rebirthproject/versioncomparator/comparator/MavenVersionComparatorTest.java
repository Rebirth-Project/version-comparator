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
package it.rebirthproject.versioncomparator.comparator;

import it.rebirthproject.versioncomparator.parser.MavenVersionParser;
import it.rebirthproject.versioncomparator.utils.MavenVersionPadder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class MavenVersionComparatorTest {

    //TODO add more complex cases
    @ParameterizedTest
    @CsvSource({
        "1, 1.1, -1",
        "1-snapshot, 1, -1",
        "1-snapshot, 1-sp, -1",
        "1-foo2, 1-foo10, -1",        
        "1.foo, 1-foo, 0",
        "1.foo, 1-1, -1",
        "1.foo, 1.1, -1",
        "1.ga, 1-ga, 0",
        "1.ga, 1-0, 0",
        "1.0, 1-0, 0",
        "1.0, 1, 0",
        "1-0, 1, 0",        
        "1-sp, 1-ga, 1",
        "1-sp.1, 1-ga.1, 1",        
        "1-sp-1, 1-ga-1, -1",
        "1-a1, 1-alpha-1, 0"        
    })
    public void compareStrictSemanticVersionTest(String version1, String version2, int expectedComparisonResult) {
        int actualComparisonResult = new MavenVersionComparator(new MavenVersionParser(), new MavenVersionPadder()).compare(version1, version2);
        assertEquals(expectedComparisonResult, actualComparisonResult);
    }
}
