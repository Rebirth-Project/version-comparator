/*
 * Copyright (C) 2024 Andrea Paternesi Rebirth project
 * Modifications copyright (C) 2021/2024 Matteo Veroni Rebirth project
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

public class TokenUtils {    

    public static boolean isRealSeparator(char c) {
        return c == MavenConstants.HYPHEN_SEPARATOR || c == MavenConstants.FULLSTOP_SEPARATOR;
    }

    public static boolean isNumber(String token) {
        return token.matches(MavenConstants.IS_NUMBER_REGEX);
    }

    public static boolean isNullValue(String token) {
        return token.equals(MavenConstants.ZERO) || token.equals(MavenConstants.EMPTY_STRING) || token.equals(MavenConstants.GA_STRING) || token.equals(MavenConstants.FINAL_STRING);
    }
}