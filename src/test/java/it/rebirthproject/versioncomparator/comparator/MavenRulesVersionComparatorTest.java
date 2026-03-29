/*
 * Copyright (C) 2024/2026 Andrea Paternesi Rebirth project
 * Modifications copyright (C) 2021/2024/2026 Matteo Veroni Rebirth project
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
import it.rebirthproject.versioncomparator.utils.MavenRulesVersionPadder;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

// Based on examples provided by https://maven.apache.org/pom.html#Version_Order_Specification
// Based on examples provided by https://docs.oracle.com/middleware/1212/core/MAVEN/maven_version.htm#MAVEN400
public class MavenRulesVersionComparatorTest {

    private static final MavenRulesVersionComparator mavenVersionComparator = new MavenRulesVersionComparator(new MavenRulesVersionParser(), new MavenRulesVersionPadder());

    @ParameterizedTest
    @CsvSource({
        "1.0-aaa, 1.0-aab, -1",
        "1.0-aab, 1.0-aaa, 1",
        "1.0-alpha, 1.0-aaa, -1",
        "1.0-aaa, 1.0-alpha, 1",
        "1.0.alpha, 1.0-aaa, -1",
        "1.0-aaa, 1.0.alpha, 1",
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
        "2.6.1.Final, 2.6.1.Final, 0",
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

    @ParameterizedTest
    @CsvSource({
        "1-foo, 1-FOO, 0",
        "1-zebra, 1-AARDVARK, 1",
        "1-é, 1-É, 0"
    })
    public void compareMavenVersionCaseInsensitiveQualifierRules(String version1, String version2, int expectedComparisonResult) {
        int actualComparisonResult = mavenVersionComparator.compare(version1, version2);
        assertEquals(expectedComparisonResult, actualComparisonResult);
    }

    @ParameterizedTest
    @CsvSource({
        "2147483648, 2147483647",
        "1-2147483648, 1-2147483647",
        "1.2147483648, 1.2147483647"
    })
    public void compareMavenVersionShouldNotThrowWithLargeNumericTokens(String version1, String version2) {
        int actualComparisonResult = mavenVersionComparator.compare(version1, version2);
        assertEquals(1, actualComparisonResult);
    }

    @ParameterizedTest
    @CsvSource({
        "1-alpha2snapshot,1-alpha2",
        "1-alpha2,1-alpha-123",
        "1-alpha-123,1-beta-2",
        "1-beta-2,1-beta123",
        "1-beta123,1-m2",
        "1-m2,1-m11",
        "1-m11,1-rc",
        "1-rc,1-cr2",
        "1-cr2,1-rc123",
        "1-rc123,1-SNAPSHOT",
        "1-SNAPSHOT,1",
        "1,1-sp",
        "1-sp,1-sp2",
        "1-sp2,1-sp123",
        "1-sp123,1-abc",
        "1-abc,1-def",
        "1-def,1-pom-1",
        "1-pom-1,1-1-snapshot",
        "1-1-snapshot,1-1",
        "1-1,1-2",
        "1-2,1-123"
    })
    public void should_CompareMavenQualifierOrder_When_ComparableVersionCases(String lowerVersion, String higherVersion) {
        assertComparison(lowerVersion, higherVersion, -1);
    }

    @ParameterizedTest
    @CsvSource({
        "2.0,2.0.a",
        "2.0.a,2-1",
        "2-1,2.0.2",
        "2.0.2,2.0.123",
        "2.0.123,2.1.0",
        "2.1.0,2.1-a",
        "2.1-a,2.1b",
        "2.1b,2.1-c",
        "2.1-c,2.1-1",
        "2.1-1,2.1.0.1",
        "2.1.0.1,2.2",
        "2.2,2.123",
        "2.123,11.a2",
        "11.a2,11.a11",
        "11.a11,11.b2",
        "11.b2,11.b11",
        "11.b11,11.m2",
        "11.m2,11.m11",
        "11.m11,11",
        "11,11.a",
        "11.a,11b",
        "11b,11c",
        "11c,11m"
    })
    public void should_CompareMavenNumberOrder_When_ComparableVersionCases(String lowerVersion, String higherVersion) {
        assertComparison(lowerVersion, higherVersion, -1);
    }

    @ParameterizedTest
    @CsvSource({
        "1,1",
        "1,1.0",
        "1,1.0.0",
        "1.0,1.0.0",
        "1,1-0",
        "1,1.0-0",
        "1.0,1.0-0",
        "1a,1-a",
        "1a,1.0-a",
        "1a,1.0.0-a",
        "1.0a,1-a",
        "1.0.0a,1-a",
        "1x,1-x",
        "1x,1.0-x",
        "1x,1.0.0-x",
        "1.0x,1-x",
        "1.0.0x,1-x",
        "1ga,1",
        "1release,1",
        "1final,1",
        "1cr,1rc",
        "1a1,1-alpha-1",
        "1b2,1-beta-2",
        "1m3,1-milestone-3",
        "1X,1x",
        "1A,1a",
        "1B,1b",
        "1M,1m",
        "1Ga,1",
        "1GA,1",
        "1RELEASE,1",
        "1release,1",
        "1RELeaSE,1",
        "1Final,1",
        "1FinaL,1",
        "1FINAL,1",
        "1Cr,1Rc",
        "1cR,1rC",
        "1m3,1Milestone3",
        "1m3,1MileStone3",
        "1m3,1MILESTONE3"
    })
    public void should_CompareMavenEquality_When_ComparableVersionCases(String firstVersion, String secondVersion) {
        assertComparison(firstVersion, secondVersion, 0);
    }

    @ParameterizedTest
    @CsvSource({
        "1,2",
        "1.5,2",
        "1,2.5",
        "1.0,1.1",
        "1.1,1.2",
        "1.0.0,1.1",
        "1.0.1,1.1",
        "1.1,1.2.0",
        "1.0-alpha-1,1.0",
        "1.0-alpha-1,1.0-alpha-2",
        "1.0-alpha-1,1.0-beta-1",
        "1.0-beta-1,1.0-SNAPSHOT",
        "1.0-SNAPSHOT,1.0",
        "1.0-alpha-1-SNAPSHOT,1.0-alpha-1",
        "1.0,1.0-1",
        "1.0-1,1.0-2",
        "1.0.0,1.0-1",
        "2.0-1,2.0.1",
        "2.0.1-klm,2.0.1-lmn",
        "2.0.1,2.0.1-xyz",
        "2.0.1,2.0.1-123",
        "2.0.1-xyz,2.0.1-123"
    })
    public void should_CompareMavenOrder_When_ComparableVersionCases(String lowerVersion, String higherVersion) {
        assertComparison(lowerVersion, higherVersion, -1);
    }

    @ParameterizedTest
    @CsvSource({
        "6.1.0rc3,6.1.0",
        "6.1.0rc3,6.1H.5-beta",
        "6.1.0,6.1H.5-beta"
    })
    public void should_CompareMavenOrder_When_Mng5568Cases(String lowerVersion, String higherVersion) {
        assertComparison(lowerVersion, higherVersion, -1);
    }

    @ParameterizedTest
    @CsvSource({
        "20190126.230843,1234567890.12345",
        "1234567890.12345,123456789012345.1H.5-beta",
        "20190126.230843,123456789012345.1H.5-beta",
        "123456789012345.1H.5-beta,12345678901234567890.1H.5-beta",
        "1234567890.12345,12345678901234567890.1H.5-beta",
        "20190126.230843,12345678901234567890.1H.5-beta"
    })
    public void should_CompareMavenOrder_When_Mng6572Cases(String lowerVersion, String higherVersion) {
        assertComparison(lowerVersion, higherVersion, -1);
    }

    @ParameterizedTest
    @CsvSource({
        "0000000000000000001,1",
        "000000000000000001,1",
        "00000000000000001,1",
        "0000000000000001,1",
        "000000000000001,1",
        "00000000000001,1",
        "0000000000001,1",
        "000000000001,1",
        "00000000001,1",
        "0000000001,1",
        "000000001,1",
        "00000001,1",
        "0000001,1",
        "000001,1",
        "00001,1",
        "0001,1",
        "001,1",
        "01,1",
        "1,1"
    })
    public void should_CompareMavenEquality_When_LeadingZeroOneCases(String firstVersion, String secondVersion) {
        assertComparison(firstVersion, secondVersion, 0);
    }

    @ParameterizedTest
    @CsvSource({
        "0000000000000000000,0",
        "000000000000000000,0",
        "00000000000000000,0",
        "0000000000000000,0",
        "000000000000000,0",
        "00000000000000,0",
        "0000000000000,0",
        "000000000000,0",
        "00000000000,0",
        "0000000000,0",
        "000000000,0",
        "00000000,0",
        "0000000,0",
        "000000,0",
        "00000,0",
        "0000,0",
        "000,0",
        "00,0",
        "0,0"
    })
    public void should_CompareMavenEquality_When_LeadingZeroZeroCases(String firstVersion, String secondVersion) {
        assertComparison(firstVersion, secondVersion, 0);
    }

    @ParameterizedTest
    @CsvSource({
        "1-0.alpha,1",
        "1-0.beta,1",
        "1-0.alpha,1-0.beta"
    })
    public void should_CompareMavenOrder_When_Mng6964Cases(String lowerVersion, String higherVersion) {
        assertComparison(lowerVersion, higherVersion, -1);
    }

    @ParameterizedTest
    @CsvSource({
        "1.0.0.abc1,1.0.0-abc2",
        "1.0.0.alpha1,1.0.0-alpha2",
        "1.0.0.a1,1.0.0-a2",
        "1.0.0.beta1,1.0.0-beta2",
        "1.0.0.b1,1.0.0-b2",
        "1.0.0.def1,1.0.0-def2",
        "1.0.0.milestone1,1.0.0-milestone2",
        "1.0.0.m1,1.0.0-m2",
        "1.0.0.RC1,1.0.0-RC2"
    })
    public void should_CompareMavenOrder_When_Mng7644Cases(String lowerVersion, String higherVersion) {
        assertComparison(lowerVersion, higherVersion, -1);
    }

    @ParameterizedTest
    @CsvSource({
        "2-abc,2.0.abc",
        "2-abc,2.0.0.abc",
        "2.0.abc,2.0.0.abc",
        "2-alpha,2.0.alpha",
        "2-alpha,2.0.0.alpha",
        "2.0.alpha,2.0.0.alpha",
        "2-a,2.0.a",
        "2-a,2.0.0.a",
        "2.0.a,2.0.0.a",
        "2-beta,2.0.beta",
        "2-beta,2.0.0.beta",
        "2.0.beta,2.0.0.beta",
        "2-b,2.0.b",
        "2-b,2.0.0.b",
        "2.0.b,2.0.0.b",
        "2-def,2.0.def",
        "2-def,2.0.0.def",
        "2.0.def,2.0.0.def",
        "2-milestone,2.0.milestone",
        "2-milestone,2.0.0.milestone",
        "2.0.milestone,2.0.0.milestone",
        "2-m,2.0.m",
        "2-m,2.0.0.m",
        "2.0.m,2.0.0.m",
        "2-RC,2.0.RC",
        "2-RC,2.0.0.RC",
        "2.0.RC,2.0.0.RC"
    })
    public void should_CompareMavenEquality_When_Mng7644Cases(String firstVersion, String secondVersion) {
        assertComparison(firstVersion, secondVersion, 0);
    }

    @ParameterizedTest
    @CsvSource({
        "en,1-abcdefghijklmnopqrstuvwxyz,1-ABCDEFGHIJKLMNOPQRSTUVWXYZ",
        "tr,1-abcdefghijklmnopqrstuvwxyz,1-ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    })
    public void should_CompareMavenCaseInsensitiveBehavior_When_LocaleChanges(String localeTag, String firstVersion, String secondVersion) {
        Locale originalLocale = Locale.getDefault();

        try {
            Locale.setDefault(Locale.forLanguageTag(localeTag));
            assertComparison(firstVersion, secondVersion, 0);
        } finally {
            Locale.setDefault(originalLocale);
        }
    }

    private void assertComparison(String firstVersion, String secondVersion, int expectedComparison) {
        int actualComparison = Integer.signum(mavenVersionComparator.compare(firstVersion, secondVersion));
        assertEquals(expectedComparison, actualComparison, "Mismatch for versions [" + firstVersion + ", " + secondVersion + "]");
    }
}
