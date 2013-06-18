rule {
    google_define {
        pattern = /([\W\w]+)/
        url = "www.google.com/search?q=define:{0.1}"
    }
}