/**
 * This file contains the rules that will be used for deciphurling.
 * For more information on rules definition, read here: https://github.com/ceilfors/deciphurl
 */
rule {
    google_define {
        pattern = /([\W\w]+)/
        url = "http://www.google.com/search?q=define:{0.1}"
    }
}