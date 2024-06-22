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
package it.rebirthproject.versioncomparator.version;

import java.util.List;

/**
 * The internal representatio of a version.
 */
public class Version {

    /**
     * The major version's number
     */
    private final long major;
    /**
     * The minuor version's number
     */
    private final long minor;
    /**
     * The patch version's number
     */
    private final long patch;
    /**
     * The version's qualifier
     */
    private final String qualifier;
    /**
     * The version's build metadata
     */
    private final String buildMetadata;
    /**
     * a boolean that states if the version is tokenized or not
     */
    private boolean isTokenized = false;

    /**
     * a tokenized list representing the version for Maven comparator
     */
    private List<String> tokenList = null;

    /**
     * The semantic version's contructor.
     *
     * @param major the major version's number
     * @param minor the minor version's number
     * @param patch the patch version's number
     * @param qualifier the version's qualifier
     * @param buildMetadata the version's build metadata
     */
    public Version(long major, long minor, long patch, String qualifier, String buildMetadata) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.qualifier = qualifier;
        this.buildMetadata = buildMetadata;
    }

    /**
     * The standard version's contructor.
     *
     * @param major the major version's number
     * @param minor the minor version's number
     * @param patch the patch version's number
     * @param qualifier the version's qualifier
     */
    public Version(long major, long minor, long patch, String qualifier) {
        this(major, minor, patch, qualifier, "");
    }

    /**
     * The minimal version's contructor.
     *
     * @param major the major version's number
     * @param minor the minor version's number
     * @param patch the patch version's number
     */
    public Version(long major, long minor, long patch) {
        this(major, minor, patch, "", "");
    }

    /**
     * The Maven version's contructor.
     *
     *
     */
    public Version(List<String> tokenList) {
        this(-1, -1, -1, "", "");
        this.isTokenized = true;
        this.tokenList = tokenList;
    }

    /**
     *
     * @return the major version number.
     */
    public long getMajor() {
        return major;
    }

    /**
     *
     * @return the minor version number.
     */
    public long getMinor() {
        return minor;
    }

    /**
     *
     * @return the patch version number.
     */
    public long getPatch() {
        return patch;
    }

    /**
     *
     * @return the version string qualifier.
     */
    public String getQualifier() {
        return qualifier;
    }

    /**
     *
     * @return the version string build metadata.
     */
    public String getBuildMetadata() {
        return buildMetadata;
    }

    /**
     *
     * @return a boolean that states if the version is tokenized or not.
     */
    public boolean isIsTokenized() {
        return isTokenized;
    }

    /**
     *
     * @return the tokenized version.
     */
    public List<String> getTokenList() {
        return tokenList;
    }
}
