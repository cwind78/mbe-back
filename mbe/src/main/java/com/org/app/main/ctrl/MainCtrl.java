package com.org.app.main.ctrl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.org.app.main.service.MainService;
import com.org.app.main.vo.Cards;
import com.org.app.main.vo.Code;
import com.org.app.main.vo.Filters;
import com.org.app.main.vo.Point;
import com.org.app.main.vo.Recommand_User;
import com.org.app.main.vo.Recommand_filter;
import com.org.app.main.vo.Result;
import com.org.app.main.vo.Survey;
import com.org.app.main.vo.Surveys;
import com.org.app.main.vo.User;

import static java.nio.file.StandardCopyOption.*;

@Controller
@RequestMapping(value="/main")
public class MainCtrl {
	@Autowired
	MainService mainService;
	
	/*
	 * login
	 */
	@RequestMapping(value="/loginCheck")
	@ResponseBody
	public ResponseEntity<List<User>> loginCheck(
			@RequestParam(value="user_id", required=true) String user_id,
			@RequestParam(value="user_password", required=true) String user_password,
			HttpServletRequest request, HttpServletResponse response
			) throws Exception {
		User user = new User();
		user.setUser_id(user_id);
		user.setUser_password(user_password);
		
		List<User> list = mainService.loginCheck(user, request);
		if (list != null && list.size() > 0) {
			return new ResponseEntity<List<User>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
		}	
	}
	
	/*
	 * duplicate id check
	 */
	@RequestMapping(value="/dupleCheck")
	@ResponseBody
	public ResponseEntity<List<User>> dupleCheck(
			@RequestParam(value="user_id", required=true) String user_id,
			HttpServletRequest request, HttpServletResponse response
			) throws Exception {
		User user = new User();
		user.setUser_id(user_id);
		
		List<User> list = mainService.dupleCheck(user);
		if (list != null && list.size() > 0) {
			return new ResponseEntity<List<User>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
		}	
	}
	
	/*
	 * duplicate nickname check
	 */
	@RequestMapping(value="/getCommonCode/{code_group}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Code>> getCommonCode(@PathVariable("code_group") String code_group, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] data = code_group.split("--");
		List<String> str = new ArrayList<String>();
		for (String item : data) {
			str.add(item);
		}
		
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		map.put("data", str);
		List<Code> list = mainService.getCommonCode(map);
		
		if (list != null && list.size() > 0) {
			return new ResponseEntity<List<Code>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Code>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * signup user profile image upload
	 */
	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	//@ResponseBody
    public ResponseEntity<List<Result>> uploadImage(@RequestParam(value="photoimage") MultipartFile photo_image, HttpServletRequest request) throws Exception {
		Result result = null;
		List<Result> resultList = null;

	    try {
	      Path downloadedFile = Paths.get(photo_image.getOriginalFilename());
	      Files.deleteIfExists(downloadedFile);

	      String dir = request.getSession().getServletContext().getRealPath("/") + File.separator + "resources/upload/profile";
	      File mkDir = new File(dir);
	      if (!mkDir.exists()) {
	        mkDir.mkdir();
	      }
	      
	      Files.copy(photo_image.getInputStream(), Paths.get(request.getSession().getServletContext().getRealPath("/") + File.separator + "resources/upload/profile" + File.separator + downloadedFile.getFileName()), REPLACE_EXISTING);

	      result = new Result();
	      result.setResult("1");
	      resultList = new ArrayList<Result>();
		  resultList.add(result);
		  return new ResponseEntity<List<Result>>(resultList, HttpStatus.OK);
	    }
	    catch (IOException e) {
	      LoggerFactory.getLogger(this.getClass()).error("pictureupload", e);
	      return new ResponseEntity<List<Result>>(HttpStatus.NOT_FOUND);
	    }
    }
	
	/*
	 * sign up save
	 */
	@RequestMapping(value="/signup", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Result>> signUp(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		user.setRegist_user_id(user.getUser_id());
		user.setPoint("100");
		user.setRecommand_aggrement(user.getRecommand_aggrement()==null||user.getRecommand_aggrement().equals("false")?"N":"Y");
		user.setEmail_aggrement(user.getEmail_aggrement()==null||user.getEmail_aggrement().equals("false")?"N":"Y");
		user.setPush_message_aggrement(user.getPush_message_aggrement()==null||user.getPush_message_aggrement().equals("false")?"N":"Y");
		user.setInformation_aggrement(user.getInformation_aggrement()==null||user.getInformation_aggrement().equals("false")?"N":"Y");
		Integer rValue  = mainService.signUp(user);
		Result result = new Result();
		result.setResult(String.valueOf(rValue));
		List<Result> list = new ArrayList<Result>();
		list.add(result);
		
		if (rValue > 0) {
			return new ResponseEntity<List<Result>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Result>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * filter save
	 */
	@RequestMapping(value="/filter", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Result>> filterSave(@RequestBody Filters filters, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer rValue  = mainService.filterSave(filters, request);
		Result result = new Result();
		result.setResult(String.valueOf(rValue));
		List<Result> list = new ArrayList<Result>();
		list.add(result);
		
		if (rValue > 0) {
			return new ResponseEntity<List<Result>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Result>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * filter save
	 */
	@RequestMapping(value="/filter/delete/{seq_no}", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Result>> filterDelete(@PathVariable("seq_no") String seq_no,  HttpServletRequest request, HttpServletResponse response) throws Exception {
		Recommand_filter filter = new Recommand_filter();
		filter.setSeq_no(seq_no);
		Integer rValue  = mainService.filterDelete(filter);
		Result result = new Result();
		result.setResult(String.valueOf(rValue));
		List<Result> list = new ArrayList<Result>();
		list.add(result);
		
		if (rValue > 0) {
			return new ResponseEntity<List<Result>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Result>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * get saved recommand filter
	 */
	@RequestMapping(value="/filter", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Recommand_filter>> getRecommandFilter(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Recommand_filter filter = new Recommand_filter();
		HttpSession session = request.getSession();
		filter.setUser_id(session.getAttribute("USER_ID").toString());
		List<Recommand_filter> list = mainService.getRecommandFilter(filter);
		
		if (list != null && list.size() > 0) {
			return new ResponseEntity<List<Recommand_filter>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Recommand_filter>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * get other user's survey list(only today and increasing survey)
	 */
	@RequestMapping(value="/survey", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Survey>> getSurveyList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Survey survey = new Survey();
		HttpSession session = request.getSession();
		survey.setUser_id(session.getAttribute("USER_ID").toString());
		List<Survey> list = mainService.getSurveyList(survey);
		
		if (list != null && list.size() > 0) {
			return new ResponseEntity<List<Survey>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Survey>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * get my survey list(only today and increasing survey)
	 */
	@RequestMapping(value="/survey/user", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Survey>> getMySurveyList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Survey survey = new Survey();
		HttpSession session = request.getSession();
		survey.setUser_id(session.getAttribute("USER_ID").toString());
		List<Survey> list = mainService.getMySurveyList(survey);
		
		if (list != null && list.size() > 0) {
			return new ResponseEntity<List<Survey>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Survey>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * get max survey user value
	 */
	@RequestMapping(value="/survey/user/max", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Survey>> getMaxUserValue(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Survey survey = new Survey();
		HttpSession session = request.getSession();
		survey.setUser_id(session.getAttribute("USER_ID").toString());
		List<Survey> list = mainService.getMaxUserValue(survey);
		
		if (list != null && list.size() > 0) {
			return new ResponseEntity<List<Survey>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Survey>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * survey regist
	 */
	@RequestMapping(value="/survey/user", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Result>> surveySave(@RequestBody Surveys surveys, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer rValue  = mainService.surveySave(surveys, request);
		Result result = new Result();
		result.setResult(String.valueOf(rValue));
		List<Result> list = new ArrayList<Result>();
		list.add(result);
		
		if (rValue > 0) {
			return new ResponseEntity<List<Result>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Result>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * survey regist
	 */
	@RequestMapping(value="/survey/vote", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Result>> voteSave(@RequestBody Surveys surveys, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer rValue  = mainService.voteSave(surveys, request);
		Result result = new Result();
		result.setResult(String.valueOf(rValue));
		List<Result> list = new ArrayList<Result>();
		list.add(result);
		
		if (rValue > 0) {
			return new ResponseEntity<List<Result>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Result>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * save card game result
	 */
	@RequestMapping(value="/card", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Result>> cardSave(@RequestBody Cards cards, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer rValue  = mainService.cardSave(cards, request);
		Result result = new Result();
		result.setResult(String.valueOf(rValue));
		List<Result> list = new ArrayList<Result>();
		list.add(result);
		
		if (rValue > 0) {
			return new ResponseEntity<List<Result>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Result>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * get user point
	 */
	@RequestMapping(value="/user/point", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<User>> getUserPoint(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = new User();
		HttpSession session = request.getSession();
		user.setUser_id(session.getAttribute("USER_ID").toString());
		List<User> list = mainService.getUserPoint(user);
		
		if (list != null && list.size() > 0) {
			return new ResponseEntity<List<User>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * get user point lists
	 */
	@RequestMapping(value="/point/user", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Point>> getUserPoints(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Point point = new Point();
		HttpSession session = request.getSession();
		point.setUser_id(session.getAttribute("USER_ID").toString());
		List<Point> list = mainService.getUserPoints(point);
		
		if (list != null && list.size() > 0) {
			return new ResponseEntity<List<Point>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Point>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * get user point lists
	 */
	@RequestMapping(value="/recommand", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Recommand_User>> getRecommandUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = new User();
		HttpSession session = request.getSession();
		user.setUser_id(session.getAttribute("USER_ID").toString());
		List<Recommand_User> list = mainService.getRecommandUser(user);
		
		if (list != null && list.size() > 0) {
			return new ResponseEntity<List<Recommand_User>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Recommand_User>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * call recommand user
	 */
	@RequestMapping(value="/recommand/call", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Result>> callRecommandUser(@RequestBody Recommand_User recommand_user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer rValue  = mainService.callRecommandUser(recommand_user, request);
		Result result = new Result();
		result.setResult(String.valueOf(rValue));
		List<Result> list = new ArrayList<Result>();
		list.add(result);
		
		if (rValue > 0) {
			return new ResponseEntity<List<Result>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Result>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * deny recommand user
	 */
	@RequestMapping(value="/recommand/deny", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Result>> denyRecommandUser(@RequestBody Recommand_User recommand_user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer rValue  = mainService.denyRecommandUser(recommand_user, request);
		Result result = new Result();
		result.setResult(String.valueOf(rValue));
		List<Result> list = new ArrayList<Result>();
		list.add(result);
		
		if (rValue > 0) {
			return new ResponseEntity<List<Result>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Result>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * get called recommand list
	 */
	@RequestMapping(value="/called", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<User>> calledMeRecommandedUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = new User();
		HttpSession session = request.getSession();
		user.setUser_id(session.getAttribute("USER_ID").toString());
		List<User> list = mainService.calledMeRecommandedUser(user);
		
		if (list != null && list.size() > 0) {
			return new ResponseEntity<List<User>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * recommand acceptance
	 */
	@RequestMapping(value="/called/accept", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Result>> insertRecommandAcceptance(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer rValue  = mainService.insertRecommandAcceptance(user);
		Result result = new Result();
		result.setResult(String.valueOf(rValue));
		List<Result> list = new ArrayList<Result>();
		list.add(result);
		
		if (rValue > 0) {
			return new ResponseEntity<List<Result>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Result>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * deny call
	 */
	@RequestMapping(value="/called/deny", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Result>> denyCall(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer rValue  = mainService.denyCall(user);
		Result result = new Result();
		result.setResult(String.valueOf(rValue));
		List<Result> list = new ArrayList<Result>();
		list.add(result);
		
		if (rValue > 0) {
			return new ResponseEntity<List<Result>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Result>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * save token
	 */
	@RequestMapping(value="/token/{token}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Result>> saveToken(@PathVariable("token") String token, HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = new User();
		HttpSession session = request.getSession();
		user.setUser_id(session.getAttribute("USER_ID").toString());
		user.setToken(token);
		Integer rValue = mainService.saveToken(user);
		Result result = new Result();
		result.setResult(String.valueOf(rValue));
		List<Result> list = new ArrayList<Result>();
		
		if (rValue > 0) {
			return new ResponseEntity<List<Result>>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Result>>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * logout
	 */
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Result>> logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(false);
		session.invalidate();
		
		Result result = new Result();
		List<Result> resultList = new ArrayList<Result>();
		result.setResult("1");
				
		resultList.add(result);
		
		if (resultList.size() > 0) {
			return new ResponseEntity<List<Result>>(resultList, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Result>>(HttpStatus.NOT_FOUND);
		}
	}
}