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
 */
package com.ariesmcrae.videoservice;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.ariesmcrae.videoservice.model.Video;
import com.ariesmcrae.videoservice.model.VideoStatus;
import com.ariesmcrae.videoservice.model.VideoStatus.VideoState;

/**
 * @author aries@ariesmcrae.com 
 */
@Controller
public class VideoServiceController {

    private static VideoFileManager videoDataMgr;
	
    private static final AtomicLong currentId = new AtomicLong(0L);
	
	private static Map<Long,Video> videos = new HashMap<Long, Video>();
	

	@RequestMapping(value="/video", method=RequestMethod.POST)
	public @ResponseBody Video addVideo(@RequestBody Video video, HttpServletRequest request){
		
		if (video != null && video.getId() == 0) {
			video.setId(currentId.incrementAndGet());	
			video.setDataUrl(getDataUrl(video.getId())); //http://localhost:8080/video/1/data
			videos.put(video.getId(), video);
		}
		
		return video;
	}
	
	
	
	
	@RequestMapping(value="/video/{id}/data", method=RequestMethod.POST) //RequestParam = querystring e.g. /greeting?name=blah @RequestParam("name")
	public @ResponseBody VideoStatus setVideoData(@PathVariable("id") long id, @RequestParam("data") MultipartFile videoData, HttpServletResponse response) throws IOException {
		initVideoDataMgr();
		Video video = videos.get(id);

		if (video != null) {
	 	    videoDataMgr.saveVideoData(videos.get(id), videoData.getInputStream());
		} else {
			response.setStatus(404);
		}
		
		return new VideoStatus(VideoState.READY);
	}
	
	
	
	
	@RequestMapping(value="/video/{id}/data", method=RequestMethod.GET)	
    public void getData(@PathVariable("id") long id, HttpServletResponse response) throws IOException {
		initVideoDataMgr();	
		Video video = videos.get(id);
		
		if (video != null) {
			videoDataMgr.copyVideoData(videos.get(id), response.getOutputStream());
			response.setStatus(200);			
		} else {
			response.setStatus(404);			
		}
	}
	

	
	@RequestMapping(value="/video", method=RequestMethod.GET)	
	public @ResponseBody Collection<Video> getVideoList() {
		return videos.values();
	}
	
	
	
	
	private void initVideoDataMgr() throws IOException {
		if (videoDataMgr == null) {
			videoDataMgr = VideoFileManager.get();			
		}
	}
	
	
	
    private static String getDataUrl(long videoId){
 	   HttpServletRequest request =  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
 	   String urlBase = "http://" + request.getServerName() + ((request.getServerPort() != 80) ? ":" + request.getServerPort() : "");
    	
 	   return urlBase + "/video/" + videoId + "/data";
    }

	
}
