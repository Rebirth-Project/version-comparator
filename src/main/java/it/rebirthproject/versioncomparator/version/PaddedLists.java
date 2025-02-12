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
package it.rebirthproject.versioncomparator.version;

import java.util.List;

public class PaddedLists {

    private final List<String> version1;
    private final List<String> version2;

    public PaddedLists(List<String> version1, List<String> version2) {
        this.version1 = version1;
        this.version2 = version2;
    }

    public List<String> getVersion1() {
        return version1;
    }

    public List<String> getVersion2() {
        return version2;
    }
}
