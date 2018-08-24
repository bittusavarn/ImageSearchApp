# ImageSearchApp
It's an android application which search image using flickr API

It contains An Activity which will allow user to search Image using 

https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&
format=json&nojsoncallback=1&safe_search=1&text=text

 Application is using text and page parameter in query 
 For ex :- if user has to load page 2 result of search text kittens 
 Url will be  
 https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&
format=json&nojsoncallback=1&safe_search=1&text=kittens&page=2
 
 Click on Search will Return the limited image url which will be loaded  in background from Server.
 If User scroll down and reach to the bottom based on prvious response App will query server fro next page 
 and return the next page result and rquest is sent to Thread pool to load Images .
 
 ImageSearchActivity will use recyclerView with Grid layout of column 3 .
RecyclerView is registered with listener InfiniteScrollListener which will check id data set is cahnged and reach to thresold 
load next page .SearchTask is sending is sending request to load all the data of a page in json fromat using RequestProcsessor. RequestProcessor use JSONParse to parse tha data and return to task .Search Task notify activity about change which is notified to   RecyclerView Adapter. RecyclerViewAdapter use PhotoLoader to load image .if Image is recently loaded it can be found in LRU Cache if not 
it will load data from server and add it to LRU cache.
 
 
