# Snatch Challenge #


## Features ##
1) GPS location listening -> drives updates to the list of nearby locations

2) List includes the distance to the location in meters -> list updates in realtime

3) Tapping on a location, lets the user read more about it on wikipedia within a Web Browser 



## Answers to Questions ##
**1)** I chose the following libraries:
   retrofit - for the nice API interface,
   okhttp - to do the heavy lifting of the API call,
   gson - to convert the JSON data into a POJO
   rxjava - for the convenient subscribers and observers
   dagger - for dependency injection
   butterknife - to avoid findViewById() calls
   android-reactive-location - to listen to GPS updates
   
**2)** challenge:
   getting GPS location updates elegantly within the Rx framework. I eventually got it done by relying on a library.
   
**3)** API performance issues & server load:
   I anticipate that the API call will scale quite well because I integrated limiting of the API calls to no more than once every 30 seconds.

**4)** What I like about the test:
   The resulting app was really cool
   
   What I didn't like about it:
   It needs more graphics - icons for the individual locations
   
**5)** Suggestions for improving the test:
   loading icons for each location would make the app better looking, however, it would make the test a little bit more time consuming to finish
   UI design - it would be more realistic if there were a separate sheet for a UI specification
   
   
   