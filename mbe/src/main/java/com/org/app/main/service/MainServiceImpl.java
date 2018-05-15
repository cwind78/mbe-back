package com.org.app.main.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.org.app.main.dao.MainDao;
import com.org.app.main.vo.Card;
import com.org.app.main.vo.Cards;
import com.org.app.main.vo.Code;
import com.org.app.main.vo.Filters;
import com.org.app.main.vo.Point;
import com.org.app.main.vo.Recommand_Denial;
import com.org.app.main.vo.Recommand_User;
import com.org.app.main.vo.Recommand_filter;
import com.org.app.main.vo.Survey;
import com.org.app.main.vo.Surveys;
import com.org.app.main.vo.User;

@Service("mainService")
public class MainServiceImpl implements MainService {
	@Autowired
	MainDao mainDao;
	
	public List<User> loginCheck(User user, HttpServletRequest request) throws Exception {
		List<User> list = mainDao.loginCheck(user);
		if (list != null && list.size() > 0) {
			HttpSession session = request.getSession(true);
			session.setAttribute("USER_ID", user.getUser_id());
			session.setAttribute("USER_NAME", list.get(0).getUser_name());
			session.setMaxInactiveInterval(60*120);
			
			mainDao.insertRecommandUser(user);
		}
		return list;
	}
	
	/*
	 *  sign up : duple id check
	 */
	public List<User> dupleCheck(User user) throws Exception {
		return mainDao.dupleCheck(user);
	}
	
	/*
	 * get common codes list
	 */
	public List<Code> getCommonCode(Map<String, List<String>> data) throws Exception {
		return mainDao.getCommonCode(data);
	}
	
	/*
	 * sign up save
	 */
	public Integer signUp(User user) throws Exception {
		return mainDao.signUp(user);
	}
	
	/*
	 * filter save
	 */
	public Integer filterSave(Filters filters, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		String user_id = session.getAttribute("USER_ID").toString();
		Integer rValue = 0;
		
		for (Recommand_filter filter : filters.getFilters()) {
			if (filter.getSeq_no() == null || filter.getSeq_no().equals("")) {
				filter.setRegist_user_id(user_id);
				filter.setUse_yn("Y");
				rValue += mainDao.insertFilter(filter);
			} else {
				filter.setUpdate_user_id(user_id);
				rValue += mainDao.updateFilter(filter);
			}
		}
		
		return rValue;
	}
	
	/*
	 * filter delete
	 */
	public Integer filterDelete(Recommand_filter filter) throws Exception {
		return mainDao.deleteFilter(filter);
	}
	
	/*
	 * get saved recommand filter
	 */
	public List<Recommand_filter> getRecommandFilter(Recommand_filter filter) throws Exception {
		return mainDao.getRecommandFilter(filter);
	}
	/*
	 * get other user's survey list(only today and increasing survey)
	 */
	public List<Survey> getSurveyList(Survey survey) throws Exception {
		return mainDao.getSurveyList(survey);
	}
	/*
	 * get my survey list(only today and increasing survey)
	 */
	public List<Survey> getMySurveyList(Survey survey) throws Exception {
		return mainDao.getMySurveyList(survey);
	}
	/*
	 * get max survey user value
	 */
	public List<Survey> getMaxUserValue(Survey survey) throws Exception {
		List<Survey> list = mainDao.getMaxUserValue(survey);
		Integer registed_count = mainDao.checkSurvey(survey);
		if (list != null && list.size() > 0) {
			list.get(0).setConfig_value(registed_count.toString());
		} else {
			Survey rSurvey = new Survey();
			rSurvey.setUser_value("100");
			rSurvey.setConfig_value(registed_count.toString());
			list.add(rSurvey);
		}
		return list;
	}
	/*
	 * survey insert
	 */
	public Integer surveySave(Surveys surveys, HttpServletRequest request) throws Exception {
		Integer rValue = 0;
		Integer checkValue = 0;
		//사용자가 입력한 user_value(win count)와 계산하여 가져온 가능한 가장 높은 user_value를 비교하기 위한 값(이 변수는 후자에 해당)
		Integer user_value = 0;
		List<Survey> result = null;
		
		HttpSession session = request.getSession();
		String user_id = session.getAttribute("USER_ID").toString();
		
		for (Survey survey : surveys.getSurveys()) {
			survey.setUser_id(user_id);
			checkValue = mainDao.checkSurvey(survey);
			result = mainDao.getMaxUserValue(survey);
			if (result != null && result.size() > 0) {
				user_value = Integer.valueOf(result.get(0).getUser_value());
			} else {
				user_value = 100;
			}
			
			if (checkValue > 0 || user_value < Integer.valueOf(survey.getUser_value())) {
				return 0;
			} else {
				rValue += mainDao.insertSurvey(survey);
			}
		}
		
		return rValue;
	}
	
	/*
	 * vote save
	 */
	public Integer voteSave(Surveys surveys, HttpServletRequest request) throws Exception {
		Integer rValue = 0;
		HttpSession session = request.getSession();
		for (Survey survey : surveys.getSurveys()) {
			survey.setUser_id(session.getAttribute("USER_ID").toString());
			Point point = new Point();
			point.setUser_id(session.getAttribute("USER_ID").toString());
			point.setRoute("2");
			point.setPoint("1");
			point.setAdjustment("1");
			
			User user = new User();
			user.setUser_id(session.getAttribute("USER_ID").toString());
			user.setPoint("1");
			if (mainDao.checkVote(survey) > 0 || mainDao.checkSurveyForVote(survey) > 0) {
				return 0;
			} else {
				rValue += mainDao.updateVote(survey);
				rValue += mainDao.insertVoteDetail(survey);
				rValue += mainDao.insertUserPointWithSurvey(point);
				rValue += mainDao.updateUserPointWithSurveyVote(user);
			}
		}
		return rValue;
	}
	
	/*
	 * card save
	 */
	public Integer cardSave(Cards cards, HttpServletRequest request) throws Exception {
		Integer rValue = 0;
		HttpSession session = request.getSession();
		for (Card card : cards.getCards()) {
			card.setUser_id(session.getAttribute("USER_ID").toString());
			Point point = new Point();
			point.setUser_id(session.getAttribute("USER_ID").toString());
			point.setRoute("3");
			point.setAdjustment(Integer.valueOf(card.getPaid_point())>0?"1":"-1");
			point.setPoint(String.valueOf(Math.abs(Integer.valueOf(card.getPaid_point()))));
			
			User user = new User();
			user.setUser_id(session.getAttribute("USER_ID").toString());
			user.setPoint(card.getPaid_point());
			
			rValue += mainDao.insertUserPointWithSurvey(point);
			rValue += mainDao.updateUserPointWithSurveyVote(user);
			rValue += mainDao.insertUserPointWithCard(card);
		}
		
		return rValue;
	}
	
	/*
	 * get user point
	 */
	public List<User> getUserPoint(User user) throws Exception {
		return mainDao.getUserPoint(user);
	}
	/*
	 * get user point lists
	 */
	public List<Point> getUserPoints(Point point) throws Exception {
		return mainDao.getUserPoints(point);
	}
	/*
	 * get recommand user
	 */
	public List<Recommand_User> getRecommandUser(User user) throws Exception {
		return mainDao.getRecommandUser(user);
	}
	
	/*
	 * recommand call
	 */
	public Integer callRecommandUser(Recommand_User recommand_user, HttpServletRequest request) throws Exception {
		Integer rValue = 0;
		HttpSession session = request.getSession();
		
		recommand_user.setRecommand_status("3");
		Point point = new Point();
		point.setUser_id(session.getAttribute("USER_ID").toString());
		point.setRoute("10");
		point.setPoint("50");
		point.setAdjustment("-1");
		
		User user = new User();
		user.setUser_id(session.getAttribute("USER_ID").toString());
		user.setPoint("-50");
		
		if (Integer.valueOf(mainDao.getUserPoint(user).get(0).getPoint()) > 50) {
			rValue += mainDao.insertUserPointWithSurvey(point);
			rValue += mainDao.updateUserPointWithSurveyVote(user);
			rValue = mainDao.updateRecommandStatus(recommand_user);
		}
		
		return rValue;
	}
	
	/*
	 * recommand deny
	 */
	public Integer denyRecommandUser(Recommand_User recommand_user, HttpServletRequest request) throws Exception {
		Integer rValue = 0;
		HttpSession session = request.getSession();
		
		recommand_user.setRecommand_status("2");
		
		User user = new User();
		user.setUser_id(recommand_user.getUser_id());
		
		Recommand_Denial recommand_denial = new Recommand_Denial();
		recommand_denial.setUser_id(session.getAttribute("USER_ID").toString());
		recommand_denial.setDenial_user_id(recommand_user.getRecommand_user_id());
		if (mainDao.checkDenial(user) > 4) {
			return 0;
		}
		
		rValue = mainDao.updateRecommandStatus(recommand_user);
		rValue = mainDao.insertUserRecommandDenial(recommand_denial);
		
		return rValue;
	}
	
	/*
	 * get called recommand list
	 */
	public List<User> calledMeRecommandedUser(User user) throws Exception {
		return mainDao.calledMeRecommandedUser(user);
	}
}