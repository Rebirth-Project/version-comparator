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
import it.rebirthproject.versioncomparator.version.VersionReleaseTypes;
import java.util.Arrays;
import java.util.Optional;

/**
 * This class is used to compare two versions in String format.
 */
public class MavenStandardVersionComparator extends StandardVersionComparator {

    /**
     * The version comparator private contructor. The
     * {@link VersionComparatorBuilder} is used to build the comparator.
     *
     * @param versionParser The version parser chosen to check if a String
     * formatted version matches the rules.
     */
    MavenStandardVersionComparator(VersionParser versionParser) {
        super(versionParser);
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
    @Override
    protected int compareQualifiers(String firstVersionQualifier, String secondVersionQualifier) {
        if (firstVersionQualifier == null && secondVersionQualifier == null) {
            return 0;
        }

        if (firstVersionQualifier == null) {
            if (isStableOrFinal(secondVersionQualifier) || !isKnownQualifier(secondVersionQualifier)) {
                return -1;
            } else {
                return 1;
            }
        }

        if (secondVersionQualifier == null) {
            if (isStableOrFinal(firstVersionQualifier) || !isKnownQualifier(firstVersionQualifier)) {
                return 1;
            } else {
                return -1;
            }
        }

        if (releaseTypeUniqueInQualifier) {
            return compareReleaseTypesInQualifier(firstVersionQualifier, secondVersionQualifier);
        } else {
            return firstVersionQualifier.compareToIgnoreCase(secondVersionQualifier);
        }
    }

    private boolean isKnownQualifier(String qualifier) {
        String[] arrayValues = VersionReleaseTypes.getValues();
        Optional<String> optionalQualifier = Arrays.stream(arrayValues).filter(s -> s.equals(qualifier)).findFirst();

        return optionalQualifier.isPresent();
    }
}
