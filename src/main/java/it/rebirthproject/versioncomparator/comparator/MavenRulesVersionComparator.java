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
package it.rebirthproject.versioncomparator.comparator;

import it.rebirthproject.versioncomparator.parser.VersionParser;
import it.rebirthproject.versioncomparator.utils.MavenRulesVersionPadder;
import it.rebirthproject.versioncomparator.utils.TokenUtils;
import it.rebirthproject.versioncomparator.version.MavenConstants;
import it.rebirthproject.versioncomparator.version.PaddedLists;
import it.rebirthproject.versioncomparator.version.Version;
import it.rebirthproject.versioncomparator.version.VersionReleaseTypes;
import java.util.List;

public class MavenRulesVersionComparator implements VersionComparator {

    private final VersionParser mavenVersionParser;
    private final MavenRulesVersionPadder mavenVersionPadder;

    MavenRulesVersionComparator(VersionParser mavenVersionParser, MavenRulesVersionPadder mavenVersionPadder) {
        this.mavenVersionParser = mavenVersionParser;
        this.mavenVersionPadder = mavenVersionPadder;
    }

    @Override
    public int compare(String version1, String version2) throws IllegalArgumentException {
        Version firstVersion = mavenVersionParser.parseVersion(version1);
        Version secondVersion = mavenVersionParser.parseVersion(version2);

        PaddedLists paddedLists = mavenVersionPadder.padShorterVersion(firstVersion.getTokenList(), secondVersion.getTokenList());
        List<String> firstVersionTokenList = paddedLists.getVersion1();
        List<String> secondVersionTokenList = paddedLists.getVersion2();

        int size = firstVersionTokenList.size();

        String token1 = firstVersionTokenList.get(0);
        String token2 = secondVersionTokenList.get(0);

        int compareResult = compareNumericTokens(token1, token2);
        if (compareResult != 0) {
            return compareResult;
        } else {
            for (int i = 1; i < size; i++) {
                //First token in list is always a separator 
                String separator1 = firstVersionTokenList.get(i);
                String separator2 = secondVersionTokenList.get(i);

                //then we must take the following token to compare it
                i++;
                token1 = firstVersionTokenList.get(i);
                token2 = secondVersionTokenList.get(i);

                if (separator1.equals(separator2)) {
                    compareResult = compareTokens(firstVersionTokenList, secondVersionTokenList, i);
                } else {
                    compareResult = compareTokensWithSeparator(separator1, token1, token2);
                }

                if (compareResult != 0) {
                    return compareResult;
                }
            }
            return 0;
        }
    }

    private int compareTokens(List<String> firstVersionTokenList, List<String> secondVersionTokenList, int tokenIndex) {
        String token1 = firstVersionTokenList.get(tokenIndex);
        String token2 = secondVersionTokenList.get(tokenIndex);
        boolean token1IsNumber = TokenUtils.isNumber(token1);
        boolean token2IsNumber = TokenUtils.isNumber(token2);
        // alpha = a < beta = b < milestone = m < rc = cr < snapshot < '' < final = ga = release < sp

        // Check if both tokens are numeric
        if (token1IsNumber && token2IsNumber) {
            return compareNumericTokens(token1, token2);
        }

        // Check if one is numeric and the other is a string
        if (TokenUtils.isNumber(token1)) {
            return 1; // Numbers come before qualifiers
        }
        if (TokenUtils.isNumber(token2)) {
            return -1; // Qualifiers come after numbers
        }

        // Both are strings, compare their order
        VersionReleaseTypes qualifier1 = resolveQualifier(firstVersionTokenList, tokenIndex);
        VersionReleaseTypes qualifier2 = resolveQualifier(secondVersionTokenList, tokenIndex);

        // Both qualifiers are known
        if (qualifier1 != null && qualifier2 != null) {
            return Integer.compare(qualifier1.getMavenPriority(), qualifier2.getMavenPriority());
        }

        // If one is in known and the other is not, the one known comes first (is lesser than)
        if (qualifier1 != null) {
            return -1;
        }

        if (qualifier2 != null) {
            return 1;
        }

        //If both qualifiers are unknown then compare case-insensitively and normalize the result.
        return Integer.compare(token1.compareToIgnoreCase(token2), 0);
    }

    /**
     * Resolves a qualifier token using Maven alias rules.
     *
     * <p>Single-letter aliases {@code a}, {@code b} and {@code m} are treated as known
     * qualifiers only when followed by a numeric token (for example {@code 1a1},
     * {@code 1-b2}, {@code 1.m3}). In other contexts they are treated as unknown
     * qualifiers and compared lexicographically.</p>
     *
     * @param tokenList The parsed version token list.
     * @param tokenIndex The index of the current qualifier token.
     *
     * @return The resolved release type or {@code null} for unknown qualifiers.
     */
    private VersionReleaseTypes resolveQualifier(List<String> tokenList, int tokenIndex) {
        VersionReleaseTypes qualifier = VersionReleaseTypes.getValueOfReleaseTypes(tokenList.get(tokenIndex));

        if (qualifier == VersionReleaseTypes.A || qualifier == VersionReleaseTypes.B || qualifier == VersionReleaseTypes.M) {
            // For single-letter aliases Maven requires a following numeric token.
            int nextTokenIndex = tokenIndex + 2;
            if (nextTokenIndex >= tokenList.size()) {
                return null;
            }

            return TokenUtils.isNumber(tokenList.get(nextTokenIndex)) ? qualifier : null;
        }

        return qualifier;
    }

    private int compareTokensWithSeparator(String separator1, String token1, String token2) {
        boolean token1IsNumber = TokenUtils.isNumber(token1);
        boolean token2IsNumber = TokenUtils.isNumber(token2);

        // tokens are both strings
        if (!token1IsNumber && !token2IsNumber) {
            return 0;
        }

        // tokens are both numbers
        if (token1IsNumber && token2IsNumber) {
            if (separator1.equals("" + MavenConstants.FULLSTOP_SEPARATOR)) {
                return 1;
            } else {
                return -1;
            }
        }

        // One is a number and the other is a string
        return token1IsNumber ? 1 : -1;
    }

    /**
     * Compares numeric tokens without integer parsing to avoid overflow.
     * Leading zeros are ignored to match Maven numeric-token ordering
     * (for example {@code 0001} equals {@code 1}).
     *
     * @param token1 The first numeric token.
     * @param token2 The second numeric token.
     *
     * @return An integer indicating whether the first token is greater (1), equal (0),
     * or lesser (-1) than the second token.
     */
    private int compareNumericTokens(String token1, String token2) {
        String normalizedToken1 = normalizeNumericToken(token1);
        String normalizedToken2 = normalizeNumericToken(token2);

        int lengthComparison = Integer.compare(normalizedToken1.length(), normalizedToken2.length());
        if (lengthComparison != 0) {
            return lengthComparison;
        }

        return Integer.compare(normalizedToken1.compareTo(normalizedToken2), 0);
    }

    /**
     * Normalizes a numeric token by removing leading zeroes while preserving one zero
     * for all-zero values.
     *
     * @param token The numeric token to normalize.
     *
     * @return The normalized numeric token.
     */
    private String normalizeNumericToken(String token) {
        int index = 0;
        while (index < token.length() - 1 && token.charAt(index) == '0') {
            index++;
        }

        return token.substring(index);
    }
}
