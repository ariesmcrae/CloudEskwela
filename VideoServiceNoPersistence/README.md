# Video Service

###To run the application:
From command prompt: ./gradlew run or gradlew.bat run
From Eclipse: Right-click on the Application class in the com.ariesmcrae.videoservice package, Run As->Java Application

###To stop the application:
* From command prompt: CTRL + C
* From Eclipse: Open the Eclipse Debug Perspective (Window->Open Perspective->Debug), right-click on the application in the "Debug" view (if it isn't open, Window->Show View->Debug) and select Terminate

### Unit Test
* From command prompt: ./gradlew build or gradlew.bat build
* From Eclipse: VideoServiceControllerTest->Run As->JUnit Test to launch the test. 
Eclipse will report which tests pass or fail.

### Overview
* Application for uploading video to a cloud service.
* Manages video's metadata.


### Instructions
GET /video
   - Returns the list of videos that have been added to the
     server as JSON. The list of videos (for now) is not 
     persisted across restarts of the server. 

     
POST /video
   - Posts the video metadata
   - Returns the JSON representation of the Video object that
     was stored along with any updates to that object made by the server. 
   - The server modifies the object that was posted and gives it a unique Id.
   - The returned Video JSON include the server generated Id
     identifier so that the client can refer to it when uploading the
     binary mpeg video content for the Video.
   - The server also generates a "data url" for the
     Video. The "data url" is the url of the binary data for a
     Video (e.g., the raw mpeg data). e.g., http://localhost:8080/video/1/data
     
POST /video/{id}/data
   - The binary mpeg data for the video must be provided in a multipart
     request as a part with the key "data". The client MUST *create* a Video first by sending a POST to /video
     and getting the identifier for the newly created Video object before
     sending a POST to /video/{id}/data. 
   - The endpoint returns a VideoStatus object with state=VideoState.READY
     if the request succeeds, and the appropriate HTTP error status otherwise.
   - Rather than a PUT request, a POST is used because, by default, Spring 
     does not support a PUT with multipart data due to design decisions in the
     Commons File Upload library: https://issues.apache.org/jira/browse/FILEUPLOAD-197
     
     
GET /video/{id}/data
   - Returns the binary mpeg data (if any) for the video with the given
     identifier. If no mpeg data has been uploaded for the specified video,
     then the server should return a 404 status code.
     


