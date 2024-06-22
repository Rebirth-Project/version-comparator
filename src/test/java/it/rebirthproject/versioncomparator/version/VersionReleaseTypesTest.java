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

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class VersionReleaseTypesTest {
 
    @ParameterizedTest
    @CsvSource({
        ".DEV,true",
        "-DEV,true",
        "123,true",
        "123.SNAPSHOT,true",
        "123.SNAPSHOT.PRE_ALPHA,false",
        "123.RC-RC,false",
        "123.SNAPSHOT.ALFA,true",})
    public void qualifierUniquenessTest(String qualifiers, boolean expectedResult) {
        String regex = VersionReleaseTypes.getRegexToCheckReleaseTypeUniqueness();        
        assertEquals(expectedResult, qualifiers.matches(regex));
    }
}
