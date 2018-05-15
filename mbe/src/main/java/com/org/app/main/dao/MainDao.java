package com.org.app.main.dao;

import java.util.List;
import java.util.Map;

import com.org.app.main.vo.Card;
import com.org.app.main.vo.Code;
import com.org.app.main.vo.Point;
import com.org.app.main.vo.Recommand_filter;
import com.org.app.main.vo.Survey;
import com.org.app.main.vo.Surveys;
import com.org.app.main.vo.User;

public interface MainDao {
	public List<User> loginCheck(User user) throws Exception;
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
	 * filter insert
	 */
	public Integer insertFilter(Recommand_filter filter) throws Exception;
	/*
	 * filter update
	 */
	public Integer updateFilter(Recommand_filter filter) throws Exception;
	/*
	 * filter delete
	 */
	public Integer deleteFilter(Recommand_filter filter) throws Exception;
	/*
	 * get saved recommand filter
	 */
	public List<Recommand_filter> getRecommandFilter(Recommand_filter filter) throws Exception;
	/*
	 * get other user's survey list(only today and increasing survey)
	 */
	public List<Survey> getSurveyList(Survey survey) throws Exception;
	/*
	 * get my survey list(only today and increasing survey)
	 */
	public List<Survey> getMySurveyList(Survey survey) throws Exception;
	/*
	 * get max survey user value
	 */
	public List<Survey> getMaxUserValue(Survey survey) throws Exception;
	/*
	 * survey insert check(finished survey and doing survey count)
	 */
	public Integer checkSurvey(Survey survey) throws Exception;
	/*
	 * survey insert
	 */
	public Integer insertSurvey(Survey survey) throws Exception;
	/*
	 * check voted
	 */
	public Integer checkVote(Survey survey) throws Exception;
	/*
	 * survey가 종료 되었는지 기각(시간이 컷 오프 되어)되었는지 확인
	 */
	public Integer checkSurveyForVote(Survey survey) throws Exception;
	/*
	 * survey update(by vote)
	 */
	public Integer updateVote(Survey survey) throws Exception;
	/*
	 * insert survey detail
	 */
	public Integer insertVoteDetail(Survey survey) throws Exception;
	/*
	 * insert user point by survey vote
	 */
	public Integer insertUserPointWithSurvey(Point point) throws Exception;
	/*
	 * update user's point
	 */
	public Integer updateUserPointWithSurveyVote(User user) throws Exception;
	/*
	 * insert card
	 */
	public Integer insertUserPointWithCard(Card card) throws Exception;
	/*
	 * get user point
	 */
	public List<User> getUserPoint(User user) throws Exception;
	/*
	 * get user point lists
	 */
	public List<Point> getUserPoints(Point point) throws Exception;
}
