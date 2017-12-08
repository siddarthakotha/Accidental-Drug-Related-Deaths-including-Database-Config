package com.project.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.project.models.DeathPersonDetails;
import com.project.models.RegUser;
import com.project.models.UploadedFile;
import com.project.models.UserUpdatePassword;

@Repository
public class UserDaoImpl implements UserDao {

	MongoTemplate template;

	@Autowired
	public void setTemplate(MongoTemplate template) {
		this.template = template;
	}

	@Override
	public RegUser isUserExists(String email) {
		Query qur = new Query(Criteria.where("email").is(email));
		return template.findOne(qur, RegUser.class);
	}

	@Override
	public void registerUser(RegUser user) throws RuntimeException {
		template.insert(user, "users");
	}

	@Override
	public RegUser loginUser(RegUser user) {
		Query qur = new Query(Criteria.where("email").is(user.getEmail()).and("password").is(user.getPassword()).and("userStatus").is("1"));
		RegUser loggedInUser = template.findOne(qur, RegUser.class);
		if (loggedInUser != null)
			loggedInUser.setPassword(null);
		return loggedInUser;
	}

	@Override
	public int changePassword(UserUpdatePassword data) {
		Query qur = new Query(Criteria.where("email").is(data.getEmail()).and("password").is(data.getOldPassword()));
		return template.updateFirst(qur, Update.update("password", data.getNewPassword()), RegUser.class).getN();
	}

	@Override
	public void addFile(UploadedFile file) throws Exception {
		template.insert(file, "uploadedfiles");
	}

	@Override
	public List<UploadedFile> getUserUploadedFiles(String email) {
		Query qur = new Query(Criteria.where("email").is(email));
		List<UploadedFile> files = template.find(qur, UploadedFile.class, "uploadedfiles");
		return files;
	}

	@Override
	public List<DeathPersonDetails> getData(int fromAge, int toAge) {
		Query qur = new Query(Criteria.where("age").gte(fromAge).lte(toAge));
		return template.find(qur, DeathPersonDetails.class);
	}

	@Override
	public List<DeathPersonDetails> getData(String sex, String city) {
		sex = sex.substring(0, 1).toUpperCase() + sex.substring(1);
		Query qur = new Query(Criteria.where("sex").is(sex).and("deathCity").is(city.toUpperCase()));
		return template.find(qur, DeathPersonDetails.class);
	}

	@Override
	public int getNoOfDocsByRace(String race) {
		Query qur = new Query(Criteria.where("race").is(race));
		return template.find(qur, DeathPersonDetails.class).size();
	}

	@Override
	public List<DeathPersonDetails> getData(String race) {
		Query qur = new Query(Criteria.where("race").is(race));
		return template.find(qur, DeathPersonDetails.class);
	}

	@Override
	public int getNoOfDocsByDrug(String drug) {
		Query qur = new Query(Criteria.where(drug).is("Y"));
		return template.find(qur, DeathPersonDetails.class).size();
	}

	@Override
	public List<DeathPersonDetails> getDataByDrug(String drug) {
		Query qur = new Query(Criteria.where(drug).is("Y"));
		return template.find(qur, DeathPersonDetails.class);
	}
	
}
