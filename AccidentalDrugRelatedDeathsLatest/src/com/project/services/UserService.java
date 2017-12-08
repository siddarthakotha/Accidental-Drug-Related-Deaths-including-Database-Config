package com.project.services;

import java.util.List;

import com.project.models.DeathPersonDetails;
import com.project.models.RegUser;
import com.project.models.UploadedFile;
import com.project.models.UserUpdatePassword;

public interface UserService {
	RegUser getUser(String email);
	boolean registerUser(RegUser user)throws Exception;
	RegUser loginUser(RegUser user);
	boolean changePassword(UserUpdatePassword data);
	boolean addFile(UploadedFile file);
	List<UploadedFile> getUserUploadedFiles(String email);
	List<DeathPersonDetails> getData(int fromAge, int toAge);
	List<DeathPersonDetails> getData(String sex, String city);
	List<DeathPersonDetails> getData();
	List<DeathPersonDetails> getDataByDrug();
}
