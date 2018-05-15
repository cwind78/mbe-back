package com.org.app.main.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.org.app.main.vo.Cards;
import com.org.app.main.vo.Code;
import com.org.app.main.vo.Filters;
import com.org.app.main.vo.Point;
import com.org.app.main.vo.Recommand_filter;
import com.org.app.main.vo.Survey;
import com.org.app.main.vo.Surveys;
import com.org.app.main.vo.User;

public interface MainService {
	public List<User> loginCheck(User user, HttpServletRequest request) throws Exception;
	/*
	 *  sign up : duple id check
	 */
	public List<User> dupleCheck(User user) throws Exception;
	/*
	 * get common codes list
	 */
	public List<Code> getCommonCode(Map<String, List<String>> data) throws Exception;
	
	/*
	 * sign up save
	 */
	public Integer signUp(User user) throws Exception;
	
	/*
	 * filter save
	 */
	public Integer filterSave(Filters filters, HttpServletRequest request) throws Exception;
	/*
	 * filter delete
	 */
	public Integer filterDelete(Recommand_filter filter) throws Exception;
	/*
	 * get saved recommand filter
	 */
	public List<Recommand_filter> getRecommandFilter(Recommand_filter filter) throws Exception;
	/*
	 * get other user's survey list(only today and increasing survey)
	 */
	public List<Survey> getSurveyList(Survey survey) throws Exception;
	/*
	 * get max survey user value
	 */
	public List<Survey> getMaxUserValue(Survey survey) throws Exception;
	/*
	 * get my survey list(only today and increasing survey)
	 */
	public List<Survey> getMySurveyList(Survey survey) throws Exception;
	/*
	 * survey insert
	 */
	public Integer surveySave(Surveys surveys, HttpServletRequest request) throws Exception;
	/*
	 * vote save
	 */
	public Integer voteSave(Surveys surveys, HttpServletRequest request) throws Exception;
	/*
	 * card save
	 */
	public Integer cardSave(Cards cards, HttpServletRequest request) throws Exception;
	/*
	 * get user point
	 */
	public List<User> getUserPoint(User user) throws Exception;
	/*
	 * get user point lists
	 */
	public List<Point> getUserPoints(Point point) throws Exception;
}