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
package it.rebirthproject.versioncomparator.parser;

import it.rebirthproject.versioncomparator.version.Version;

/**
 * The interface for a version parser.
 * 
 */
public interface VersionParser {    

    /**
     * The abstract method to parse a version
     * 
     * @param version the version in string format
     * @return the parsed version
     * @throws IllegalArgumentException if the string version does not match parsers' rules. 
     */
    public Version parseVersion(String version) throws IllegalArgumentException;
}
