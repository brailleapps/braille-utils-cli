Note: This project has been merged into Dotify (https://github.com/brailleapps/dotify-cli) and 
will not be updated anymore. Please use Dotify in the future.

# Braille Utils #
Contributors: Joel Håkansson, Bert Frees
Code license: GNU Lesser GPL
Language: Java
Platform: Cross platform
Code URL: https://github.com/brailleapps/braille-utils-cli


## Contents ##
1	Description
2	Installation
3	Help Resources


## 1 Description ##
Braille Utils is a cross platform utility package for embossing and converting 
PEF-files.

Braille Utils provides a cross platform API for embossing and converting braille
in PEF-format. Conversion to and from commonly used "braille" text formats is
also supported.

The package is a command line interface is written in Java.

### Main Features ###
 * Emboss PEF-files
 * Convert to or from PEF-files
 * Split or merge PEF-files
 * Validate PEF-files

### Supported embossers ###
The package supports the following embosser families:
 * Braillo
 * Cidat
 * Enabling Technologies
 * Index Braille
 * Mountbatten
 * ViewPlus

For details, see the complete list of supported embossers by typing the following command:
  brailleutils list embossers

## 2 Installation ##
You can download a ready-to-run binary variant of this library from
  https://github.com/brailleapps/braille-utils-cli

A list of the dependencies is located in the developer documentation (e. g. in
the source under doc/).

Compilation is done just by
  % ./gradlew build
in the source directory. The built library can be found under build/distributions/.


## 3 Help Resources ##
See the latest version of the written documentation on
  https://github.com/brailleapps/braille-utils-cli
