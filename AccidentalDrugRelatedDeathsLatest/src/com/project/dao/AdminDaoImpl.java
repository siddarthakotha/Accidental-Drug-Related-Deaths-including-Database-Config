package com.project.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.project.models.DeathPersonDetails;
import com.project.models.DeathPersonDetailsPrivate;
import com.project.models.RegUser;
import com.project.models.UploadedFile;
import com.project.models.User;

@Repository
public class AdminDaoImpl implements AdminDao {

	MongoTemplate template;

	@Autowired
	public void setTemplate(MongoTemplate template) {
		this.template = template;
	}

	public User amdinLogin(User user) {
		Query qur = new Query(Criteria.where("username").is(user.getUsername()).and("password").is(user.getPassword()));
		User adminUser = template.findOne(qur, User.class);
		return adminUser;
	}

	public List<RegUser> getAllRegUsers() {
		return template.findAll(RegUser.class);
	}

	@Override
	public List<UploadedFile> getUnverifiedFiles() {
		Query qur = new Query(Criteria.where("status").is(0));
		return template.find(qur, UploadedFile.class);
	}

	@Override
	public int changePassword(String oldPassword, String newPassword, String username) {
		Query qur = new Query(Criteria.where("username").is(username).and("password").is(oldPassword));
		WriteResult wr= template.updateFirst(qur, Update.update("password", newPassword), User.class);
		return wr.getN();
	}

	@Override
	public int blockOrUnblockUser(String email, String userId, String status){
		Query qur = new Query(Criteria.where("_id").is(userId));
		Update update = new Update();
		update.set("userStatus", status);
		return template.updateFirst(qur, update, RegUser.class).getN();
	}

	@Override
	public String getFilePath(String fileId) {
		Query qur = new Query(Criteria.where("_id").is(fileId));
		qur.fields().include("filePath");
		UploadedFile file = template.findOne(qur, UploadedFile.class);
		return (file != null)?file.getFilePath():null;
	}

	@Override
	public int acceptFile(String fileId) throws MongoException  {
		Query qur = new Query(Criteria.where("_id").is(fileId));
		return template.updateFirst(qur, Update.update("status", 1), UploadedFile.class).getN();
	}
	
	@Override
	public void insertFileData(List<DeathPersonDetails> list)throws MongoException {
		template.insert(list, DeathPersonDetails.class);
	}
	
	

	@Override
	public void insertPrivateFileData(List<DeathPersonDetailsPrivate> list) {
		template.insert(list, DeathPersonDetailsPrivate.class);
	}

	@Override
	public int rejectFile(String fileId, String reason) {
		Query qur = new Query(Criteria.where("_id").is(fileId));
		Update update = new Update();
		update.set("status", 2);
		update.set("rejectReason", reason);
		return template.updateFirst(qur, update, UploadedFile.class).getN();
	}

	@Override
	public List<UploadedFile> getUserFiles(String email) {
		Query qur = new Query(Criteria.where("email").is(email).and("status").ne(0));
		List<UploadedFile> files = template.find(qur, UploadedFile.class, "uploadedfiles");
		return files;
	}

	@Override
	public String getFileVisibility(String fileId) {
		Query qur = new Query(Criteria.where("_id").is(fileId));
		qur.fields().include("visibility");
		UploadedFile file = template.findOne(qur, UploadedFile.class);
		return file.getVisibility();
	}
	
	
	
}
