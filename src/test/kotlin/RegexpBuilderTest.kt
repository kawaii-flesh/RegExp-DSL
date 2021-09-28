import org.junit.Test
import kotlin.test.assertEquals

class RegexBuilderTest
{
    @Test
    fun emailValid()
    {
        val actual =
        makeRegexpAsString()
        {
            beginLine()
            characterClass()
            {
                range('a', 'z')
                range('A', 'Z')
                range('0', '9')
                string("+_.-")
            }
            oneOrMore()
            character('@')
            characterClass()
            {
                range('a', 'z')
                range('A', 'Z')
                range('0', '9')
                string(".-")
            }
            oneOrMore()
            endLine()
        }

        assertEquals("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\$", actual)
    }

    @Test
    fun passwordValid()
    {
        val actual =
        makeRegexpAsString()
        {
            group()
            {
                positiveLookAhead()
                {
                    characterClass()
                    {
                        range('A', 'Z')
                        range('a', 'z')
                    }
                    missingOrMore()
                    characterClass()
                    {
                        range('0', '9')
                    }
                }
                positiveLookAhead()
                {
                    characterClass()
                    {
                        range('0', '9')
                        range('A', 'Z')
                    }
                    missingOrMore()
                    characterClass()
                    {
                        range('a', 'z')
                    }
                }
                positiveLookAhead()
                {
                    characterClass()
                    {
                        range('0', '9')
                        range('a', 'z')
                    }
                    missingOrMore()
                    characterClass()
                    {
                        range('A', 'Z')
                    }
                }
            }
            nOrMore(8)
        }

        assertEquals("((?=[A-Za-z]*[0-9])(?=[0-9A-Z]*[a-z])(?=[0-9a-z]*[A-Z])){8,}", actual)
    }
    
    @Test
    fun ip4Valid()
    {
        val actual =
            makeRegexpAsString()
            {
                wordBoundary()
                nonCapturingGroup()
                {
                    nonCapturingGroup()
                    {
                        string("25")
                        characterClass{range('0', '5')}
                        or()
                        character('2')
                        characterClass{range('0', '4')}
                        characterClass{range('0', '9')}
                        or()
                        characterClass{string("01")}
                        missingOrOne()
                        characterClass{range('0', '9')}
                        characterClass{range('0', '9')}
                        missingOrOne()
                    }
                    character('.')
                }
                nExactly(3)
                nonCapturingGroup()
                {
                    string("25")
                    characterClass{range('0', '5')}
                    or()
                    character('2')
                    characterClass{range('0', '4')}
                    characterClass{range('0', '9')}
                    or()
                    characterClass{string("01")}
                    missingOrOne()
                    characterClass{range('0', '9')}
                    characterClass{range('0', '9')}
                    missingOrOne()
                }
                wordBoundary()
            }
            
        assertEquals("\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b", actual)
    }

    @Test
    fun hexColor()
    {
        val actual =
            makeRegexpAsString()
            {
                character('#')
                group()
                {
                    characterClass{range('a' to 'f', 'A' to 'F')}
                    or()
                    characterClass{range('0', '9')}
                }
                nToM(3, 6)
            }

        assertEquals("#([a-fA-F]|[0-9]){3,6}", actual)
    }
}