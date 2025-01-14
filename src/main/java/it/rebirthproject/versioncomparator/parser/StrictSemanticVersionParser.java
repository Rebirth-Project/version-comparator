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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parser that checks if a version structure is compiant with the semantic
 * version definition.
 *
 * See https://semver.org/ for a complete documentation on semantic version
 * definition.
 */
public class StrictSemanticVersionParser implements VersionParser {

    /**
     * Max number of digits for the version's number parts.
     */
    private static final String MAX_DIGITS = "19";
    /**
     * The complete regex that matches the semantic version structure.
     */
    private final Pattern pattern = Pattern.compile("(?i)^(?<major>0|[1-9]\\d{0," + MAX_DIGITS + "})\\.(?<minor>0|[1-9]\\d{0," + MAX_DIGITS + "})\\.(?<patch>0|[1-9]\\d{0," + MAX_DIGITS + "})(?:-(?<qualifier>(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+(?<buildmetadata>[0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$");

    /**
     * The implemented method to parse a semantic version
     *
     * @param version the string format version
     * @return A structured version if the parser matches rules.
     * @throws IllegalArgumentException if the string version does not match the parser's rules.
     */       
    @Override
    public Version parseVersion(String version) throws IllegalArgumentException {
        Matcher matcher = pattern.matcher(version);

        if (matcher.matches()) {
            Long major = matcher.group("major") != null ? Long.valueOf(matcher.group("major")) : -1;
            Long minor = matcher.group("minor") != null ? Long.valueOf(matcher.group("minor")) : -1;
            Long patch = matcher.group("patch") != null ? Long.valueOf(matcher.group("patch")) : -1;
            String qualifier = matcher.group("qualifier");
            String buildMetadata = matcher.group("buildmetadata");

            return new Version(major, minor, patch, qualifier, buildMetadata);
        } else {
            throw new IllegalArgumentException("Invalid Strict Semantic version string format: " + version);
        }
    }
}
