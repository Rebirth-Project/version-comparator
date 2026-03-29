# Version Comparator ![Version Comparator Icon](icon/versioncomparator.png)
Version Comparator is an ultra-micro library written in java that lets you compare version numbers. 

**```Latest Version 1.1.2```**

![Build Status](https://github.com/Rebirth-Project/version-comparator/actions/workflows/build.yml/badge.svg?raw=true)

## Requirements
- Minimum Java version: 8
- Minimum Android version: 5.0 minSdkVersion 21

## Main features

* Micro library (~25k jar)
* Absolutely trivial to use
* No external dependencies 
* The code is clean, testable, compact and very easy to understand and maintain
* Completely covered with a large number of unit tests
* Easily extendable

## Goals
  * Provide a simple-to-use library to simplify version comparison
  * Make the code as clean and testable as possible
  * Don't rely on any other third-party library except for standard Java libraries
  * Obtain a jar as small as possible
  * Write good documentation for the library usage

## How to add Version Comparator dependency in your project

##### Gradle:

```
dependencies {
    implementation "it.rebirthproject:version-comparator:1.1.2"
}
```
##### Maven:

```
<dependency>
    <groupId>it.rebirthproject</groupId>
    <artifactId>version-comparator</artifactId>
    <version>1.1.2</version>
</dependency>
```

## Introduction

The version comparator can compare versions provided in string format. Currently, the library checks whether a passed version conforms to the internal parser rules. It throws an exception if an invalid version is provided as input. The library in its current state of the art has four different parsers to choose from:

1. a minimal parser (version format X.Y or X.Y.Z)
2. a semantic version parser (the standard semantic version you can find here  [https://semver.org/](https://semver.org/), that basically parses this version format X.Y.Z-QUALIFIER+buildmetadata)
3. a relaxed semantic version parser that is the default choice to build the comparator and is basically the version standard used by Maven artifacts (version format X.Y.Z-QUALIFIER where the qualifier should be a unique type string).
The qualifier types recognized by the relaxed parser are the following and in this order of priority: 

 - SNAPSHOT
 - PRE_ALPHA
 - ALPHA
 - BETA
 - MILESTONE
 - RC
 - STABLE
 - FINAL

4. a complete Maven's rules compliant version parser (you can find here the specs [https://maven.apache.org/pom.html#version-order-specification](https://maven.apache.org/pom.html#version-order-specification))

### Minimal parser format

The minimal parser accepts versions in the form `X.Y` or `X.Y.Z`, where:
- `X` = major
- `Y` = minor
- `Z` = optional patch

Examples of valid minimal versions:
- `1.2`
- `1.2.0`
- `10.0.99999`

Notes:
- Numeric parts cannot have leading zeros (except `0` itself), e.g. `01.2.3` is invalid.
- If patch is omitted (`X.Y`), it is considered lower than an explicit patch value during comparison (for example `1.2 < 1.2.0`).

### Strict semantic parser format

The strict semantic parser validates versions according to Semantic Versioning rules (https://semver.org/).

Expected format:
- `X.Y.Z`
- optional pre-release part: `-QUALIFIER`
- optional build metadata: `+BUILDMETADATA`

Examples of valid strict semantic versions:
- `1.2.3`
- `1.2.3-alpha`
- `1.2.3-alpha.1`
- `1.2.3+build.2024`
- `1.2.3-rc.1+build.5`

Notes:
- Major, minor and patch are mandatory.
- Numeric identifiers cannot contain leading zeros (except `0` itself).
- Invalid examples: `1.2`, `01.2.3`, `1.2.3-01`.

### Relaxed semantic parser format (default)

The relaxed semantic parser is the default parser used by the builder and supports a Maven-like qualifier style.

Expected format:
- `X.Y.Z`
- optional qualifier appended to the numeric part (for example `-RC`, `.FINAL`, `SNAPSHOT`)

Examples of valid relaxed semantic versions:
- `1.2.3`
- `1.2.3-RC`
- `1.2.3.FINAL`
- `1.2.3SNAPSHOT`
- `10.20.30-DEV-SNAPSHOT`

Notes:
- This parser is intentionally less strict than full Semantic Versioning.
- If a known release type is used in the qualifier, it must be unique (for example avoid combinations like `SNAPSHOT-RC`).
- Build metadata using `+...` is not supported by this parser.

### Maven rules parser format

The Maven rules parser follows Maven version ordering rules:
https://maven.apache.org/pom.html#version-order-specification

Accepted versions can include:
- numbers
- separators (`.` and `-`)
- qualifiers (for example `alpha`, `beta`, `rc`, `snapshot`, `final`, `ga`, `sp`)
- mixed token transitions (for example `foo1`, `1foo`)

Examples of valid Maven versions:
- `1`
- `1.0`
- `1.0-rc1`
- `1.0-final`
- `1-1.foo-bar1baz-.1`

Notes:
- Version comparison follows Maven precedence semantics, including known qualifier ordering.
- Invalid characters are rejected.
- Numeric tokens with leading zeros are rejected (for example `1.02`, `1-beta.09`).

## Usage

``` java

//create a default relaxed semantic version comparator
//version to check is in this format X.Y.Z-QUALIFIER
VersionComparator vc = new VersionComparatorBuilder().build();

//create a minimal version comparator 
//version to check is in this format X.Y or X.Y.Z
VersionComparator vc = new VersionComparatorBuilder().useMinimalVersionParser().build();

//create a semantic version comparator (https://semver.org/)
//version to check is in this format X.Y.Z-QUALIFIER+buildmetadata
VersionComparator vc = new VersionComparatorBuilder().useStrictSemanticVersionParser().build();

//create a maven's rules version comparator (https://maven.apache.org/pom.html#version-order-specification)
//version to check is in different formats as stated by the maven's document. For example: 1-1.foo-bar1baz-.1 
VersionComparator vc = new VersionComparatorBuilder().useMavenRulesVersionParser().build();

//use the version comparator
try {     
    int comp1=vc.compare("1.0.0-FINAL", "1.0.0-RC");
    int comp2=vc.compare("1.0.0", "1.0.1");
    int comp3=vc.compare("1.0.0-FINAL+build2024", "1.0.0-RC+build2023");
    int comp4=vc.compare("1-1.foo-bar1baz-.1", "1-1.bar");
} catch (IllegalArgumentException ex) {
    //Do something here in case of exception
}

```

## Contributors

If you would like to help, but don't know where to start, please note that finding bugs and debugging the code is always a good start.
Simple Pull Requests that fix anything other than Version Comparator core code (documentation, JavaDoc, typos, test cases, etc) are 
always appreciated and would be merged quickly.
However, if you want or feel the need to change the main code or add a new functionality, please do not issue a pull request 
without [creating a new  issue](https://github.com/Rebirth-Project/version-comparator/issues/new) and discussing your desired 
changes,  _**before you start working on it**_.
It would be a shame to reject your pull request if it might not align with the project's goals, design expectations or planned functionality.
 
For direct communications, you can use this [email](mailto:rebirthproject2021@gmail.com)

## Credits and License
Copyright (C) 2024/2025 [Andrea Paternesi](https://github.com/patton73)

Copyright (C) 2024/2025 [Matteo Veroni](https://github.com/mavek87)
 
Current website under creation [Rebirth Project](https://www.rebirth-project.it)

Version Comparator binaries and source code can be used according to the [Apache License, Version 2.0](LICENSE.md).
