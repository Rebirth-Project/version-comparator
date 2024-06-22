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

import it.rebirthproject.versioncomparator.version.MavenConstants;
import it.rebirthproject.versioncomparator.version.MavenCharType;
import it.rebirthproject.versioncomparator.utils.TokenUtils;
import it.rebirthproject.versioncomparator.version.Version;
import java.util.ArrayList;
import java.util.List;

public class MavenVersionParser implements VersionParser {

    @Override
    public Version parseVersion(String version) throws IllegalArgumentException {
        List<String> tokenList = new ArrayList<>();
        StringBuilder token = new StringBuilder();

        if (version == null) {
            throw new IllegalArgumentException("Invalid Maven version string format: version is \"" + version + "\"");
        }

        Character c = version.charAt(0);
        MavenCharType previousCharType = getCharType(c);

        if (!previousCharType.equals(MavenCharType.DIGIT)) {
            throw new IllegalArgumentException("Invalid Maven version string format: does not start with a number \"" + version + "\"");
        }
        if (c.equals('0') && version.length()> 1) {
            throw new IllegalArgumentException("Invalid Maven version string format: starts with 0 \"" + version + "\"");
        }

        //Basically the _ is equivalent to - to maven specifications to we remove it for simplicity
        String replacedVersion = version.replace(MavenConstants.REPLACEBLE_SEPARATOR, MavenConstants.HYPHEN_SEPARATOR);

        for (int i = 0; i < replacedVersion.length(); i++) {
            c = replacedVersion.charAt(i);
            MavenCharType currentCharType = getCharType(c);

            if (currentCharType == MavenCharType.SEPARATOR) {
                tokenList.add(token.toString());
                token.setLength(0);
                tokenList.add("" + c);
                previousCharType = currentCharType;
            } else {
                if (currentCharType != previousCharType && previousCharType != MavenCharType.SEPARATOR) {
                    tokenList.add(token.toString());
                    token.setLength(0);
                    //For transactions between numbers and letters we add always an hyphen as stated in maven specifications.
                    tokenList.add("-");
                }
                token.append(c);
                previousCharType = currentCharType;
            }
        }
        tokenList.add(token.toString());

        checkForInvalidNumericTokens(version, tokenList);
        trimNullValues(tokenList);

        return new Version(tokenList);
    }

    private MavenCharType getCharType(char c) throws IllegalArgumentException {
        if (Character.isDigit(c)) {
            return MavenCharType.DIGIT;
        } else if (Character.isLetter(c)) {
            return MavenCharType.LETTER;
        } else {
            if (TokenUtils.isRealSeparator(c)) {
                return MavenCharType.SEPARATOR;
            } else {
                throw new IllegalArgumentException("Invalid Maven version string format: version contains unacceptable characters. \"" + c + "\"");
            }
        }
    }

    private void checkForInvalidNumericTokens(String version, List<String> tokens) throws IllegalArgumentException {
        for (String token : tokens) {
            if (TokenUtils.isNumber(token) && token.length() > 1 && token.startsWith("0")) {
                throw new IllegalArgumentException("Invalid Maven version string format: numbers starting with 0 are not allowed \"" + version + "\"");
            }
        }
    }

    private void trimNullValues(List<String> tokens) {
        //The first token is always a number and even if 0 we must leave it because 0 is a valid version.
        for (int i = tokens.size() - 1; i > 0; i--) {
            if (TokenUtils.isNullValue(tokens.get(i))) {
                //We remove the value and also the previous separator
                tokens.remove(i);
                if (i > 0) {
                    i--;
                    tokens.remove(i);
                }
            }
        }
    }
}