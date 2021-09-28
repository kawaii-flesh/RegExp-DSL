## [Examples](https://github.com/kawaii-flesh/Regexp-DSL/blob/master/src/test/kotlin/RegexpBuilderTest.kt)

## Characters

| Construct | Equivalent                                  | Matches                                                            |
|-----------|---------------------------------------------|--------------------------------------------------------------------|
| x         | character(Char)                             | The character x                                                    |
| \\\\      | character('\\\\')                           | The backslash character                                            |
| \0n       | octal(OctalValue(7))                        | The character with octal value 0n (0 \<= n \<= 7)                  |
| \0nn      | octal(OctalValue(7, 7))                     | The character with octal value 0nn (0 \<= n \<= 7)                 |
| \0mnn     | octal(OctalValue(3, 7, 7))                  | The character with octal value 0mnn (0 \<= m \<= 3, 0 \<= n \<= 7) |
| \xhh      | hexdecimal(HexdecimalValue('F', 'F'))       | The character with hexadecimal value 0xhh                          |
| \uhhhh    | unicode(UnicodeValue('F', 'F', 'F', 'F'))   | The character with hexadecimal value 0xhhhh                        |
| \t        | tab()                                       | The tab character ('\u0009')                                       |
| \n        | newLine()                                   | The newline (line feed) character ('\u000A')                       |
| \r        | carriageReturn()                            | The carriage-return character ('\u000D')                           |
| \f        | formFeed()                                  | The form-feed character ('\u000C')                                 |
| \a        | alert()                                     | The alert (bell) character ('\u0007')                              |
| \e        | escape()                                    | The escape character ('\u001B')                                    |
| \cx       | control(Char)                               | The control character corresponding to x                           |
|           | string(String)                              | The character sequence                                             |

## Character classes

| Construct    | Equivalent                                                                                                                              | Matches                                               |
|--------------|-----------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------|
| [abc]        | characterClass(Boolean(false), CharacterClassBuilder.()-\>Unit){...}                                                                    | a, b, or c (simple class)                             |
| [^abc]       | characterClass(Boolean(true), CharacterClassBuilder.()-\>Unit){...}                                                                     | Any character except a, b, or c (negation)            |
| [a-zA-Z]     | characterClass(Boolean(false), CharacterClassBuilder.()-\>Unit){range('a', 'z') …}                                                      | a through z or A through Z, inclusive (range)         |
| [a-d[m-p]]   | characterClass(Boolean(false), CharacterClassBuilder.()-\>Unit){… characterClass{...}}                                                  | a through d, or m through p: [a-dm-p] (union)         |
| [a-z&&[def]] | characterClass(Boolean(false), CharacterClassBuilder.()-\>Unit){… intersection(Boolean(false), CharacterClassBuilder.()-\>Unit){…}}     | d, e, or f (intersection)                             |
| [a-z&&[^bc]] | characterClass(Boolean(false), CharacterClassBuilder.()-\>Unit){… intersection(Boolean(true), CharacterClassBuilder.()-\>Unit){…}}      | a through z, except for b and c: [ad-z] (subtraction) |

## Predefined character classes

| Construct | Equivalent                         | Matches                                                                                 |
|-----------|------------------------------------|-----------------------------------------------------------------------------------------|
| .         | anyCharacter()                     | Any character (may or may not match line terminators)                                   |
| \d        | digit()                            | A digit: [0-9]                                                                          |
| \D        | nonDigit()                         | A non-digit: [^0-9]                                                                     |
| \h        | horizontalWhitespaceCharacter()    | A horizontal whitespace character: [ \t\xA0\u1680\u180e\u2000-\u200a\u202f\u205f\u3000] |
| \H        | nonHorizontalWhitespaceCharacter() | A non-horizontal whitespace character: [^\h]                                            |
| \s        | whitespaceCharacter()              | A whitespace character: [ \t\n\x0B\f\r]                                                 |
| \S        | noneWhitespaceCharacter()          | A non-whitespace character: [^\s]                                                       |
| \v        | verticalWhitespaceCharacter()      | A vertical whitespace character: [\n\x0B\f\r\x85\u2028\u2029]                           |
| \V        | noneVerticalWhitespaceCharacter()  | A non-vertical whitespace character: [^\v]                                              |
| \w        | wordCharacter()                    | A word character: [a-zA-Z_0-9]                                                          |
| \W        | noneWordCharacter()                | A non-word character: [^\w]                                                             |

## POSIX character classes (US-ASCII only)

| Construct  | Equivalent             | Matches                                                 |
|------------|------------------------|---------------------------------------------------------|
| \p{Lower}  | pLowerCase()           | A lower-case alphabetic character: [a-z]                |
| \p{Upper}  | pUpperCase()           | An upper-case alphabetic character:[A-Z]                |
| \p{ASCII}  | pASCII()               | All ASCII:[\x00-\x7F]                                   |
| \p{Alpha}  | pAlphabetic()          | An alphabetic character:[\p{Lower}\p{Upper}]            |
| \p{Digit}  | pDecimalDigit()        | A decimal digit: [0-9]                                  |
| \p{Alnum}  | pAlphanumeric()        | An alphanumeric character:[\p{Alpha}\p{Digit}]          |
| \p{Punct}  | pPunctuation()         | Punctuation: One of !"#$%&'()*+,-./:;\<=\>?@[\]^_`{\|}~ |
| \p{Graph}  | pVisible()             | A visible character: [\p{Alnum}\p{Punct}]               |
| \p{Print}  | pPrintable()           | A printable character: [\p{Graph}\x20]                  |
| \p{Blank}  | pSpaceOrTab()          | A space or a tab: [ \t]                                 |
| \p{Cntrl}  | pControl()             | A control character: [\x00-\x1F\x7F]                    |
| \p{XDigit} | pHexadecimal()         | A hexadecimal digit: [0-9a-fA-F]                        |
| \p{Space}  | pWhitespaceCharacter() | A whitespace character: [ \t\n\x0B\f\r]                 |

## java.lang.Character classes (simple java character type)

| Construct          | Equivalent     | Matches                                          |
|--------------------|----------------|--------------------------------------------------|
| \p{javaLowerCase}  | pjLowerCase()  | Equivalent to java.lang.Character.isLowerCase()  |
| \p{javaUpperCase}  | pjUpperCase()  | Equivalent to java.lang.Character.isUpperCase()  |
| \p{javaWhitespace} | pjWhitespace() | Equivalent to java.lang.Character.isWhitespace() |
| \p{javaMirrored}   | pjMirrored()   | Equivalent to java.lang.Character.isMirrored()   |

## Classes for Unicode scripts, blocks, categories and binary properties

| Construct        | Equivalent           | Matches                                                |
|------------------|----------------------|--------------------------------------------------------|
| \p{IsLatin}      | pLatin()             | A Latin¬†script character (script)                     |
| \p{InGreek}      | pGreek()             | A character in the Greek¬†block (block)                |
| \p{Lu}           | pUpperCaseCategory() | An uppercase letter (category)                         |
| \p{IsAlphabetic} | pAlphabeticBP()      | An alphabetic character (binary property)              |
| \p{Sc}           | pCurrencySymbol()    | A currency symbol                                      |
| \P{InGreek}      | pExceptGreek()       | Any character except one in the Greek block (negation) |
| \p{L}            | pAnyLetter()         | Any letter                                             |

## Boundary matchers

| Construct | Equivalent                   | Matches                                                    |
|-----------|------------------------------|------------------------------------------------------------|
| ^         | beginLine()                  | The beginning of a line                                    |
| $         | endLine()                    | The end of a line                                          |
| \b        | wordBoundary()               | A word boundary                                            |
| \B        | noneWordBoundary()           | A non-word boundary                                        |
| \A        | beginInput()                 | The beginning of the input                                 |
| \G        | endPreviousMatch()           | The end of the previous match                              |
| \Z        | endInputForFinalTerminator() | The end of the input but for the final terminator, ifÂ any |
| \z        | endInput()                   | The end of the input                                       |

## Linebreak matcher

| Construct | Equivalent         | Matches                                                                                                     |
|-----------|--------------------|-------------------------------------------------------------------------------------------------------------|
| \R        | unicodeLinebreak() | Any Unicode linebreak sequence, is equivalent to \u000D\u000A\|[\u000A\u000B\u000C\u000D\u0085\u2028\u2029] |

## Quantifiers

| Construct | Equivalent      | Matches                                 |
|-----------|-----------------|-----------------------------------------|
| X?        | missingOrOne()  | X, once or not at all                   |
| X*        | missingOrMore() | X, zero or more times                   |
| X+        | oneOrMore()     | X, one or more times                    |
| X{n}      | nExactly(Int)   | X, exactly n times                      |
| X{n,}     | nOrMore(Int)    | X, at least n times                     |
| X{n,m}    | nToM(Int, Int)  | X, at least n but not more than m times |

## Logical operators

| Construct | Equivalent                         | Matches                 |
|-----------|------------------------------------|-------------------------|
| X\|Y      | or()                               | Either X or Y           |
| (X)       | group(RegexBuilder.()-\>Unit){...} | X, as a capturing group |

## Back references

| Construct| Equivalent                            | Matches                                           |
|----------|---------------------------------------|---------------------------------------------------|
| \k\<name\> | namedCapturingGroupReference(String)| Whatever the named-capturing group "name" matched |

## Quotation

| Construct | Equivalent     | Matches                                     |
|-----------|----------------|---------------------------------------------|
| \Q        | startQuoting() | Nothing, but quotes all characters until \E |
| \E        | endQuoting()   | Nothing, but ends quoting started by \Q     |

## Special constructs (named-capturing and non-capturing)

| Construct    | Equivalent                                            | Matches                                     |
|--------------|-------------------------------------------------------|---------------------------------------------|
| (?\<name\>X) | namedCapturingGroup(String, RegexBuilder.()-\>Unit){}   | X, as a named-capturing group             |
| (?:X)        | nonCapturingGroup(String, RegexBuilder.()-\>Unit){}     | X, as a non-capturing group               |
| (?=X)        | positiveLookAhead(RegexBuilder.()-\>Unit){}            | X, via zero-width positive lookahead       |
| (?!X)        | negativeLookAhead(RegexBuilder.()-\>Unit){}             | X, via zero-width negative lookahead      |
| (?\<=X)      | positiveLookBehind(RegexBuilder.()-\>Unit){}           | X, via zero-width positive lookbehind      |
| (?\<!X)      | negativeLookBehind(RegexBuilder.()-\>Unit){}            | X, via zero-width negative lookbehind     |
| (?\>X)       | independentNoneCapturingGroup(RegexBuilder.()-\>Unit){} | X, as an independent, non-capturing group |