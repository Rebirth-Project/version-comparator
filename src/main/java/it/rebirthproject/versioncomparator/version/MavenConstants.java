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
package it.rebirthproject.versioncomparator.version;

public class MavenConstants {
    
    
//    private static final List<String> QUALIFIER_ORDER = Arrays.asList(
//            "alpha", "beta", "milestone", "rc", "cr", "snapshot", "", "final", "ga", "sp"
//    );


    public static final String IS_NUMBER_REGEX = "^\\d+$";

    public static final Character REPLACEBLE_SEPARATOR = '_';
    public static final Character HYPHEN_SEPARATOR = '-';
    public static final Character FULLSTOP_SEPARATOR = '.';

    public static final String ZERO = "0";
    public static final String EMPTY_STRING = "";
    public static final String GA_STRING = "ga";
    public static final String FINAL_STRING = "final";
}
