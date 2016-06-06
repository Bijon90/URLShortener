# URLShortener
Shortens longer urls and stores in the database

This program provides two options - 1. Shorten a longer URL and 2. Decode short urls to original one. The program generates an id for
each url from the hashcode of the url string and encrypts that hashcode to generate shorter url strings. The record related to each url
is then stored inside a Concourse database. If the user wants to get back the original url string, already existing in database, he sends
a query by entering the short url and the long url strings is returned from the database. The user can also provide a domain name, passed
as a parameter to the constructor, which would be appended to the generated shortened url. 

Sample Interaction with the application:

Do you want to shorten a URL or expand a short URL?
Enter shorten/expand?
shorten
Entered choice: shorten
Enter URL to shorten: 
https://github.com
Inserting record to DB...
Record inserted to Database!
Id: 1534095099 LongURL: https://github.com Shortened URL:www.bitly.com/1fouDL
Do you want to continue(Y/N): 
Y
Do you want to shorten a URL or expand a short URL?
Enter shorten/expand?
shorten
Entered choice: shorten
Enter URL to shorten: 
http://www.stackoverflow.com
Record already exists!
Id: 1226764108 LongURL: http://www.stackoverflow.com Shortened URL:www.bitly.com/1L1NNs
Do you want to continue(Y/N): 
Y
Do you want to shorten a URL or expand a short URL?
Enter shorten/expand?
expand
Entered choice: expand
Enter URL to expand: 
www.bitly.com/1L1NNs
LongURL: http://www.stackoverflow.com
ShortURL: www.bitly.com/1L1NNs Id: 1226764108 Expanded URL: http://www.stackoverflow.com
Do you want to continue(Y/N): 
N
Exit!
