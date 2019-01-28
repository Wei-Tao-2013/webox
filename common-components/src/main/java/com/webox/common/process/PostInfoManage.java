package com.webox.common.process;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.webox.common.model.Message;
import com.webox.common.model.Photo;
import com.webox.common.model.PostInfo;
import com.webox.common.model.Request;
import com.webox.common.model.Response;
import com.webox.common.model.Service;
import com.webox.common.repository.PostInfoRepository;
import com.webox.common.repository.ServiceRepository;
import com.webox.common.repository.UserRepository;
import com.webox.common.utils.AppConsts;
import com.webox.common.utils.GIutils;

import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

public abstract class PostInfoManage {
	private static final Logger logger = LoggerFactory.getLogger(PostInfoManage.class);

	@Autowired
	PostInfoRepository postInfoRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	@Qualifier("messageManage")
	MessageManage messageManage;

	@Autowired
	@Qualifier("watchListManage")
	WatchListManage watchListManage;

	@Autowired
	@Qualifier("orderManage")
	OrderManage orderManage;

	@Autowired
	ServiceRepository serviceRepository;

	@Autowired
	@Qualifier("fileStore")
	FileStore fileStore;

	// private LocalDateTime localDateTime = LocalDateTime.now();

	public Response draftAPostInfo(Request request) {

		PostInfo postInfo = new PostInfo();
		Response response = new Response();
		postInfo = request.getPostInfo();
		String postInfoId = postInfo.getPostInfoId();
		PostInfo existPostInfo = postInfoRepository.findByPostInfoId(postInfoId);
		LocalDateTime localDateTime = LocalDateTime.now();
		if (existPostInfo != null) {
			List<Photo> photoList = existPostInfo.getPostPhoto();
			GIutils.copyProperties(postInfo, existPostInfo);
			existPostInfo.setPostPhoto(photoList);
			existPostInfo.setStatus("DRAFT");
			existPostInfo.setPostUpdateDateTime(localDateTime);

			logger.debug("draft existPostInfo is " + GIutils.converToJson(existPostInfo));
			postInfoRepository.save(existPostInfo);
			postInfoId = existPostInfo.getPostInfoId();
		} else {
			postInfo.setStatus("DRAFT");
			postInfo.setPostUpdateDateTime(localDateTime);
			postInfo.setPostDateTime(localDateTime);
			postInfoRepository.save(postInfo);
			postInfoId = postInfo.getPostInfoId();
		}
		response.setAppInfo("DRAFT");
		response.setAppStatus(AppConsts.RETURN_TRUE);
		response.setPostInfoId(postInfoId);
		return response;
	}

	public Response loadPostInfos(String serviceId, String postInfoStatus) {
		Response response = new Response();
		response.setAppInfo("POSTINFOS_LOADED");
		response.setAppStatus(AppConsts.RETURN_TRUE);
		List<PostInfo> listPost = postInfoRepository.loadPostInfosByService(serviceId, postInfoStatus);
		response.setPostInfos(listPost);
		return response;
	}

	public Map<String, List<PostInfo>> loadLimitPostInfosByServices(List<Service> services, int limit) {
		Map<String, List<PostInfo>> postMap = new HashMap<String, List<PostInfo>>();
		if (!services.isEmpty()) {
			services.forEach(s -> {
				postMap.put(s.getServiceId(), postInfoRepository.loadPostInfosBriefsByService(s.getServiceId(), limit));
			});
		}
		return postMap;
	}

	public Response loadAPostInfo(String postInfoId) {
		Response response = new Response();
		PostInfo postInfo = postInfoRepository.findByPostInfoId(postInfoId);
		if (postInfo != null) {
			response.setAppInfo("APOSTINFO_LOADED");
		} else {
			response.setAppInfo("POSTINFO_NOTFOUND");
		}
		response.setPostInfo(postInfo);
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}

	public Response publishAPostInfo(Request request) {
		PostInfo postInfo = new PostInfo();
		Response response = new Response();
		postInfo = request.getPostInfo();
		String postInfoId = postInfo.getPostInfoId();
		PostInfo existPostInfo = postInfoRepository.findByPostInfoId(postInfoId);
		LocalDateTime localDateTime = LocalDateTime.now();
		if (existPostInfo != null) {
			List<Photo> photoList = existPostInfo.getPostPhoto();
			GIutils.copyProperties(postInfo, existPostInfo);
			existPostInfo.setPostPhoto(photoList);
			existPostInfo.setStatus("PUBLISHED");
			existPostInfo.setPostUpdateDateTime(localDateTime);
			logger.debug("A published existPostInfo is " + GIutils.converToJson(existPostInfo));
			postInfoRepository.save(existPostInfo);
			postInfoId = existPostInfo.getPostInfoId();
		} else {
			postInfo.setStatus("PUBLISHED");
			postInfo.setPostDateTime(localDateTime);
			postInfo.setPostUpdateDateTime(localDateTime);
			postInfoRepository.save(postInfo);
			postInfoId = postInfo.getPostInfoId();
		}

		// send message to users
		Service serviceByPoster = serviceRepository.findByServiceId(postInfo.getServiceId());
		if (serviceByPoster != null) {
			List<Message> messageList = messageManage.constructMessageObjList(
					this.getMessageReceivers(postInfo.getServiceId()), serviceByPoster.getUserId(), "text",
					"尊敬的用户，您所关注的服务有一条新的公告。");
			messageManage.saveMessageList(messageList);
		}

		// end message to users

		response.setPostInfoId(postInfoId);
		response.setAppInfo("PUBLISHED");
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}

	private List<String> getMessageReceivers(String serviceId) {
		Stream<String> combinedStream = Stream.of(watchListManage.loadUserIdListByWatchService(serviceId),
				orderManage.loadUserIdListByServiceOrder(serviceId)).flatMap(Collection::stream);
		Collection<String> userIds = combinedStream.collect(Collectors.toSet());
		return userIds.stream().collect(Collectors.toList());

	}

	public Response deleteAPostInfo(Request request) {
		PostInfo postInfo = new PostInfo();
		Response response = new Response();
		postInfo = request.getPostInfo();
		String postInfoId = postInfo.getPostInfoId();
		PostInfo existPostInfo = postInfoRepository.findByPostInfoId(postInfoId);
		LocalDateTime localDateTime = LocalDateTime.now();
		if (existPostInfo != null) {
			List<Photo> photoList = existPostInfo.getPostPhoto();
			GIutils.copyProperties(postInfo, existPostInfo);
			existPostInfo.setPostPhoto(photoList);
			existPostInfo.setStatus("DELETED");
			existPostInfo.setPostUpdateDateTime(localDateTime);
			logger.debug("A published existPostInfo is " + GIutils.converToJson(existPostInfo));
			postInfoRepository.save(existPostInfo);
			postInfoId = existPostInfo.getPostInfoId();
			response.setPostInfoId(postInfoId);
			response.setAppInfo("DELETED");
		} else {
			response.setAppInfo("CANNOTFOUND");
		}
		response.setAppStatus(AppConsts.RETURN_TRUE);
		return response;
	}

	public List<PostInfo> loadLimitPostInfosbyService(String serviceId, int limit) {
		return postInfoRepository.loadPostInfosBriefsByService(serviceId, limit);
	}

	private List<PostInfo> loadPostInfosByExample(PostInfo postInfo, ExampleMatcher matcher) {
		Example<PostInfo> example = Example.of(postInfo, matcher);
		List<PostInfo> postInfoList = postInfoRepository.findAll(example);
		postInfoList.sort((a, b) -> {
			if (a.getPostUpdateDateTime().isAfter(b.getPostUpdateDateTime()))
				return 1;
			else
				return 0;
		});
		return postInfoList;
	}

	public abstract Response storeImage(Request request, String fileName, Binary data);

	public abstract Response getImages(String PostInfoId);

	public abstract void deleteImage(String fileId);

	public Response approvalAPostInfo(Request request) {
		return null;
	}

}