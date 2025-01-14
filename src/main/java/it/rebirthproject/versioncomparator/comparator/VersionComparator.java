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

import it.rebirthproject.versioncomparator.version.Version;
import java.util.Comparator;

/**
 * This class is used to compare two versions in String format.
 */
public interface VersionComparator extends Comparator<String> {

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
    public int compare(String version1, String version2) throws IllegalArgumentException;
}
