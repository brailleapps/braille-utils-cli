[![Type](https://img.shields.io/badge/type-application-blue.svg)](https://github.com/brailleapps/wiki/wiki/Badges)

Note: This project has been merged into Dotify (https://github.com/brailleapps/dotify-cli) and 
will not be updated anymore. Please use Dotify in the future.

# BrailleUtils #
BrailleUtils provides a command line interface for embossing and converting braille in PEF-format. Conversion to and from commonly used "braille" text formats is also supported.

Braille Utils supports a range of embossers, including popular [Index](http://www.indexbraille.com/) and [Braillo](http://www.braillo.com/) embossers. Note however that several embossers are untested, due to lack of access and/or time.

## Main Features ##
  * Emboss a PEF-file
  * Validate a PEF-file
  * Search meta data in collection of PEF-files
  * Convert from text to a PEF-file
  * Convert from a PEF-file to text
  * Split a PEF-file into one file per volume
  * Merge several PEF-files into one
 
## Using ##
Download the [latest release](https://github.com/brailleapps/braille-utils-cli/releases) and unpack it. For more information see, the user guide
in the `docs` folder.

## Building ##
Build with `gradlew build` (Windows) or `./gradlew build` (Mac/Linux)

## Testing ##
Tests are run with `gradlew test` (Windows) or `./gradlew test` (Mac/Linux)

## Requirements & Compatibility ##
* Requires Java 8
* Compatible with SPI

## More information ##
See the [common wiki](https://github.com/brailleapps/wiki/wiki) for more information.
