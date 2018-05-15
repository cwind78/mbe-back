package com.org.app.main.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.org.app.main.vo.Card;
import com.org.app.main.vo.Code;
import com.org.app.main.vo.Point;
import com.org.app.main.vo.Recommand_Denial;
import com.org.app.main.vo.Recommand_User;
import com.org.app.main.vo.Recommand_filter;
import com.org.app.main.vo.Survey;
import com.org.app.main.vo.Surveys;
import com.org.app.main.vo.User;

@Repository
public class MainDaoImpl implements MainDao {
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	
	public List<User> loginCheck(User user) throws Exception {
		return sqlSessionTemplate.selectList("main.loginCheck", user);
	}
	
	/*
	 *  sign up : duple id check
	 */
	public List<User> dupleCheck(User user) throws Exception {
		return sqlSessionTemplate.selectList("main.dupleCheck", user);
	}
	
	/*
	 * get common codes list
	 */
	public List<Code> getCommonCode(Map<String, List<String>> data) throws Exception {
		return sqlSessionTemplate.selectList("main.getCommonCode", data);
	}
	
	/*
	 * sign up save
	 */
	public Integer signUp(User user) throws Exception {
		return sqlSessionTemplate.insert("main.signUp", user);
	}
	
	/*
	 * filter insert
	 */
	public Integer insertFilter(Recommand_filter filter) throws Exception {
		return sqlSessionTemplate.insert("main.insertFilter", filter);
	}
	/*
	 * filter update
	 */
	public Integer updateFilter(Recommand_filter filter) throws Exception {
		return sqlSessionTemplate.update("main.updateFilter", filter);
	}
	/*
	 * filter delete
	 */
	public Integer deleteFilter(Recommand_filter filter) throws Exception {
		return sqlSessionTemplate.delete("main.deleteFilter", filter);
	}
	/*
	 * get saved recommand filter
	 */
	public List<Recommand_filter> getRecommandFilter(Recommand_filter filter) throws Exception {
		return sqlSessionTemplate.selectList("main.getRecommandFilter", filter);
	}
	/*
	 * get other user's survey list(only today and increasing survey)
	 */
	public List<Survey> getSurveyList(Survey survey) throws Exception {
		return sqlSessionTemplate.selectList("main.getSurveyList", survey);
	}
	/*
	 * get my survey list(only today and increasing survey)
	 */
	public List<Survey> getMySurveyList(Survey survey) throws Exception {
		return sqlSessionTemplate.selectList("main.getMySurveyList", survey);
	}
	/*
	 * get max survey user value
	 */
	public List<Survey> getMaxUserValue(Survey survey) throws Exception {
		return sqlSessionTemplate.selectList("main.getMaxUserValue", survey);
	}
	/*
	 * survey insert check(finished survey and doing survey count)
	 */
	public Integer checkSurvey(Survey survey) throws Exception {
		return sqlSessionTemplate.selectOne("main.checkSurvey", survey);
	}
	/*
	 * survey insert
	 */
	public Integer insertSurvey(Survey survey) throws Exception {
		return sqlSessionTemplate.insert("main.insertSurvey", survey);
	}
	/*
	 * check voted
	 */
	public Integer checkVote(Survey survey) throws Exception {
		return sqlSessionTemplate.selectOne("main.checkVote", survey);
	}
	/*
	 * survey가 종료 되었는지 기각(시간이 컷 오프 되어)되었는지 확인
	 */
	public Integer checkSurveyForVote(Survey survey) throws Exception {
		return sqlSessionTemplate.selectOne("main.checkSurveyForVote", survey);
	}
	/*
	 * survey update(by vote)
	 */
	public Integer updateVote(Survey survey) throws Exception {
		return sqlSessionTemplate.update("main.updateVote", survey);
	}
	/*
	 * insert survey detail
	 */
	public Integer insertVoteDetail(Survey survey) throws Exception {
		return sqlSessionTemplate.insert("main.insertVoteDetail", survey);
	}
	/*
	 * insert user point by survey vote
	 */
	public Integer insertUserPointWithSurvey(Point point) throws Exception {
		return sqlSessionTemplate.insert("main.insertUserPointWithSurvey", point);
	}
	/*
	 * update user's point
	 */
	public Integer updateUserPointWithSurveyVote(User user) throws Exception {
		return sqlSessionTemplate.update("main.updateUserPointWithSurveyVote", user);
	}
	/*
	 * insert card
	 */
	public Integer insertUserPointWithCard(Card card) throws Exception {
		return sqlSessionTemplate.insert("main.insertUserPointWithCard", card);
	}
	/*
	 * get user point
	 */
	public List<User> getUserPoint(User user) throws Exception {
		return sqlSessionTemplate.selectList("main.getUserPoint", user);
	}
	/*
	 * get user point lists
	 */
	public List<Point> getUserPoints(Point point) throws Exception {
		return sqlSessionTemplate.selectList("main.getUserPoints", point);
	}
	/*
	 * recommand user(insert user_recommand)
	 */
	public Integer insertRecommandUser(User user) throws Exception {
		return sqlSessionTemplate.insert("main.insertRecommandUser", user);
	}
	
	/*
	 * get recommand user
	 */
	public List<Recommand_User> getRecommandUser(User user) throws Exception {
		return sqlSessionTemplate.selectList("main.getRecommandUser", user);
	}
	
	/*
	 * update recommand status
	 */
	public Integer updateRecommandStatus(Recommand_User recommand_user) throws Exception {
		return sqlSessionTemplate.update("main.updateRecommandStatus", recommand_user);
	}
	
	/*
	 * insert recommand denial
	 */
	public Integer insertUserRecommandDenial(Recommand_Denial recommand_denial) throws Exception {
		return sqlSessionTemplate.insert("main.insertUserRecommandDenial", recommand_denial);
	}
	
	/*
	 * get denial count on today
	 */
	public Integer checkDenial(User user) throws Exception {
		return sqlSessionTemplate.selectOne("main.checkDenial", user);
	}
	
	/*
	 * get called recommand list
	 */
	public List<User> calledMeRecommandedUser(User user) throws Exception {
		return sqlSessionTemplate.selectList("main.calledMeRecommandedUser", user);
	}
}
