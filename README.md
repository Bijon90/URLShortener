# URLShortener
Shortens longer urls and stores in the database

This program provides two options - 1. Shorten a longer URL and 2. Decode short urls to original one. The program generates an id for
each url from the hashcode of the url string and encrypts that hashcode to generate shorter url strings. The record related to each url
is then stored inside a Concourse database. If the user wants to get back the original url string, already existing in database, he sends
a query by entering the short url and the long url strings is returned from the database. The user can also provide a domain name, passed
as a parameter to the constructor, which would be appended to the generated shortened url. 
