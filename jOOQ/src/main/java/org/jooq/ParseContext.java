/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.jooq.conf.Settings;
import org.jooq.impl.ParserException;

/**
 * A publicly available API for the internal parse context that allows for
 * parsing SQL fragements.
 * <p>
 *
 *
 * @author Lukas Eder
 */
public interface ParseContext extends Scope {

    // -------------------------------------------------------------------------
    // Additional Scope context
    // -------------------------------------------------------------------------

    /**
     * Convenient access to {@link Settings#getParseDialect()}.
     */
    SQLDialect parseDialect();

    /**
     * Convenient access to {@link Settings#getParseDialect()}'s family.
     */
    SQLDialect parseFamily();

    // -------------------------------------------------------------------------
    // Parse context
    // -------------------------------------------------------------------------

    /**
     * The character at the current {@link #position()}.
     */
    char character();

    /**
     * The character at position {@link #position(int)}.
     */
    char character(int pos);

    /**
     * The current position.
     */
    int position();

    /**
     * Set the current position to a new one.
     *
     * @return Always returns true.
     */
    boolean position(int newPosition);

    // -------------------------------------------------------------------------
    // Parse actions
    // -------------------------------------------------------------------------

    /**
     * Peek at the next character.
     *
     * @return Whether the next character is the expected one.
     * @see #parse(char)
     */
    boolean peek(char c);

    /**
     * Peek at the next characters.
     *
     * @return Whether the next characters are the expected ones.
     * @see #parse(String)
     */
    boolean peek(String string);

    /**
     * Peek at the next keyword.
     *
     * @return Whether the next keyword is the expected one.
     * @see #parseKeywordIf(String)
     */
    boolean peekKeyword(String keyword);

    /**
     * Peek at the next keyword.
     *
     * @return Whether the next keyword is any of the expected ones.
     * @see #peekKeyword(String)
     * @see #parseKeywordIf(String)
     */
    boolean peekKeyword(String... keywords);

    /**
     * Parse a single character or fail if it cannot be parsed.
     *
     * @return Always returns true.
     */
    boolean parse(char c) throws ParserException;

    /**
     * Try parsing a single character.
     *
     * @return Whether the character could be parsed.
     */
    boolean parseIf(char c);

    /**
     * Parse a string or fail if it cannot be parsed.
     *
     * @return Always returns true.
     */
    boolean parse(String string) throws ParserException;

    /**
     * Try parsing a string.
     *
     * @return Whether the string could be parsed.
     */
    boolean parseIf(String string);

    /**
     * Parse a keyword or fail if the keyword cannot be parsed.
     * <p>
     * The argument keyword should be in UPPER CASE and is allowed to contain
     * spaces. Spaces separating keyword parts are interpreted as any arbitrary
     * amount of whitespace. For example, when parsing <code>"ORDER BY"</code>,
     * then <code>"order    by"</code> will be parsed as well.
     *
     * @return Always returns true.
     * @see #parseKeywordIf(String)
     */
    boolean parseKeyword(String keyword) throws ParserException;

    /**
     * Try parsing a keyword.
     * <p>
     * The argument keyword should be in UPPER CASE and is allowed to contain
     * spaces. Spaces separating keyword parts are interpreted as any arbitrary
     * amount of whitespace. For example, when parsing <code>"ORDER BY"</code>,
     * then <code>"order    by"</code> will be parsed as well.
     *
     * @return Whether the keyword could be parsed.
     */
    boolean parseKeywordIf(String keyword);

    /**
     * Try parsing any keyword.
     *
     * @return Whether any of the keywords could be parsed.
     * @see #parseKeywordIf(String)
     */
    boolean parseKeywordIf(String... keywords);

    /**
     * Parse any keyword or fail if none of the keywords could be parsed.
     *
     * @return Always returns true.
     * @see #parseKeyword(String)
     */
    boolean parseKeyword(String... keywords) throws ParserException;

    /**
     * Parse an (unqualified) identifier or fail if the current token is not an
     * identifier.
     *
     * @return The parsed identifier.
     * @see #parseName()
     */
    Name parseIdentifier();

    /**
     * Try parsing an (unqualified) identifier.
     *
     * @return The identifier if it could be parsed, or <code>null</code> if the
     *         current token is not an identifier.
     * @see #parseNameIf()
     */
    Name parseIdentifierIf();

    /**
     * Parse a name (a qualified identifier) or fail if the current token is not
     * a name.
     *
     * @return The parsed name.
     * @see #parseIdentifier()
     */
    Name parseName() throws ParserException;

    /**
     * Try parsing a name (a qualified identifier).
     *
     * @return The name if it could be parsed, or <code>null</code> if the
     *         current token is not a name.
     * @see #parseIdentifierIf()
     */
    Name parseNameIf();

    /**
     * Try parsing a function name.
     *
     * @return Whether the function name could be parsed.
     */
    boolean parseFunctionNameIf(String name);

    /**
     * Try parsing any function name.
     *
     * @return Whether any function name could be parsed.
     * @see #parseFunctionNameIf(String)
     */
    boolean parseFunctionNameIf(String... names);

    /**
     * Parse a string literal or fail if the current token is not a string
     * literal.
     *
     * @return The parsed string literal.
     */
    String parseStringLiteral() throws ParserException;

    /**
     * Try parsing a string literal.
     *
     * @return The string literal if it could be parsed, or <code>null</code> if
     *         the current token is not a string literal.
     */
    String parseStringLiteralIf();

    /**
     * Parse an unsigned integer literal or fail if the current token is not an
     * unsigned integer literal.
     *
     * @return The unsigned integer literal.
     */
    Long parseUnsignedIntegerLiteral();

    /**
     * Try parsing an unsigned integer literal.
     *
     * @return The unsigned integer literal if it could be parsed, or
     *         <code>null</code> if the current token is not an unsigned integer
     *         literal.
     */
    Long parseUnsignedIntegerLiteralIf();

    /**
     * Parse a signed integer literal or fail if the current token is not a
     * signed integer literal.
     *
     * @return The signed integer literal.
     */
    Long parseSignedIntegerLiteral();

    /**
     * Try parsing an signed integer literal.
     *
     * @return The signed integer literal if it could be parsed, or
     *         <code>null</code> if the current token is not an signed integer
     *         literal.
     */
    Long parseSignedIntegerLiteralIf();

    /**
     * Parse a {@link DataType} expression or fail if the current expression is
     * not a data type.
     *
     * @return The data type.
     */
    DataType<?> parseDataType();

    /**
     * Parse a {@link Field} expression or fail if the current expression is not
     * a field.
     *
     * @return The parsed field.
     */
    Field<?> parseField() throws ParserException;

    /**
     * Parse a {@link SortField} expression or fail if the current expression is
     * not a sort field.
     *
     * @return The parsed sort field.
     */
    SortField<?> parseSortField() throws ParserException;

    /**
     * Parse a {@link Condition} expression or fail if the current expression is
     * not a condition.
     *
     * @return The parsed condition.
     */
    Condition parseCondition() throws ParserException;

    /**
     * Parse a {@link Table} expression or fail if the current expression is not
     * a table.
     *
     * @return The parsed table.
     */
    Table<?> parseTable() throws ParserException;

    /**
     * Convenience method to parse a list of at least 1 elements.
     */
    <T> List<T> parseList(String separator, Function<? super ParseContext, ? extends T> element);

    /**
     * Convenience method to parse a list of at least 1 elements.
     */
    <T> List<T> parseList(Predicate<? super ParseContext> separator, Function<? super ParseContext, ? extends T> element);

    /**
     * Convenience method to parse parenthesised content.
     */
    <T> T parseParenthesised(Function<? super ParseContext, ? extends T> content);

    /**
     * Convenience method to parse parenthesised content.
     */
    <T> T parseParenthesised(char open, Function<? super ParseContext, ? extends T> content, char close);

    /**
     * Convenience method to parse parenthesised content.
     */
    <T> T parseParenthesised(String open, Function<? super ParseContext, ? extends T> content, String close);

    /**
     * An exception that can be thrown from the current position.
     */
    ParserException exception(String message);

}

