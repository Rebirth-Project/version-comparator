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
package it.rebirthproject.versioncomparator.parser;

import it.rebirthproject.versioncomparator.version.MavenConstants;
import it.rebirthproject.versioncomparator.version.MavenCharType;
import it.rebirthproject.versioncomparator.utils.TokenUtils;
import it.rebirthproject.versioncomparator.version.Version;
import java.util.ArrayList;
import java.util.List;

public class MavenRulesVersionParser implements VersionParser {

    @Override
    public Version parseVersion(String version) throws IllegalArgumentException {
        List<String> tokenList = new ArrayList<>();
        StringBuilder token = new StringBuilder();

        if (version == null) {
            throw new IllegalArgumentException("Invalid Maven version string format: version is \"" + version + "\"");
        }

        if (version.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid Maven version string format: version is blank");
        }

        Character c = version.charAt(0);
        MavenCharType previousCharType = getCharType(c);

        //Basically the _ is equivalent to - to maven specifications so we replace it for simplicity
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
        
        normalizeDotQualifierTransition(tokenList);
        replaceEmptyTokensWithZero(tokenList);
        trimNullValues(tokenList);

        return new Version(tokenList);
    }

    private MavenCharType getCharType(char c) throws IllegalArgumentException {
        if (Character.isDigit(c)) {
            return MavenCharType.DIGIT;
        } else if (Character.isLetter(c)) {
            return MavenCharType.LETTER;
        } else {
            if (TokenUtils.isSeparator(c)) {
                return MavenCharType.SEPARATOR;
            } else {
                // Maven ComparableVersion is permissive for non-separator symbols,
                // so we treat them as string qualifier characters.
                return MavenCharType.LETTER;
            }
        }
    }

    private void replaceEmptyTokensWithZero(List<String> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).isEmpty()) {
                tokens.set(i, MavenConstants.ZERO);
            }
        }
    }

    /**
     * Normalizes dot-separated qualifiers to Maven canonical structure.
     *
     * <p>When a dot precedes a qualifier token, it is treated as a hyphen separator.
     * Expressions like "2.0.0.alpha" and "2-alpha" become equivalent after
     * null-value trimming.</p>
     *
     * @param tokens The parsed token list to normalize.
     */
    private void normalizeDotQualifierTransition(List<String> tokens) {
        for (int i = 1; i < tokens.size() - 1; i++) {
            if (MavenConstants.FULLSTOP_SEPARATOR.toString().equals(tokens.get(i))
                    && !TokenUtils.isNumber(tokens.get(i + 1))
                    // Keep the historical behavior for "-N.qualifier" segments (e.g. "1-1.foo").
                    // We only normalize dot-to-hyphen for the plain dot-chain form used by MNG-7644
                    // (e.g. "2.0.alpha" -> "2-0-alpha" before trimming).
                    && (i < 2 || !MavenConstants.HYPHEN_SEPARATOR.toString().equals(tokens.get(i - 2)))) {
                tokens.set(i, MavenConstants.HYPHEN_SEPARATOR.toString());
            }
        }
    }

    private void trimNullValues(List<String> tokens) {
        //Maven Rules state:
        //Then, starting from the end of the version, the trailing "null" values (0, "", "final", "ga") are trimmed. This process is repeated at each remaining hyphen from end to start.
        boolean shouldTrim = true;
        for (int i = tokens.size() - 1; i > 0; i--) {
            String token = tokens.get(i);
            if (shouldTrim && TokenUtils.isNullValue(token)) {
                tokens.remove(i);
                if (i > 0) {
                    i--;
                    tokens.remove(i);
                }
            } else {
                shouldTrim = token.equals("-");
            }
        }
    }
}
