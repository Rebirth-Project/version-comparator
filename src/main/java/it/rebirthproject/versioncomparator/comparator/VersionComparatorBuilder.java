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

import it.rebirthproject.versioncomparator.parser.MinimalVersionParser;
import it.rebirthproject.versioncomparator.parser.RelaxedSemanticVersionParser;
import it.rebirthproject.versioncomparator.parser.StrictSemanticVersionParser;
import it.rebirthproject.versioncomparator.parser.VersionMatchingParserType;
import static it.rebirthproject.versioncomparator.parser.VersionMatchingParserType.MINIMAL_LENGTH_VERSION;
import it.rebirthproject.versioncomparator.parser.VersionParser;

/**
 * A builder which can be used to create an {@link VersionComparator}
 */
public class VersionComparatorBuilder {

    /**
     * The type {@link VersionMatchingParserType} of parser used to check if a String 
     * corresponds to a formal {@link  it.rebirthproject.versioncomparator.version.Version}.
     */
    private VersionMatchingParserType versionMatchingParserType=VersionMatchingParserType.RELAXED_SEMANTIC_VERSION;
 
    /**
     * Sets the {@link #versionMatchingParserType} attribute to minimal version parser.
     *
     * @return The {@link VersionComparatorBuilder} instance configured with the
     * chosen minimal version parser.
     */
    public VersionComparatorBuilder useMinimalVersionParser() {
        this.versionMatchingParserType = VersionMatchingParserType.MINIMAL_LENGTH_VERSION;
        return this;
    }
    
    /**
     * Sets the {@link #versionMatchingParserType} attribute to strict semantic version parser.
     *
     * @return The {@link VersionComparatorBuilder} instance configured with the
     * chosen strict semantic version parser.
     */
    public VersionComparatorBuilder useStrictSemanticVersionParser() {
        this.versionMatchingParserType = VersionMatchingParserType.STRICT_SEMANTIC_VERSION_STANDARD;
        return this;
    }
    
     /**
     * Builds a {@link VersionComparator} configured by {@link VersionComparatorBuilder}'s
     * properties eventually set or with pre-configured default values
     *
     * @return an {@link VersionComparator} instance
     *
     */
    public VersionComparator build() {
        return new VersionComparator(createParser());
    }

     /**
     * Builds a {@link VersionParser} configured by {@link VersionComparatorBuilder}'s
     * properties eventually set or with pre-configured default values
     *
     * @return an {@link VersionParser} instance
     *
     */
    private VersionParser createParser() {
        switch (versionMatchingParserType) {
            case MINIMAL_LENGTH_VERSION:
                return new MinimalVersionParser();            
            case STRICT_SEMANTIC_VERSION_STANDARD:
                return new StrictSemanticVersionParser();
            default:
                return new RelaxedSemanticVersionParser();
        }
    }
}
