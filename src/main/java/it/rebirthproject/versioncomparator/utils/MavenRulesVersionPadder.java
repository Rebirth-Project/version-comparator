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
import it.rebirthproject.versioncomparator.version.PaddedLists;
import java.util.ArrayList;
import java.util.List;

public class MavenRulesVersionPadder {

    public PaddedLists padShorterVersion(List<String> version1, List<String> version2) {
        List<String> shorterList = version1.size() < version2.size() ? version1 : version2;
        List<String> longerList = shorterList == version1 ? version2 : version1;
        int numElementsToPad = longerList.size() - shorterList.size();

        List<String> paddedList = new ArrayList<>(shorterList);
        for (int i = 0; i < numElementsToPad; i++) {
            paddedList.add(determinePadValue(longerList.get(shorterList.size() + i)));
        }

        //We want the input lists order to be the same in the output
        if (version1 == shorterList) {
            return new PaddedLists(paddedList, longerList);
        } else {
            return new PaddedLists(longerList, paddedList);
        }
    }

    private String determinePadValue(String otherToken) {
        char firstChar = otherToken.charAt(0);
        if (TokenUtils.isSeparator(firstChar)) {
            return "" + firstChar;
        } else if (Character.isDigit(firstChar)) {
            return MavenConstants.ZERO;
        } else {
            return "";
        }
    }
}
