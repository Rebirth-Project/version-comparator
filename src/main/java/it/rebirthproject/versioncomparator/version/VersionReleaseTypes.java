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
package it.rebirthproject.versioncomparator.version;

import java.util.Arrays;

/**
 * An enum containing all known version's release types.
 */
public enum VersionReleaseTypes {
    SNAPSHOT("SNAPSHOT", 0, 4),
    PRE_ALPHA("PRE_ALPHA", 1, 0),
    ALPHA("ALPHA", 2, 0),
    A("A", 2, 0),
    BETA("BETA", 3, 1),
    B("BETA", 3, 1),
    MILESTONE("MILESTONE", 4, 2),
    M("M", 4, 2),
    CR("CR", 5, 3),
    RC("RC", 5, 3),
    EMPTY("", 8, 5),
    STABLE("STABLE", 6, 6),
    FINAL("FINAL", 7, 6),
    GA("GA", 7, 6),
    RELEASE("RELEASE", 7, 6),
    SP("SP", 7, 7);

    /**
     * The String value representing the version release type
     */
    private final String value;

    /**
     * An integer that represents the priority of a release type. Higher
     * priority means an higher Version.
     */
    private final int semanticPriority;

    /**
     * An integer that represents the priority of a release type. Higher
     * priority means an higher Version.
     */
    private final int mavenPriority;

    /**
     * The {@link VersionReleaseTypes}'s contructor.
     *
     * @param value The string value of the type.
     * @param priority the priority of the type.
     */
    private VersionReleaseTypes(String value, int semanticPriority, int mavenPriority) {
        this.value = value;
        this.semanticPriority = semanticPriority;
        this.mavenPriority = mavenPriority;
    }

    /**
     *
     * @return the semantic priority of the type.
     */
    public int getSemanticPriority() {
        return semanticPriority;
    }

    /**
     *
     * @return the maven priority of the type.
     */
    public int getMavenPriority() {
        return mavenPriority;
    }

    /**
     *
     * @return the string value of the type.
     */
    public String getValue() {
        return value;
    }

    /**
     * Creates the VersionReleaseTypes enum type given the string
     * representation.
     *
     * @param value the string value of the type.
     * @return The enum type given the string value.
     */
    public static VersionReleaseTypes getValueOfReleaseTypes(String value) {
        VersionReleaseTypes[] lista = VersionReleaseTypes.values();
        for (VersionReleaseTypes valoreReleaseType : lista) {
            if (valoreReleaseType.value.compareToIgnoreCase(value) == 0) {
                return valoreReleaseType;
            }
        }
        return null;
    }

    /**
     * This method returns a stering array with all the types.
     *
     * @return all the types in an string array format.
     */
    public static String[] getValues() {
        return Arrays.stream(VersionReleaseTypes.values()).map(Enum::name).toArray(String[]::new);
    }

    /**
     * This method creates a regex used to check the release type uniqueness if
     * present.
     *
     * @return the complete regex in string format.
     */
    public static String getRegexToCheckReleaseTypeUniqueness() {
        String qualifiers = String.join("|", getValues());
        //(?i) means case insensitive match
        //The Release Type is not mandatory but if is present then must be unique.
        return "(?i)^(?!.*\\b(?:" + qualifiers + ")\\b.*\\b(?:" + qualifiers + ")\\b).*$";
    }
}
