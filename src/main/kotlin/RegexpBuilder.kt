//https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html

@DslMarker
annotation class RegexpDSL

fun makeRegexp(options: Set<RegexOption> = emptySet(), init: RegexpBuilder.()->Unit): Regex
{
    val builder = RegexpBuilder()
    init(builder)
    return builder.buildAsRegex(options)
}

fun makeRegexp(option: RegexOption,  p: RegexpBuilder.()->Unit): Regex
{
    val builder = RegexpBuilder()
    p(builder)
    return builder.buildAsRegex(option)
}

fun makeRegexpAsString(p: RegexpBuilder.()->Unit): String
{
    val builder = RegexpBuilder()
    p(builder)
    return builder.buildAsString()
}

@RegexpDSL
class RegexpBuilder
{
    private val stringBuilder = StringBuilder()

    fun buildAsString() = stringBuilder.toString()
    fun buildAsRegex(options: Set<RegexOption> = emptySet()): Regex = Regex(buildAsString(), options)
    fun buildAsRegex(option: RegexOption): Regex = Regex(buildAsString(), option)

    fun characterClass(except: Boolean = false, init: CharacterClassBuilder.()->Unit): Unit
    {
        val characterClass = CharacterClassBuilder(except)
        init(characterClass)
        stringBuilder.append(characterClass.build())
    }
    fun group(init: RegexpBuilder.()->Unit): Unit
    {
        stringBuilder.append('(')
        init()
        stringBuilder.append(')')
    }
    fun anyCharacter(): Unit {stringBuilder.append('.')}
    fun character(character: Char): Unit
    {
        val newC = when(character)
        {
            '.', '*', '+', '?', '{', '}', '[', ']', '\\', '\$', '^' -> "\\$character"
            else -> character
        }
        stringBuilder.append(newC)
    }
    fun string(string: String): Unit
    {
        stringBuilder.append(string.
        replace(".", "\\.").
        replace("*", "\\*").
        replace("+", "\\+").
        replace("?", "\\?").
        replace("{", "\\{").
        replace("}", "\\}").
        replace("[", "\\[").
        replace("]", "\\]").
        replace("\\", "\\\\").
        replace("\$", "\\\$").
        replace("^", "\\^"))
    }
    
    fun missingOrOne(): Unit {stringBuilder.append('?')}
    fun missingOrMore(): Unit {stringBuilder.append('*')}
    fun oneOrMore(): Unit {stringBuilder.append('+')}
    fun nExactly(n: Int): Unit {stringBuilder.append("{$n}")}
    fun nOrMore(n: Int): Unit {stringBuilder.append("{$n,}")}
    fun nToM(n: Int, m: Int): Unit {stringBuilder.append("{$n,$m}")}
    fun nToM(nm: IntRange): Unit {stringBuilder.append("{${nm.first},${nm.last}}")}

    fun or(): Unit {stringBuilder.append('|')}

    fun octal(octalValue: OctalValue): Unit {stringBuilder.append("\\0${octalValue.octal}")}
    fun hexdecimal(hexdecimalValue: HexdecimalValue): Unit {stringBuilder.append("\\x${hexdecimalValue.hexdecimal}")}
    fun unicode(unicodeValue: UnicodeValue): Unit {stringBuilder.append("\\u${unicodeValue.unicode}")}

    fun tab(): Unit {stringBuilder.append("\\t")}
    fun newLine(): Unit {stringBuilder.append("\\n")}
    fun carriageReturn(): Unit {stringBuilder.append("\\r")}
    fun formFeed(): Unit {stringBuilder.append("\\f")}
    fun alert(): Unit {stringBuilder.append("\\a")}
    fun escape(): Unit {stringBuilder.append("\\e")}
    fun control(character: Char): Unit {stringBuilder.append("\\c$character")}

    fun digit(): Unit {stringBuilder.append("\\d")}
    fun nonDigit(): Unit {stringBuilder.append("\\D")}
    fun horizontalWhitespaceCharacter(): Unit {stringBuilder.append("\\h")}
    fun nonHorizontalWhitespaceCharacter(): Unit {stringBuilder.append("\\H")}
    fun whitespaceCharacter(): Unit {stringBuilder.append("\\s")}
    fun noneWhitespaceCharacter(): Unit {stringBuilder.append("\\S")}
    fun verticalWhitespaceCharacter(): Unit {stringBuilder.append("\\v")}
    fun noneVerticalWhitespaceCharacter(): Unit {stringBuilder.append("\\V")}
    fun wordCharacter(): Unit {stringBuilder.append("\\w")}
    fun noneWordCharacter(): Unit {stringBuilder.append("\\W")}

    fun beginLine(): Unit {stringBuilder.append('^')}
    fun endLine(): Unit {stringBuilder.append('$')}

    fun wordBoundary(): Unit {stringBuilder.append("\\b")}
    fun noneWordBoundary(): Unit {stringBuilder.append("\\B")}
    fun beginInput(): Unit {stringBuilder.append("\\A")}
    fun endPreviousMatch(): Unit {stringBuilder.append("\\G")}
    fun endInputForFinalTerminator(): Unit {stringBuilder.append("\\Z")}
    fun endInput(): Unit {stringBuilder.append("\\z")}

    fun unicodeLinebreak(): Unit {stringBuilder.append("\\R")}

    fun pLowerCase(): Unit {stringBuilder.append("\\p{Lower}")}
    fun pUpperCase(): Unit {stringBuilder.append("\\p{Upper}")}
    fun pASCII(): Unit {stringBuilder.append("\\p{ASCII}")}
    fun pAlphabetic(): Unit {stringBuilder.append("\\p{Alpha}")}
    fun pDecimalDigit(): Unit {stringBuilder.append("\\p{Digit}")}
    fun pAlphanumeric(): Unit {stringBuilder.append("\\p{Alnum}")}
    fun pPunctuation(): Unit {stringBuilder.append("\\p{Punct}")}
    fun pVisible(): Unit {stringBuilder.append("\\p{Graph}")}
    fun pPrintable(): Unit {stringBuilder.append("\\p{Print}")}
    fun pSpaceOrTab(): Unit {stringBuilder.append("\\p{Blank}")}
    fun pControl(): Unit {stringBuilder.append("\\p{Cntrl}")}
    fun pHexadecimal(): Unit {stringBuilder.append("\\p{XDigit}")}
    fun pWhitespaceCharacter(): Unit {stringBuilder.append("\\p{Space}")}

    fun pjLowerCase(): Unit {stringBuilder.append("\\p{javaLowerCase}")}
    fun pjUpperCase(): Unit {stringBuilder.append("\\p{javaUpperCase}")}
    fun pjWhitespace(): Unit {stringBuilder.append("\\p{javaWhitespace}")}
    fun pjMirrored(): Unit {stringBuilder.append("\\p{javaMirrored}")}

    fun pLatin(): Unit {stringBuilder.append("\\p{IsLatin}")}
    fun pGreek(): Unit {stringBuilder.append("\\p{InGreek}")}
    fun pUpperCaseCategory(): Unit {stringBuilder.append("\\p{Lu}")}
    fun pAlphabeticBP(): Unit {stringBuilder.append("\\p{IsAlphabetic}")}
    fun pCurrencySymbol(): Unit {stringBuilder.append("\\p{Sc}")}
    fun pExceptGreek(): Unit {stringBuilder.append("\\P{InGreek}")}
    fun pAnyLetter(): Unit {stringBuilder.append("\\p{L}")}

    fun startQuoting(): Unit {stringBuilder.append("\\Q")}
    fun endQuoting(): Unit {stringBuilder.append("\\E")}

    fun namedCapturingGroupReference(name: String): Unit {stringBuilder.append("\\k<$name>")}
    fun namedCapturingGroup(name: String, init: RegexpBuilder.()->Unit): Unit
    {
        stringBuilder.append("(?<$name>")
        init()
        stringBuilder.append(')')
    }
    fun nonCapturingGroup(init: RegexpBuilder.()->Unit): Unit
    {
        stringBuilder.append("(?:")
        init()
        stringBuilder.append(')')
    }
    fun positiveLookAhead(init: RegexpBuilder.()->Unit): Unit
    {
        stringBuilder.append("(?=")
        init()
        stringBuilder.append(')')
    }
    fun negativeLookAhead(init: RegexpBuilder.()->Unit): Unit
    {
        stringBuilder.append("(?!")
        init()
        stringBuilder.append(')')
    }
    fun positiveLookBehind(init: RegexpBuilder.()->Unit): Unit
    {
        stringBuilder.append("(?<=")
        init()
        stringBuilder.append(')')
    }
    fun negativeLookBehind(init: RegexpBuilder.()->Unit): Unit
    {
        stringBuilder.append("(?<!")
        init()
        stringBuilder.append(')')
    }
    fun independentNoneCapturingGroup(init: RegexpBuilder.()->Unit): Unit
    {
        stringBuilder.append("(?>")
        init()
        stringBuilder.append(')')
    }

    @RegexpDSL
    class CharacterClassBuilder(private val except: Boolean = false)
    {
        private val stringBuilder = StringBuilder()

        fun build(): String = "[${if(except) '^' else ""}$stringBuilder]"

        fun characterClass(init: CharacterClassBuilder.()->Unit): Unit
        {
            val characterClass = CharacterClassBuilder()
            init(characterClass)
            stringBuilder.append(characterClass.build())
        }
        fun intersection(except: Boolean = false, p: CharacterClassBuilder.()->Unit): Unit
        {
            val exceptChar = if(except) "^" else ""
            stringBuilder.append("&&[$exceptChar")
            p()
            stringBuilder.append(']')
        }
        fun range(charRange: CharRange): Unit
        {
            stringBuilder.append("${charRange.first}-${charRange.last}")
        }
        fun range(first: Char, last: Char)
        {
            stringBuilder.append("$first-$last")
        }
        fun range(vararg firstLast: Pair<Char, Char>)
        {
            for((first, last) in firstLast)
                stringBuilder.append("$first-$last")
        }
        fun range(vararg firstLast: CharRange)
        {
            for(charRange in firstLast)
                stringBuilder.append("${charRange.first}-${charRange.last}")
        }
        fun character(character: Char): Unit
        {
            stringBuilder.append(when(character)
            {
                '^' -> "\\^"
                else -> character
            })
        }
        fun string(string: String): Unit
        {
            stringBuilder.append(string.
            replace("\\", "\\\\").
            replace("[", "\\[").
            replace("]", "\\]"))
        }

        fun octal(octalValue: OctalValue): Unit {stringBuilder.append("\\0${octalValue.octal}")}
        fun hexdecimal(hexdecimalValue: HexdecimalValue): Unit {stringBuilder.append("\\x${hexdecimalValue.hexdecimal}")}
        fun unicode(unicodeValue: UnicodeValue): Unit {stringBuilder.append("\\u${unicodeValue.unicode}")}

        fun tab(): Unit {stringBuilder.append("\\t")}
        fun newLine(): Unit {stringBuilder.append("\\n")}
        fun carriageReturn(): Unit {stringBuilder.append("\\r")}
        fun formFeed(): Unit {stringBuilder.append("\\f")}
        fun alert(): Unit {stringBuilder.append("\\a")}
        fun escape(): Unit {stringBuilder.append("\\e")}
        fun control(character: Char): Unit {stringBuilder.append("\\c$character")}

        fun digit(): Unit {stringBuilder.append("\\d")}
        fun nonDigit(): Unit {stringBuilder.append("\\D")}
        fun horizontalWhitespaceCharacter(): Unit {stringBuilder.append("\\h")}
        fun nonHorizontalWhitespaceCharacter(): Unit {stringBuilder.append("\\H")}
        fun whitespaceCharacter(): Unit {stringBuilder.append("\\s")}
        fun noneWhitespaceCharacter(): Unit {stringBuilder.append("\\S")}
        fun verticalWhitespaceCharacter(): Unit {stringBuilder.append("\\v")}
        fun noneVerticalWhitespaceCharacter(): Unit {stringBuilder.append("\\V")}
        fun wordCharacter(): Unit {stringBuilder.append("\\w")}
        fun noneWordCharacter(): Unit {stringBuilder.append("\\W")}

        fun pLowerCase(): Unit {stringBuilder.append("\\p{Lower}")}
        fun pUpperCase(): Unit {stringBuilder.append("\\p{Upper}")}
        fun pASCII(): Unit {stringBuilder.append("\\p{ASCII}")}
        fun pAlphabetic(): Unit {stringBuilder.append("\\p{Alpha}")}
        fun pDecimalDigit(): Unit {stringBuilder.append("\\p{Digit}")}
        fun pAlphanumeric(): Unit {stringBuilder.append("\\p{Alnum}")}
        fun pPunctuation(): Unit {stringBuilder.append("\\p{Punct}")}
        fun pVisible(): Unit {stringBuilder.append("\\p{Graph}")}
        fun pPrintable(): Unit {stringBuilder.append("\\p{Print}")}
        fun pSpaceOrTab(): Unit {stringBuilder.append("\\p{Blank}")}
        fun pControl(): Unit {stringBuilder.append("\\p{Cntrl}")}
        fun pHexadecimal(): Unit {stringBuilder.append("\\p{XDigit}")}
        fun pWhitespaceCharacter(): Unit {stringBuilder.append("\\p{Space}")}

        fun pjLowerCase(): Unit {stringBuilder.append("\\p{javaLowerCase}")}
        fun pjUpperCase(): Unit {stringBuilder.append("\\p{javaUpperCase}")}
        fun pjWhitespace(): Unit {stringBuilder.append("\\p{javaWhitespace}")}
        fun pjMirrored(): Unit {stringBuilder.append("\\p{javaMirrored}")}

        fun pLatin(): Unit {stringBuilder.append("\\p{IsLatin}")}
        fun pGreek(): Unit {stringBuilder.append("\\p{InGreek}")}
        fun pUpperCaseCategory(): Unit {stringBuilder.append("\\p{Lu}")}
        fun pAlphabeticBP(): Unit {stringBuilder.append("\\p{IsAlphabetic}")}
        fun pCurrencySymbol(): Unit {stringBuilder.append("\\p{Sc}")}
        fun pExceptGreek(): Unit {stringBuilder.append("\\P{InGreek}")}
        fun pAnyLetter(): Unit {stringBuilder.append("\\p{L}")}
    }
}

class OctalValue(a: Int = 0, b: Int = 0, c: Int)
{
    var octal: Int = 0
    private set
    init
    {
        if((a !in 0..3) || (b !in 0..7) || (c !in 0..7))
            throw IllegalArgumentException("The number must be in the range from 0 to 377 inclusive")
        octal = a*100 + b*10 + c
    }
}

class HexdecimalValue(a: Char = '0', b: Char)
{
    lateinit var hexdecimal: String
    private set
    init
    {
        if((a !in '0'..'9' && a.toLowerCase() !in 'a'..'f') ||
            (b !in '0'..'9' && b.toLowerCase() !in 'a'..'f'))
                throw IllegalArgumentException("The number must be in the range from 00 to ff inclusive")
        hexdecimal = "$a$b"
    }
}

class UnicodeValue(a: Char = '0', b: Char = '0', c: Char = '0', d: Char)
{
    lateinit var unicode: String
        private set
    init
    {
        if((a !in '0'..'9' && a.toLowerCase() !in 'a'..'f') ||
            (b !in '0'..'9' && b.toLowerCase() !in 'a'..'f') ||
            (c !in '0'..'9' && c.toLowerCase() !in 'a'..'f') ||
            (d !in '0'..'9' && d.toLowerCase() !in 'a'..'f'))
            throw IllegalArgumentException("The number must be in the range from 00 to ff inclusive")
        unicode = "$a$b$c$d"
    }
}