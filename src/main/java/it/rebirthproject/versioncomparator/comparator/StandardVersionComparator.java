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

import it.rebirthproject.versioncomparator.parser.RelaxedSemanticVersionParser;
import it.rebirthproject.versioncomparator.parser.StrictSemanticVersionParser;
import it.rebirthproject.versioncomparator.version.Version;
import it.rebirthproject.versioncomparator.version.VersionReleaseTypes;
import it.rebirthproject.versioncomparator.parser.VersionParser;
import java.util.Arrays;
import java.util.Optional;

/**
 * This class is used to compare two versions in String format.
 */
public class StandardVersionComparator implements VersionComparator {

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
    protected boolean releaseTypeUniqueInQualifier = false;

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
            } else if (versionParser instanceof StrictSemanticVersionParser) {
                return 0;
            } else {
                return compareBuildMetadata(firstVersion.getBuildMetadata(), secondVersion.getBuildMetadata());
            }
        }
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
    protected int compareQualifiers(String firstVersionQualifier, String secondVersionQualifier) {
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
        } else if (versionParser instanceof StrictSemanticVersionParser) {
            return compareStrictSemanticPrerelease(firstVersionQualifier, secondVersionQualifier);
        } else {
            return firstVersionQualifier.compareToIgnoreCase(secondVersionQualifier);
        }
    }

    /**
     * Compares strict semantic pre-release identifiers according to SemVer precedence rules.
     *
     * @param firstVersionQualifier The first qualifier to compare.
     * @param secondVersionQualifier The second qualifier to compare.
     *
     * @return An integer indicating whether the first qualifier is greater (1), equal (0),
     * or lesser (-1) than the second qualifier.
     */
    private int compareStrictSemanticPrerelease(String firstVersionQualifier, String secondVersionQualifier) {
        String[] firstIdentifiers = firstVersionQualifier.split("\\.");
        String[] secondIdentifiers = secondVersionQualifier.split("\\.");
        int commonIdentifiersCount = Math.min(firstIdentifiers.length, secondIdentifiers.length);

        for (int i = 0; i < commonIdentifiersCount; i++) {
            String firstIdentifier = firstIdentifiers[i];
            String secondIdentifier = secondIdentifiers[i];
            boolean firstNumeric = isNumericIdentifier(firstIdentifier);
            boolean secondNumeric = isNumericIdentifier(secondIdentifier);

            if (firstNumeric && secondNumeric) {
                int lengthComparison = Integer.compare(firstIdentifier.length(), secondIdentifier.length());
                if (lengthComparison != 0) {
                    return lengthComparison;
                }

                int lexicalNumericComparison = Integer.signum(firstIdentifier.compareTo(secondIdentifier));
                if (lexicalNumericComparison != 0) {
                    return lexicalNumericComparison;
                }
            } else if (firstNumeric != secondNumeric) {
                return firstNumeric ? -1 : 1;
            } else {
                int lexicalComparison = Integer.signum(firstIdentifier.compareTo(secondIdentifier));
                if (lexicalComparison != 0) {
                    return lexicalComparison;
                }
            }
        }

        return Integer.compare(firstIdentifiers.length, secondIdentifiers.length);
    }

    /**
     * Checks if an identifier contains only numeric characters.
     *
     * @param identifier The identifier to check.
     *
     * @return true if the identifier is numeric, false otherwise.
     */
    private boolean isNumericIdentifier(String identifier) {
        return identifier.matches("\\d+");
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
    protected int compareReleaseTypesInQualifier(String firstVersionQualifier, String secondVersionQualifier) {
        String normalizedFirstVersionQualifier = normalizeQualifier(firstVersionQualifier);
        String normalizedSecondVersionQualifier = normalizeQualifier(secondVersionQualifier);
        String[] arrayValues = VersionReleaseTypes.getValues();
        Optional<String> optionalStringReleaseTypeFirstVersion = Arrays.stream(arrayValues).filter(s -> s.equalsIgnoreCase(normalizedFirstVersionQualifier)).findFirst();
        Optional<String> optionalStringReleaseTypeSecondVersion = Arrays.stream(arrayValues).filter(s -> s.equalsIgnoreCase(normalizedSecondVersionQualifier)).findFirst();

        if (!optionalStringReleaseTypeFirstVersion.isPresent() && !optionalStringReleaseTypeSecondVersion.isPresent()) {
            return Integer.compare(normalizedFirstVersionQualifier.compareToIgnoreCase(normalizedSecondVersionQualifier), 0);
        }

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
     * Normalizes the qualifier by removing leading separators used by relaxed semantic parsing.
     *
     * @param qualifier The qualifier to normalize.
     *
     * @return The normalized qualifier.
     */
    private String normalizeQualifier(String qualifier) {
        return qualifier.replaceFirst("^[.-]+", "");
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
    protected boolean isStableOrFinal(String qualifierType) {
        String qualifierTypeUpper = qualifierType.toUpperCase();
        return qualifierTypeUpper.contains(VersionReleaseTypes.STABLE.getValue()) || qualifierTypeUpper.contains(VersionReleaseTypes.FINAL.getValue());
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
}
