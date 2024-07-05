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
package it.rebirthproject.versioncomparator.comparator;

import it.rebirthproject.versioncomparator.parser.VersionParser;
import it.rebirthproject.versioncomparator.utils.MavenVersionPadder;
import it.rebirthproject.versioncomparator.utils.TokenUtils;
import it.rebirthproject.versioncomparator.version.MavenConstants;
import it.rebirthproject.versioncomparator.version.PaddedLists;
import it.rebirthproject.versioncomparator.version.Version;
import it.rebirthproject.versioncomparator.version.VersionReleaseTypes;
import java.util.List;

final public class MavenVersionComparator implements VersionComparator {

    private final VersionParser versionParser;
    private final MavenVersionPadder mavenVersionPadder;

    MavenVersionComparator(VersionParser versionParser, MavenVersionPadder mavenVersionPadder) {
        this.versionParser = versionParser;
        this.mavenVersionPadder = mavenVersionPadder;
    }

    @Override
    public int compare(String version1, String version2) throws IllegalArgumentException {
        Version firstVersion = versionParser.parseVersion(version1);
        Version secondVersion = versionParser.parseVersion(version2);
        PaddedLists paddedLists = mavenVersionPadder.padShorterVersion(firstVersion.getTokenList(), secondVersion.getTokenList());

        List<String> firstVersionTokenList = paddedLists.getVersion1();
        List<String> secondVersionTokenList = paddedLists.getVersion2();

        int size = firstVersionTokenList.size();
       
        String token1 = firstVersionTokenList.get(0);
        String token2 = secondVersionTokenList.get(0);

        int compareResult = Integer.compare(Integer.parseInt(token1), Integer.parseInt(token2));
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
                    compareResult = compareTokens(token1, token2);
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

    private int compareTokens(String token1, String token2) {
        boolean token1IsNumber = TokenUtils.isNumber(token1);
        boolean token2IsNumber = TokenUtils.isNumber(token2);
        // alpha = a < beta = b < milestone = m < rc = cr < snapshot < '' < final = ga = release < sp

        // Check if both tokens are numeric
        if (token1IsNumber && token2IsNumber) {
            return Integer.compare(Integer.parseInt(token1), Integer.parseInt(token2));
        }

        // Check if one is numeric and the other is a string
        if (TokenUtils.isNumber(token1)) {
            return 1; // Numbers come before qualifiers
        }
        if (TokenUtils.isNumber(token2)) {
            return -1; // Qualifiers come after numbers
        }

        // Both are strings, compare their order
        VersionReleaseTypes qualifier1 = VersionReleaseTypes.getValueOfReleaseTypes(token1);
        VersionReleaseTypes qualifier2 = VersionReleaseTypes.getValueOfReleaseTypes(token2);

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

        //If both qualifiers are unknown then compare lexicographically.
        return token1.compareTo(token2);
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
}
