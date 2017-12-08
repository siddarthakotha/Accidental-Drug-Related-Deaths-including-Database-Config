package com.project.dao;

import java.util.List;

import com.project.models.DeathPersonDetails;
import com.project.models.RegUser;
import com.project.models.UploadedFile;
import com.project.models.UserUpdatePassword;

public interface UserDao {
	RegUser isUserExists(String email);
	void registerUser(RegUser user);
	RegUser loginUser(RegUser user);
	int changePassword(UserUpdatePassword data);
	void addFile(UploadedFile file)throws Exception;
	List<UploadedFile> getUserUploadedFiles(String email);
	List<DeathPersonDetails> getData(int fromAge, int toAge);
	List<DeathPersonDetails> getData(String sex, String city);
	int getNoOfDocsByRace(String race);
	List<DeathPersonDetails> getData(String race);
	int getNoOfDocsByDrug(String drug);
	List<DeathPersonDetails> getDataByDrug(String drug);
}
