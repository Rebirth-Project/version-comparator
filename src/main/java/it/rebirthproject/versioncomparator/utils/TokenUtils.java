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
package it.rebirthproject.versioncomparator.utils;

import it.rebirthproject.versioncomparator.version.MavenConstants;
import it.rebirthproject.versioncomparator.version.VersionReleaseTypes;

public class TokenUtils {    

    public static boolean isSeparator(char c) {
        return c == MavenConstants.HYPHEN_SEPARATOR || c == MavenConstants.FULLSTOP_SEPARATOR;
    }

    public static boolean isNumber(String token) {
        return token.matches(MavenConstants.IS_NUMBER_REGEX);
    }

    public static boolean isNullValue(String token) {
        return token.equals(MavenConstants.ZERO) || token.equals(VersionReleaseTypes.EMPTY.getValue()) || token.toUpperCase().equals(VersionReleaseTypes.GA.getValue()) || token.toUpperCase().equals(VersionReleaseTypes.FINAL.getValue());
    }
}
