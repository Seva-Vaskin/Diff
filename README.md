# Diff

## Description

Compare two files line by line.

*this program is analogue of standard bash utility diff* 

# _User guide_

## Run
* You can run this utility with this command:
```
./gradlew -q run --args="[OPTIONS] FILES"
```

## List of options
* ```--help``` display help message and exit

## Output format
For each block of changes the program prints:
```[range of deleted strings][a/b/c][range of added strings]```

```a``` stands for added, ```d``` for deleted and ```c``` for changed.

Then it prints changes:

```
< deleted strings
---
> added strings
```
The less-than and greater-than signs 
(at the beginning of lines that are added, deleted or changed)
indicate which file the lines appear in. 
Addition lines are added to the original file to appear in the
new file.
Deletion lines are deleted from the original file to be missing
in the new file.


By default, lines common to both files are not shown.
Lines that have moved are shown as added at their new location
and as deleted from their old location.

## Example of using
* ```src/test/files/textFromWikipediaOriginal.txt``` content:
```
This part of the
document has stayed the
same from version to
version.  It shouldn't
be shown if it doesn't
change.  Otherwise, that
would not be helping to
compress the size of the
changes.

This paragraph contains
text that is outdated.
It will be deleted in the
near future.

It is important to spell
check this dokument. On
the other hand, a
misspelled word isn't
the end of the world.
Nothing in the rest of
this paragraph needs to
be changed. Things can
be added after it.

```
* ```src/test/files/textFromWikipediaNew.txt``` content:
```
This is an important
notice! It should
therefore be located at
the beginning of this
document!

This part of the
document has stayed the
same from version to
version.  It shouldn't
be shown if it doesn't
change.  Otherwise, that
would not be helping to
compress the size of the
changes.

It is important to spell
check this document. On
the other hand, a
misspelled word isn't
the end of the world.
Nothing in the rest of
this paragraph needs to
be changed. Things can
be added after it.

This paragraph contains
important new additions
to this document.

```
* run utility with command:
```
./gradlew run --args="src/test/files/textFromWikipediaOriginal.txt src/test/files/textFromWikipediaNew.txt"
```
* Output:
```
0a1,6
> This is an important
> notice! It should
> therefore be located at
> the beginning of this
> document!
> 
10,14c16
< 
< This paragraph contains
< text that is outdated.
< It will be deleted in the
< near future.
---
> 
16c18
< check this dokument. On
---
> check this document. On
23a26,29
> 
> This paragraph contains
> important new additions
> to this document.
```

# Realization and testing

## About realisation
* This program is based on "the largest common subsequence (LCS)" algorithm.
1. It finds LCS of two files (it looks on files like on array of lines).
2. It tags all lines from first file but not from LCS as "deleted", all lines from
second file but not from LCS as "added" and all lines from LCS as "unchanged"
3. The result merges into blocks of changes for printing.
4. In the end program prints information about changes in each block.
## Benchmarks

* On pair of files ```src/test/files/warAndPeace.txt```, ```src/test/files/warAndPeaceChanged.txt``` (both files are about 3.6Mb)
this utility crashed because lack of memory. 
* But on pair ```src/test/files/warAndPeaceHead1000.txt``` and ```src/test/files/warAndPeaceChangedHead1000.txt```
  (only first 10000 lines of war and peace, about 1.3Mb) it works correctly

## Testing

tests covered more than 99% of code lines