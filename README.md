# NetAtlas C2 Server Search

![Build Status](https://img.shields.io/github/actions/workflow/status/michael2to3/c2-search-netlas/build.yml?branch=main)
[![License](https://img.shields.io/github/license/michael2to3/pretty-caldav-politech-schedule?style=flat-square)](https://github.com/michael2to3/pretty-caldav-politech-schedule/blob/main/LICENSE)
[![Junit Jupiter](https://img.shields.io/badge/Junit-Jupiter-green?style=flat-square)](https://junit.org/junit5/)
[![Gradle](https://img.shields.io/badge/Gradle-blue?style=flat-square)](https://gradle.org/)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/6d3c2810e4f844fa989a987f84565b7d)](https://app.codacy.com/gh/michael2to3/c2-search-netlas/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/6d3c2810e4f844fa989a987f84565b7d)](https://app.codacy.com/gh/michael2to3/c2-search-netlas/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage)

C2 Search Netlas is a Java utility to detect C2 servers using Netlas API. This utility provides a simple and easy-to-use CLI interface for searching C2 servers. It uses the Netlas API to fetch data and process it locally.

## Installation

Clone this repository to your local machine:

```bash
git clone https://github.com/michael2to3/c2-search-netlas.git
```

## Usage

To use this utility, you need to have a Netlas API key. You can get the key from the [Netlas](https://netlas.io/) website.

Once you have the API key, create a config.properties file in the root directory of the project and add the following line:

```bash
api.key=<your-netlas-api-key>
```

Now you can build the project and run it using the following commands:

```bash
./gradlew build
java -jar app/build/libs/c2-search-netlas-1.0-SNAPSHOT.jar --help
```

This will display the help message with available options.

To search for C2 servers, run the following command:

```bash
java -jar app/build/libs/c2-search-netlas-1.0-SNAPSHOT.jar -t <ip-or-domain> -p <port>
```

This will display a list of C2 servers found in the given IP address or domain.

## Support

| Name                    | Support            |
|-------------------------|--------------------|
| metasploit              | :white_check_mark: |
| HavocFramework/Havoc    | :question:         |
| Cobalt Strike           | :white_check_mark: |
| BishopFox/sliver        | :white_check_mark: |
| Empire                  | :x:                |
| Ne0nd0g/merlin          | :x:                |
| cobbr/Covenant          | :x:                |
| bruteratel              | :x:                |
| t3l3machus/Villain      | :x:                |
| bats3c/shad0w           | :x:                |
| nil0x42/phpsploit       | :x:                |
| nettitude/PoshC2        | :x:                |

Legend:

- :white_check_mark: - Accept/good support
- :question: - Support unknown/unclear
- :x: - No support/poor support

## Contributing

If you want to contribute to this project, please feel free to create a pull request.

## License

This project is licensed under the License - see the [LICENSE](https://github.com/michael2to3/c2-search-netlas/blob/main/LICENSE) file for details.
