/*
 * Copyright (C) 2024/2025 Andrea Paternesi Rebirth project
 * Modifications copyright (C) 2021/2024/2025 Matteo Veroni Rebirth project
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

import it.rebirthproject.versioncomparator.parser.MinimalVersionParser;
import it.rebirthproject.versioncomparator.parser.RelaxedSemanticVersionParser;
import it.rebirthproject.versioncomparator.parser.StrictSemanticVersionParser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class StandardVersionComparatorTest {

    @ParameterizedTest
    @CsvSource({
        "1.0.0, 1.0.0, 0",
        "1.0.1, 1.0.0, 1",
        "1.2.0, 1.0.0, 1",        
        "1.0.0, 1.0.1, -1",
        "1.0.0, 1.2.0, -1"                
    })
    public void compareMinimalVersionTest(String version1, String version2, int expectedComparisonResult) {
        int actualComparisonResult = new StandardVersionComparator(new MinimalVersionParser()).compare(version1, version2);
        assertEquals(expectedComparisonResult, actualComparisonResult);
    }
    
    @ParameterizedTest
    @CsvSource({
        "1.0.0, 1.0.0, 0",
        "1.0.1, 1.0.0, 1",
        "1.2.0, 1.0.0, 1",
        //Note this one. For Strict we just make a literal comparison
        "1.0.0-SNAPSHOT, 1.0.0-RC, -1",
        "1.0.0, 1.0.1, -1",
        "1.0.0, 1.2.0, -1",
        "1.0.0-SNAPSHOT, 1.0.0, -1",
        "1.0.0, 1.0.0-SNAPSHOT, 1",
        "1.2.0-BETA, 1.2.0, -1",
        "1.2.0, 1.2.0-BETA, 1",
        "1.2.0, 1.2.0FINAL, -1",
        "1.2.0STABLE, 1.2.0, 1",
        "1.2.0stable, 1.2.0, 1",
        "1.2.0, 1.2.0FINal, -1",
        "111.27.0, 13.40.888FINal, 1",
        "111.27.0, 111.27.0FINal-TTT, -1"    
    })
    public void compareRelaxedSemanticVersionTest(String version1, String version2, int expectedComparisonResult) {
        int actualComparisonResult = new StandardVersionComparator(new RelaxedSemanticVersionParser()).compare(version1, version2);
        assertEquals(expectedComparisonResult, actualComparisonResult);
    }
    
    @ParameterizedTest
    @CsvSource({
        "1.0.0, 1.0.0, 0",
        "1.0.1, 1.0.0, 1",
        "1.2.0, 1.0.0, 1",
        "1.0.0-SNAPSHOT, 1.0.0-RC, 1",
        "1.0.0, 1.0.1, -1",
        "1.0.0, 1.2.0, -1",
        "1.0.0-SNAPSHOT, 1.0.0, -1",
        "1.0.0, 1.0.0-SNAPSHOT, 1",
        "1.2.0-BETA, 1.2.0, -1",
        "1.2.0, 1.2.0-BETA, 1",
        "1.2.0, 1.2.0-FINAL, -1",
        "1.2.0-STABLE, 1.2.0, 1",
        "1.2.0-stable, 1.2.0, 1",
        "1.2.0, 1.2.0-FINal, -1",
        "111.27.0, 13.40.888-FINal, 1",
        "111.27.0, 111.27.0-FINal-TTT, -1"    
    })
    public void compareStrictSemanticVersionTest(String version1, String version2, int expectedComparisonResult) {
        int actualComparisonResult = new StandardVersionComparator(new StrictSemanticVersionParser()).compare(version1, version2);
        assertEquals(expectedComparisonResult, actualComparisonResult);
    }
}
