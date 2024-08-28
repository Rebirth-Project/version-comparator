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

import it.rebirthproject.versioncomparator.parser.MavenRulesVersionParser;
import it.rebirthproject.versioncomparator.parser.StrictSemanticVersionParser;
import it.rebirthproject.versioncomparator.utils.MavenRulesVersionPadder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

// Based on examples provided by https://maven.apache.org/pom.html#Version_Order_Specification
// Based on examples provided by https://docs.oracle.com/middleware/1212/core/MAVEN/maven_version.htm#MAVEN400
public class MavenRulesVersionComparatorTest {

    private static final MavenRulesVersionComparator mavenVersionComparator = new MavenRulesVersionComparator(new MavenStandardVersionComparator(new StrictSemanticVersionParser()), new MavenRulesVersionParser(), new MavenRulesVersionPadder());

    @ParameterizedTest
    @CsvSource({
        "1.0-aaa, 1.0-aab, -1",
        "1.0-aab, 1.0-aaa, 1",
        "1.0-alpha, 1.0-aaa, -1",
        "1.0-aaa, 1.0-alpha, 1",
        "1.0.alpha, 1.0-aaa, 1",
        "1.0-aaa, 1.0.alpha, -1",
        "1.0-alpha, 1.0.aaa, -1",
        "1.0.aaa, 1.0-alpha, 1",
        "1.0, 1.0.aaa, -1",
        "1.0.aaa, 1.0, 1"
    })

    public void comapreQualifierOrder(String version1, String version2, int expectedComparisonResult) {
        int actualComparisonResult = mavenVersionComparator.compare(version1, version2);
        assertEquals(expectedComparisonResult, actualComparisonResult);
    }

    @ParameterizedTest
    @CsvSource({
        "1, 1.1, -1",
        "1-1, 1.1, -1",
        "1-snapshot, 1, -1",
        "1-snapshot, 1-sp, -1",
        "1-foo2, 1-foo10, -1",
        "1.foo, 1-foo, 0",
        "1.foo, 1-1, -1",
        "1.foo, 1.1, -1",
        "1.0-rc, 1.0-cr,0",
        "1.0-cr, 1.0-rc,0",
        "1.ga, 1-ga, 0",
        "1-ga, 1.ga, 0",
        "1.ga, 1-0, 0",
        "1.0, 1-0, 0",
        "1.0, 1, 0",
        "1-0, 1, 0",
        "1, 1-sp, -1",
        "1-sp, 1-ga, 1",
        "1-sp.1, 1-ga.1, 1",
        "1-sp-1, 1-ga-1, -1",
        "1.0-sp, 1.0-ga, 1",
        "1.0-sp.1, 1.0-ga.1, 1",
        "1-a1, 1-alpha-1, 0",
        "1-alpha-1, 1-a1, 0",
        "1-1, 1-ga-1, 0",
        "1-final, 1, 0",
        "1, 1-final, 0",
        "1-1.foo-bar1baz-.1, 1-1.foo-bar-1-baz-0.1, 0",
        "1-1.foo-bar-1-baz-0.1, 1-1.foo-bar1baz-.1, 0",
        "1-1.foo-bar1baz-.1, 1-1.bar, 1"
    })
    public void compareMavenVersionDocumentationExamples(String version1, String version2, int expectedComparisonResult) {
        int actualComparisonResult = mavenVersionComparator.compare(version1, version2);
        assertEquals(expectedComparisonResult, actualComparisonResult);
    }

    @ParameterizedTest
    @CsvSource({
        "5, 5-SNAPSHOT, 1",
        "5-SNAPSHOT, 5, -1",
        "5, 5, 0",
        "5, 6,-1",
        "6, 5, 1",
        "1.2, 1.0, 1",
        "1.2-SNAPSHOT, 1.0, 1",
        "1.2, 1.0-SNAPSHOT, 1",
        "1.2-SNAPSHOT, 1.0-SNAPSHOT, 1",
        "1.0, 1.2, -1",
        "1.0-SNAPSHOT, 1.2, -1",
        "1.0, 1.2-SNAPSHOT, -1",
        "1.0-SNAPSHOT, 1.2-SNAPSHOT, -1",
        "1.0, 1.0, 0",
        "1.0-SNAPSHOT, 1.0, -1",
        "1.0, 1.0-SNAPSHOT, 1",
        "1.0-SNAPSHOT, 1.0-SNAPSHOT,0",
        "1.2-12, 1.1, 1",
        "1.2-12, 1.1-33, 1",
        "1.2-12, 1.2-11, 1",
        "1.1, 1.2-12, -1",
        "1.2-11, 1.2-12, -1",
        "1.1-33, 1.2-12, -1",
        "1.2-alpha, 1.1, 1",
        "1.2-alpha, 1.1-beta, 1",
        "1.2-beta, 1.2-alpha, 1",
        "1.1, 1.2-alpha, -1",
        "1.2-alpha, 1.2-alpha, 0",
        "1.1-beta, 1.2-alpha, -1",
        "1.0.0, 0.0.9, 1",
        "1.0.0, 0.99.9, 1",
        "1.0.0, 0.99.9-alpha, 1",
        "1.0.0, 0.99.9-gamma, 1",
        "1.0.0-alpha, 0.99.9-alpha, 1",
        "1.0.0-alpha, 0.99.9-gamma, 1",
        "1.0.0-gamma, 0.99.9-alpha, 1",
        "1.0.0-gamma, 0.99.9-gamma, 1",
        "0.0.9, 1.0.9, -1",
        "0.99.9, 1.99.9, -1",
        "0.99.9, 1.99.9-alpha, -1",
        "0.99.9, 1.99.9-gamma, -1",
        "0.99.0-alpha, 1.99.9-alpha, -1",
        "0.99.0-alpha, 1.99.9-gamma, -1",
        "0.99.0-gamma, 1.99.9-alpha, -1",
        "0.99.0-gamma, 1.99.9-gamma, -1",
        "1.0.10.1, 1.0.1.0, 1",
        "1.0.10.2, 1.0.10.1, 1",
        "1.0.10.2, 1.0.9.3, 1",
        "1.2-beta-2, 1.2-alpha-6, 1",
        "1.2.3, 1.3.2, -1",
        "0.2.3, 1.3.2, -1",
        "1.2.3, 0.3.2, 1",
        "1.2.3-SNAPSHOT, 1.2.3, -1",
        "1.2.3-SNAPSHOT, 1.2.3-SNAPSHOT, 0",
        "1.2.3, 1.2.3-SNAPSHOT, 1",
        "1.0.10-1, 1.0.1-0, 1",
        "1.0.10-2, 1.0.10-1, 1",
        "1.0.9-3, 1.0.10-2, -1",
        "1.0.9-3, 1.0.1-0, 1",
        "3.3.0-I20070605-10, 3.3.0, 1",
        "3.3.0, 3.3.0-I20070605-10, -1",
        "0.99.9, 1.99.9-gamma-snapshot, -1"
    })
    public void compareMavenVersionOtherExamples(String version1, String version2, int expectedComparisonResult) {
        int actualComparisonResult = mavenVersionComparator.compare(version1, version2);
        assertEquals(expectedComparisonResult, actualComparisonResult);
    }
}
