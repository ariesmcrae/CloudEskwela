/*
 * 
 * Copyright 2014 J. White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.ariesmcrae.videoservice;

import java.util.Collection;

import com.ariesmcrae.videoservice.model.Video;
import com.ariesmcrae.videoservice.model.VideoStatus;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;

/**
 * This interface defines an API for a VideoSvc. The
 * interface is used to provide a contract for client/server
 * interactions. The interface is annotated with Retrofit
 * annotations so that clients can automatically convert the
 * interface into a client capable of sending the appropriate
 * HTTP requests.
 * 
 * @author j.white
 */
public interface VideoSvcApi {

	public static final String DATA_PARAMETER = "data";

	public static final String ID_PARAMETER = "id";

	public static final String VIDEO_SVC_PATH = "/video";
	
	public static final String VIDEO_DATA_PATH = VIDEO_SVC_PATH + "/{id}/data";

	/**
	 * This endpoint in the API returns a list of the videos that have
	 * been added to the server. The Video objects should be returned as
	 * JSON. 
	 * 
	 * To manually test this endpoint, run your server and open this URL in a browser:
	 * http://localhost:8080/video
	 * 
	 * @return
	 */
	@GET(VIDEO_SVC_PATH)
	public Collection<Video> getVideoList();
	
	/**
	 * This endpoint allows clients to add Video objects by sending POST requests
	 * that have an application/json body containing the Video object information. 
	 * 
	 * @return
	 */
	@POST(VIDEO_SVC_PATH)
	public Video addVideo(@Body Video v);
	
	/**
	 * This endpoint allows clients to set the mpeg video data for previously
	 * added Video objects by sending multipart POST requests to the server.
	 * The URL that the POST requests should be sent to includes the ID of the
	 * Video that the data should be associated with (e.g., replace {id} in
	 * the url /video/{id}/data with a valid ID of a video, such as /video/1/data
	 * -- assuming that "1" is a valid ID of a video). 
	 * 
	 * @return
	 */
	@Multipart
	@POST(VIDEO_DATA_PATH)
	public VideoStatus setVideoData(@Path(ID_PARAMETER) long id, @Part(DATA_PARAMETER) TypedFile videoData);
	
	/**
	 * This endpoint should return the video data that has been associated with
	 * a Video object or a 404 if no video data has been set yet. The URL scheme
	 * is the same as in the method above and assumes that the client knows the ID
	 * of the Video object that it would like to retrieve video data for.
	 * 
	 * This method uses Retrofit's @Streaming annotation to indicate that the
	 * method is going to access a large stream of data (e.g., the mpeg video 
	 * data on the server). The client can access this stream of data by obtaining
	 * an InputStream from the Response as shown below:
	 * 
	 * VideoSvcApi client = ... // use retrofit to create the client
	 * Response response = client.getData(someVideoId);
	 * InputStream videoDataStream = response.getBody().in();
	 * 
	 * @param id
	 * @return
	 */
	@Streaming
    @GET(VIDEO_DATA_PATH)
    Response getData(@Path(ID_PARAMETER) long id);
	
}
