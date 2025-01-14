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

import it.rebirthproject.versioncomparator.version.Version;
import it.rebirthproject.versioncomparator.version.VersionReleaseTypes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parser that checks if a version structure is compiant with a relaxed
 * version of the semantic version definition.
 */
public class RelaxedSemanticVersionParser implements VersionParser {

    /**
     * Max number of digits for the version's number parts.
     */
    private static final String MAX_DIGITS = "10";
    /**
     * The complete regex that matches the relaxed semantic version structure.
     */
    private final Pattern pattern = Pattern.compile("(?i)^(?<major>0|[1-9]\\d{0," + MAX_DIGITS + "})\\.(?<minor>0|[1-9]\\d{0," + MAX_DIGITS + "})(?:\\.(?<patch>0|[1-9]\\d{0," + MAX_DIGITS + "}))(?:(?<qualifier>([a-zA-Z.-](?!.*[.-]{2})[a-zA-Z0-9.-]*)))?$");   
    /**
     * The implemented method to parse a relaxed semantic version
     *
     * @param version the string format version
     * @return A structured version if the parser matches rules.
     * @throws IllegalArgumentException if the string version does not match the
     * parser's rules.
     */
    @Override
    public Version parseVersion(String version) throws IllegalArgumentException {
        Matcher matcher = pattern.matcher(version);

        if (matcher.matches()) {
            Long major = matcher.group("major") != null ? Long.valueOf(matcher.group("major")) : -1;
            Long minor = matcher.group("minor") != null ? Long.valueOf(matcher.group("minor")) : -1;
            Long patch = matcher.group("patch") != null ? Long.valueOf(matcher.group("patch")) : -1;
            String qualifier = matcher.group("qualifier");           
            if (qualifier != null && !qualifier.trim().isEmpty() && !qualifier.matches(VersionReleaseTypes.getRegexToCheckReleaseTypeUniqueness())) {
                throw new IllegalArgumentException("Invalid relaxed semantic version string format: release type is not unique in qualifier" + version);
            }
            return new Version(major, minor, patch, qualifier);
        } else {
            throw new IllegalArgumentException("Invalid relaxed semantic version string format: " + version);
        }
    }
}
