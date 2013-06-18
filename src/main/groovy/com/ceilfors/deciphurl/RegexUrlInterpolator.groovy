/*
 * Copyright (c) 2013 Wisen Tanasa
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.ceilfors.deciphurl

import groovy.util.logging.Slf4j

/**
 * Interpolates a URL variable with a regex group extracted from the input. {1} refers to the first group, {2} the
 * second group and so on.
 * <br />
 * If you have the following rule:
 * <pre>
 * {
 *   pattern = /^(#my)(\d+)$/
 *   url = "http://my-jenkins/{2}"
 * }
 * </pre>
 * And the input is <tt>#my123</tt>, the URL will be interpolated to become <tt>http://my-jenkins/123</tt>.
 * {2} in the URL refers to the second group in the regex pattern. <br />
 * <br />
 * This interpolator also supports group multi match. By default when the variable is a single digit,
 * it will refer to the first matching group. The first matching group is represented with 0. To use the multi
 * match support, use a dot: <tt>{match.group}</tt>. So when a variable is {1}, it is actually equivalent with {0.1}.
 * See the following rule:
 * <pre>
 * {
 *   multiMatchRule.pattern = /(\d)(\d)/
 *   multiMatchRule.url = "multi/{2.2}{2.1}{1.2}{1.1}{0.2}{0.1}{2}{1}"
 * }
 * </pre>
 * When the input is <tt>123456</tt>, the interpolation result is <tt>multi/65432121</tt>
 * @author ceilfors
 */
@Slf4j
class RegexUrlInterpolator {

    def interpolate(rule, input) {
        def matcher = input =~ rule.pattern
        return rule.url.replaceAll(/\{((\d+)\.)?(\d+)\}/, {
            def match = it[2] ? it[2].toInteger() : 0
            def group = it[3].toInteger()
            try {
                def result = matcher[match][group]
                return result ? result : it[0]
            } catch (ignored) {
                log.error("Invalid replacement: {}", it[0])
                return it[0]
            }
        })
    }
}
