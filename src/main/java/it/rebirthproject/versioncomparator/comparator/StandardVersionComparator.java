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

import it.rebirthproject.versioncomparator.parser.RelaxedSemanticVersionParser;
import it.rebirthproject.versioncomparator.version.Version;
import it.rebirthproject.versioncomparator.version.VersionReleaseTypes;
import it.rebirthproject.versioncomparator.parser.VersionParser;
import java.util.Arrays;
import java.util.Optional;

/**
 * This class is used to compare two versions in String format.
 */
final public class StandardVersionComparator implements VersionComparator {

    /**
     * The version parser used to match a string formatted version.
     */
    private final VersionParser versionParser;

    /**
     * A boolean variable that forces an unique version release type qualifier
     * {@link VersionReleaseTypes} inside the string formatted version. If true
     * then the version qualifier can contain only one of release type string.
     * For example "RC" or "SNAPSHOT", or none of them.
     */
    private boolean releaseTypeUniqueInQualifier = false;

    /**
     * The version comparator private contructor. The
     * {@link VersionComparatorBuilder} is used to build the comparator.
     *
     * @param versionParser The version parser chosen to check if a String
     * formatted version matches the rules.
     */
    StandardVersionComparator(VersionParser versionParser) {
        this.versionParser = versionParser;
        if (versionParser instanceof RelaxedSemanticVersionParser) {
            this.releaseTypeUniqueInQualifier = true;
        }
    }

    /**
     * The main method used to compare two string formatted versions. It parses
     * the versions' strings into valid {@link Version} And if no error occurs
     * then it start comparing the various parts of the versions. We have
     * basically three parts composing a valid version: - The number part (ex:
     * 1.0.0) - The qualifier (ex: STABLE, FINAL,RC) - The build metadata (ex:
     * build5678)
     *
     * The various parts are parsed using the chose version parser.
     *
     * @param version1 The first version to compare.
     * @param version2 The second version to compare.
     *
     * @return An integer indicating whether the first version is greater (1),
     * equal (0), or lesser (-1) than the second version.
     */
    @Override
    public int compare(String version1, String version2) throws IllegalArgumentException {
        Version firstVersion = versionParser.parseVersion(version1);
        Version secondVersion = versionParser.parseVersion(version2);

        int numberComparison = compareNumbers(firstVersion, secondVersion);
        if (numberComparison != 0) {
            return numberComparison;
        } else {
            int qualifierComparison = compareQualifiers(firstVersion.getQualifier(), secondVersion.getQualifier());
            if (qualifierComparison != 0) {
                return qualifierComparison;
            } else {
                return compareBuildMetadata(firstVersion.getBuildMetadata(), secondVersion.getBuildMetadata());
            }
        }
    }

    /**
     * This method takes two {@link Version} formatted versions and compares the
     * number parts of them.
     *
     * @param firstVersion The first version to compare.
     * @param secondVersion The second version to compare.
     *
     * @return An integer indicating whether the first version's number part is
     * greater (1), equal (0), or lesser (-1) than the second version's number
     * part.
     */
    private int compareNumbers(Version firstVersion, Version secondVersion) {
        if (firstVersion.getMajor() != secondVersion.getMajor()) {
            return Long.compare(firstVersion.getMajor(), secondVersion.getMajor());
        }

        if (firstVersion.getMinor() != secondVersion.getMinor()) {
            return Long.compare(firstVersion.getMinor(), secondVersion.getMinor());
        }

        if (firstVersion.getPatch() != secondVersion.getPatch()) {
            return Long.compare(firstVersion.getPatch(), secondVersion.getPatch());
        }

        return 0;
    }

    /**
     * This method takes two String formatted version's qualifiers and compares
     * them.
     *
     * @param firstVersionQualifier The first version's qualifier to compare.
     * @param secondVersionQualifier The second version's qualifier to compare.
     *
     * @return An integer indicating whether the first version's qualifier is
     * greater (1), equal (0), or lesser (-1) than the second version's
     * qualifier.
     */
    private int compareQualifiers(String firstVersionQualifier, String secondVersionQualifier) {
        if (firstVersionQualifier == null && secondVersionQualifier == null) {
            return 0;
        }

        if (firstVersionQualifier == null) {
            return isStableOrFinal(secondVersionQualifier) ? -1 : 1;
        }

        if (secondVersionQualifier == null) {
            return isStableOrFinal(firstVersionQualifier) ? 1 : -1;
        }

        if (releaseTypeUniqueInQualifier) {
            return compareReleaseTypesInQualifier(firstVersionQualifier, secondVersionQualifier);
        } else {
            return firstVersionQualifier.compareToIgnoreCase(secondVersionQualifier);
        }
    }

    /**
     * This method takes two String formatted version's build metadata and
     * compares them in a lexical way.
     *
     * @param firstVersionBuildMetadata The first version's build metadata to
     * compare.
     * @param secondVersionBuildMetadata The second version's build metadata to
     * compare.
     *
     * @return An integer indicating whether the first version's build metadata
     * is greater (1), equal (0), or lesser (-1) than the second version's build
     * metadata.
     */
    private int compareBuildMetadata(String firstVersionBuildMetadata, String secondVersionBuildMetadata) {
        if (firstVersionBuildMetadata == null && secondVersionBuildMetadata == null) {
            return 0;
        }

        if (firstVersionBuildMetadata == null) {
            return isStableOrFinal(secondVersionBuildMetadata) ? -1 : 1;
        }

        if (secondVersionBuildMetadata == null) {
            return isStableOrFinal(firstVersionBuildMetadata) ? 1 : -1;
        }

        return firstVersionBuildMetadata.compareToIgnoreCase(secondVersionBuildMetadata);
    }

    /**
     * This method compares two version qualifiers in string format.
     *
     * @param firstVersionQualifier The first version's qualifier to compare.
     * @param secondVersionQualifier The second version's qualifier to compare.
     *
     * @return An integer indicating whether the first version's qualifier is
     * greater (1), equal (0), or lesser (-1) than the second version's
     * qualifier.
     */
    private int compareReleaseTypesInQualifier(String firstVersionQualifier, String secondVersionQualifier) {
        String[] arrayValues = VersionReleaseTypes.getValues();
        Optional<String> optionalStringReleaseTypeFirstVersion = Arrays.stream(arrayValues).filter(s -> s.equals(firstVersionQualifier)).findFirst();
        Optional<String> optionalStringReleaseTypeSecondVersion = Arrays.stream(arrayValues).filter(s -> s.equals(secondVersionQualifier)).findFirst();

        if (!optionalStringReleaseTypeFirstVersion.isPresent()) {
            return isStableOrFinal(secondVersionQualifier) ? 1 : -1;
        } else if (!optionalStringReleaseTypeSecondVersion.isPresent()) {
            return isStableOrFinal(firstVersionQualifier) ? 1 : -1;
        } else {
            int priorityReleaseTypeFirstVersion = VersionReleaseTypes.getValueOfReleaseTypes(optionalStringReleaseTypeFirstVersion.get()).getSemanticPriority();
            int priorityReleaseTypeSecondVersion = VersionReleaseTypes.getValueOfReleaseTypes(optionalStringReleaseTypeSecondVersion.get()).getSemanticPriority();

            return Integer.compare(priorityReleaseTypeFirstVersion, priorityReleaseTypeSecondVersion);
        }
    }

    /**
     * This method checks if a unique qualifier is of type
     * {VersionReleaseTypes.STABLE} or {VersionReleaseTypes.FINAL}
     *
     * @param qualifierType The qualifier's string to check.
     *
     * @return A boolean value that is true if the qualifier contains the type
     * {VersionReleaseTypes.STABLE} or {VersionReleaseTypes.FINAL}, and false
     * otherwise.
     */
    private boolean isStableOrFinal(String qualifierType) {
        String qualifierTypeUpper = qualifierType.toUpperCase();
        return qualifierTypeUpper.contains(VersionReleaseTypes.STABLE.getValue()) || qualifierTypeUpper.contains(VersionReleaseTypes.FINAL.getValue());
    }
}
